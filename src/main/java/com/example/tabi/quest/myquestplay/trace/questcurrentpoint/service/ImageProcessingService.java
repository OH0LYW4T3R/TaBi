package com.example.tabi.quest.myquestplay.trace.questcurrentpoint.service;

import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageProcessingService {
    boolean checkKeywordsInImage(MultipartFile imageFile, List<PhotoKeyword> photoKeywords);
}
