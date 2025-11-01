package com.example.tabi.quest.actions.photopuzzleaction.service;

import com.example.tabi.quest.actions.hint.entity.Hint;
import com.example.tabi.quest.actions.hint.service.HintService;
import com.example.tabi.quest.actions.hint.vo.HintRequest;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoKeyword;
import com.example.tabi.quest.actions.photopuzzleaction.entity.PhotoPuzzleAction;
import com.example.tabi.quest.actions.photopuzzleaction.repository.PhotoKeywordRepository;
import com.example.tabi.quest.actions.photopuzzleaction.repository.PhotoPuzzleActionRepository;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoKeywordRequest;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionDto;
import com.example.tabi.quest.actions.photopuzzleaction.vo.PhotoPuzzleActionRequest;
import com.example.tabi.quest.actions.stayingaction.service.StayingActionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoPuzzleActionServiceJpaImpl implements PhotoPuzzleActionService {
    private final PhotoPuzzleActionRepository photoPuzzleActionRepository;
    private final PhotoKeywordRepository photoKeywordRepository;
    private final HintService hintService;

    @Override
    @Transactional
    public PhotoPuzzleAction createPhotoPuzzleAction(PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest) {
        PhotoPuzzleAction photoPuzzleAction = new PhotoPuzzleAction();

        photoPuzzleAction.setCharacterImageUrl(photoPuzzleActionRequest.getCharacterImageUrl());
        photoPuzzleAction.setQuestStep(photoPuzzleActionRequest.getQuestStep());
        photoPuzzleActionRepository.save(photoPuzzleAction);

        for (PhotoKeywordRequest photoKeywordRequest : Objects.requireNonNullElse(photoPuzzleActionRequest.getPhotoKeywordRequests(), java.util.Collections.<PhotoKeywordRequest>emptyList())) {
            PhotoKeyword photoKeyword = new PhotoKeyword();
            photoKeyword.setKeyword(photoKeywordRequest.getKeyword());
            photoKeyword.setPhotoPuzzleAction(photoPuzzleAction);
            photoKeywordRepository.save(photoKeyword);

            photoPuzzleAction.getPhotoKeywords().add(photoKeyword);
        }

        photoPuzzleAction.setHint(hintService.createHint(hintRequest));

        photoPuzzleActionRepository.save(photoPuzzleAction);

        return photoPuzzleAction;
    }

    @Override
    public PhotoPuzzleAction retrievePhotoPuzzleAction(Long photoPuzzleActionId) {
        Optional<PhotoPuzzleAction> photoPuzzleActionOptional = photoPuzzleActionRepository.findById(photoPuzzleActionId);
        return photoPuzzleActionOptional.orElse(null);

    }

    @Override
    @Transactional
    public PhotoPuzzleAction updatePhotoPuzzleAction(Long photoPuzzleActionId, PhotoPuzzleActionRequest photoPuzzleActionRequest, HintRequest hintRequest) {
        Optional<PhotoPuzzleAction> photoPuzzleActionOptional = photoPuzzleActionRepository.findById(photoPuzzleActionId);
        if (photoPuzzleActionOptional.isEmpty()) return null;

        PhotoPuzzleAction photoPuzzleAction = photoPuzzleActionOptional.get();

        if (photoPuzzleActionRequest.getCharacterImageUrl() != null)
            photoPuzzleAction.setCharacterImageUrl(photoPuzzleActionRequest.getCharacterImageUrl());

        if (photoPuzzleActionRequest.getQuestStep() != null)
            photoPuzzleAction.setQuestStep(photoPuzzleActionRequest.getQuestStep());

        if (photoPuzzleActionRequest.getPhotoKeywordRequests() != null) {
            if (photoPuzzleAction.getPhotoKeywords() != null && !photoPuzzleAction.getPhotoKeywords().isEmpty()) {
                Iterator<PhotoKeyword> photoKeywordIterator = photoPuzzleAction.getPhotoKeywords().iterator();

                // ConcurrentModificationException 제거
                while (photoKeywordIterator.hasNext()) {
                    PhotoKeyword photoKeyword = photoKeywordIterator.next();
                    photoKeywordIterator.remove();
                    photoKeywordRepository.delete(photoKeyword);
                }
            }

            for (PhotoKeywordRequest photoKeywordRequest : photoPuzzleActionRequest.getPhotoKeywordRequests()) {
                PhotoKeyword photoKeyword = new PhotoKeyword();
                photoKeyword.setKeyword(photoKeywordRequest.getKeyword());
                photoKeyword.setPhotoPuzzleAction(photoPuzzleAction);
                photoKeywordRepository.save(photoKeyword);

                photoPuzzleAction.getPhotoKeywords().add(photoKeyword);
            }
        }

        if (hintRequest != null) {
            photoPuzzleAction.setHint(hintService.updateHint(photoPuzzleAction.getHint().getHintId(), hintRequest));
        }

        photoPuzzleActionRepository.save(photoPuzzleAction);
        return photoPuzzleAction;
    }

    @Override
    @Transactional
    public void deletePhotoPuzzleAction(Long photoPuzzleActionId) {
        photoPuzzleActionRepository.deleteById(photoPuzzleActionId);
    }
}
