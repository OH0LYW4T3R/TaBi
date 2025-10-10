package com.example.tabi.quest.questpostcomment.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.quest.questpost.entity.QuestPost;
import com.example.tabi.quest.questpost.repository.QuestPostRepository;
import com.example.tabi.quest.questpostcomment.entity.QuestPostComment;
import com.example.tabi.quest.questpostcomment.repository.QuestPostCommentRepository;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentDto;
import com.example.tabi.quest.questpostcomment.vo.QuestPostCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestPostCommentServiceJpaImpl implements QuestPostCommentService {
    private static final int PAGE_SIZE = 20;
    private static final int CHILD_PAGE_SIZE = 10;

    private final QuestPostCommentRepository questPostCommentRepository;
    private final QuestPostRepository questPostRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    @Override
    public QuestPostCommentDto createQuestPostComment(Authentication authentication, QuestPostCommentRequest questPostCommentRequest) {
        QuestPostCommentDto questPostCommentDto = new QuestPostCommentDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            questPostCommentDto.setErrorMessage("AppUser Not Found");
            return questPostCommentDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<QuestPost> optionalQuestPost = questPostRepository.findById(questPostCommentRequest.getQuestPostId());

        if (optionalQuestPost.isEmpty()) {
            questPostCommentDto.setErrorMessage("QuestPost Not Found");
            return questPostCommentDto;
        }

        QuestPost questPost = optionalQuestPost.get();

        QuestPostComment parent = null;
        if (questPostCommentRequest.getParentQuestPostCommentId() != null) {
            parent = questPostCommentRepository.findById(questPostCommentRequest.getParentQuestPostCommentId()).orElse(null);

            if (parent == null) {
                questPostCommentDto.setErrorMessage("QuestPostComment Not Found");
                return questPostCommentDto;
            }

            if (!parent.getQuestPost().getQuestPostId().equals(questPost.getQuestPostId())) {
                questPostCommentDto.setErrorMessage("Parent comments and posts are different");
                return questPostCommentDto;
            }

            if (parent.getParent() != null) {
                questPostCommentDto.setErrorMessage("Reply does not allow comments");
                return questPostCommentDto;
            }
        }

        QuestPostComment questPostComment = new QuestPostComment();
        questPostComment.setQuestPost(questPost);
        questPostComment.setAppUser(appUser);
        questPostComment.setComment(questPostCommentRequest.getComment());
        questPostComment.setParent(parent);
        questPostCommentRepository.save(questPostComment);

        return QuestPostCommentDto.questPostCommentToQuestPostCommentDto(questPostComment, null);
    }

    @Override
    public String deleteQuestPostComment(Authentication authentication, Long questPostCommentId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "You do not have permission to delete it";

        AppUser appUser = optionalAppUser.get();

        Optional<QuestPostComment> optionalQuestPostComment = questPostCommentRepository.findByQuestPostCommentIdAndAppUser(questPostCommentId, appUser);
        if (optionalQuestPostComment.isEmpty())
            return "Post that doesn't exist";

        questPostCommentRepository.delete(optionalQuestPostComment.get());

        return "Delete Successfully";
    }

    @Override
    public List<QuestPostCommentDto> getQuestPostComments(Authentication authentication, QuestPostCommentRequest questPostCommentRequest, int page) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        Optional<QuestPost> optionalQuestPost = questPostRepository.findById(questPostCommentRequest.getQuestPostId());

        if (optionalQuestPost.isEmpty())
            return null;

        QuestPost questPost = optionalQuestPost.get();

        return questPostCommentRepository.findByQuestPostAndParentIsNullOrderByLikeCountDescCreatedAtDesc(questPost, PageRequest.of(page, PAGE_SIZE))
        .map(questPostComment -> QuestPostCommentDto.questPostCommentToQuestPostCommentDto(questPostComment, null)).getContent();
    }

    @Override
    public List<QuestPostCommentDto> getQuestPostChildrenComments(Authentication authentication, QuestPostCommentRequest questPostCommentRequest, int page) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        Optional<QuestPost> optionalQuestPost = questPostRepository.findById(questPostCommentRequest.getQuestPostId());

        if (optionalQuestPost.isEmpty())
            return null;

        Optional<QuestPostComment> optionalQuestPostComment = questPostCommentRepository.findById(questPostCommentRequest.getParentQuestPostCommentId());

        if (optionalQuestPostComment.isEmpty())
            return null;

        QuestPostComment parent = optionalQuestPostComment.get();

        return questPostCommentRepository.findByParentOrderByLikeCountDescCreatedAtDesc(parent, PageRequest.of(page, CHILD_PAGE_SIZE))
        .map(questPostComment -> QuestPostCommentDto.questPostCommentToQuestPostCommentDto(questPostComment, null)).getContent();
    }
}
