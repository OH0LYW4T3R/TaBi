package com.example.tabi.character.component;

import com.example.tabi.character.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import com.example.tabi.character.entity.Character;

@Component
@RequiredArgsConstructor
public class CharacterInitializer implements ApplicationRunner {
    private final CharacterRepository characterRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] extensions = {"png", "jpg", "jpeg", "webp", "svg"};

        for (String ext : extensions) {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:/static/profile-characters/*." + ext);

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) continue;

                String urlPath = "/profile-characters/" + filename;

                if (characterRepository.existsByCharacterURL(urlPath)) continue; // 이미 존재하면 넘어감

                Character character = new Character();

                String baseName = filename.substring(0, filename.lastIndexOf("."));
                String[] parts = baseName.split("_");

                if (parts.length != 2) continue;

                String name = parts[0];
                int rank;
                try {
                    rank = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    continue; // 숫자 아니면 건너뜀
                }

                character.setCharacterName(name);
                character.setRank(rank);
                character.setCharacterURL(urlPath);

                characterRepository.save(character);
            }
        }
    }
}
