package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageProcessingServiceImpl implements ImageProcessingService {
    private final RestTemplate restTemplate;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.model}")
    private String model;
    @Override
    public boolean checkKeywordsInImage(MultipartFile imageFile, List<PhotoKeyword> photoKeywords) {
        if (imageFile == null || imageFile.isEmpty() || photoKeywords == null || photoKeywords.isEmpty()) {
            log.info("Image or Keywords are empty. Returning true by default.");
            return true;
        }

        try {
            String keywords = photoKeywords.stream()
                    .map(PhotoKeyword::getKeyword)
                    .collect(Collectors.joining(", "));

            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            String dataUrl = "data:image/jpeg;base64," + base64Image;

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 10);

            List<Map<String, Object>> messages = new ArrayList<>();
            Map<String, Object> userMessage = new HashMap<>();
            userMessage.put("role", "user");

            List<Map<String, Object>> contentList = new ArrayList<>();

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");

            String prompt = String.format(
                    "Does this image contain ALL of the following items or concepts: [%s]? " +
                    "Answer strictly with 'TRUE' if all exist, or 'FALSE' otherwise.",
                    keywords
            );
            textContent.put("text", prompt);
            contentList.add(textContent);


            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image_url");
            Map<String, String> imageUrlMap = new HashMap<>();
            imageUrlMap.put("url", dataUrl);
            imageUrlMap.put("detail", "auto");
            imageContent.put("image_url", imageUrlMap);
            contentList.add(imageContent);

            userMessage.put("content", contentList);
            messages.add(userMessage);
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) messageObj.get("content");

                    log.info("GPT Vision Analysis Result: {}", content);

                    return content != null && content.trim().toUpperCase().contains("TRUE");
                }
            }

            return true;

        } catch (Exception e) {
            log.error("Error during OpenAI image analysis. Fallback to TRUE. Error: {}", e.getMessage());
            return true;
        }
    }
}
