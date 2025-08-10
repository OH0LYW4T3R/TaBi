package com.example.tabi.treasurehunt.treasurehuntpostcomment.service;

import com.example.tabi.appuser.entity.AppUser;
import com.example.tabi.appuser.repository.AppUserRepository;
import com.example.tabi.appuser.service.AppUserServiceJpaImpl;
import com.example.tabi.member.repository.MemberRepository;
import com.example.tabi.treasurehunt.treasurehuntpost.entity.TreasureHuntPost;
import com.example.tabi.treasurehunt.treasurehuntpost.repository.TreasureHuntPostRepository;
import com.example.tabi.treasurehunt.treasurehuntpost.vo.TreasureHuntPostRequest;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.entity.TreasureHuntPostComment;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.repository.TreasureHuntPostCommentRepository;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentDto;
import com.example.tabi.treasurehunt.treasurehuntpostcomment.vo.TreasureHuntPostCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreasureHuntPostCommentServiceJpaImpl implements TreasureHuntPostCommentService {
    private static final int PAGE_SIZE = 20;
    private static final int CHILD_PAGE_SIZE = 10;

    private final TreasureHuntPostCommentRepository treasureHuntPostCommentRepository;
    private final TreasureHuntPostRepository treasureHuntPostRepository;
    private final AppUserRepository appUserRepository;
    private final MemberRepository memberRepository;

    @Override
    public TreasureHuntPostCommentDto createTreasureHuntPostComment(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest) {
        TreasureHuntPostCommentDto treasureHuntPostCommentDto = new TreasureHuntPostCommentDto();
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty()) {
            treasureHuntPostCommentDto.setErrorMessage("AppUser Not Found");
            return treasureHuntPostCommentDto;
        }

        AppUser appUser = optionalAppUser.get();

        Optional<TreasureHuntPost> optionalTreasureHuntPost = treasureHuntPostRepository.findById(treasureHuntPostCommentRequest.getTreasureHuntPostId());

        if (optionalTreasureHuntPost.isEmpty()) {
            treasureHuntPostCommentDto.setErrorMessage("TreasureHuntPost Not Found");
            return treasureHuntPostCommentDto;
        }

        TreasureHuntPost treasureHuntPost = optionalTreasureHuntPost.get();

        TreasureHuntPostComment parent = null;
        if (treasureHuntPostCommentRequest.getParentTreasureHuntPostCommentId() != null) {
            parent = treasureHuntPostCommentRepository.findById(treasureHuntPostCommentRequest.getParentTreasureHuntPostCommentId()).orElse(null);

            if (parent == null) {
                treasureHuntPostCommentDto.setErrorMessage("Parent Comment Not Found");
                return treasureHuntPostCommentDto;
            }

            // 1) 부모와 같은 게시물인지 검증 (무결성)
            if (!parent.getTreasureHuntPost().getTreasureHuntPostId().equals(treasureHuntPost.getTreasureHuntPostId())) {
                treasureHuntPostCommentDto.setErrorMessage("Parent comments and posts are different");
                return treasureHuntPostCommentDto;
            }

            // 2) 1단계 대댓글만 허용 (인스타 스타일)
            if (parent.getParent() != null) {
                treasureHuntPostCommentDto.setErrorMessage("Reply does not allow comments.");
                return treasureHuntPostCommentDto;
            }
        }

        TreasureHuntPostComment treasureHuntPostComment = new TreasureHuntPostComment();
        treasureHuntPostComment.setTreasureHuntPost(treasureHuntPost);
        treasureHuntPostComment.setAppUser(appUser);
        treasureHuntPostComment.setComment(treasureHuntPostCommentRequest.getComment());
        treasureHuntPostComment.setParent(parent);
        treasureHuntPostCommentRepository.save(treasureHuntPostComment);

        return treasureHuntPostCommentServiceToTreasureHuntPostCommentServiceDto(treasureHuntPostComment);
    }

    @Override
    public String deleteTreasureHuntPostComment(Authentication authentication, Long commentId) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return "You do not have permission to delete it";

        AppUser appUser = optionalAppUser.get();

        Optional<TreasureHuntPostComment> optionalTreasureHuntPostComment = treasureHuntPostCommentRepository.findByTreasureHuntPostCommentIdAndAppUser(commentId, appUser);

        if (optionalTreasureHuntPostComment.isEmpty())
            return "Post that doesn't exist";

        treasureHuntPostCommentRepository.delete(optionalTreasureHuntPostComment.get());

        return "Delete Successfully";
    }

    @Override
    public List<TreasureHuntPostCommentDto> getTreasureHuntPostComments(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, int page) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        Optional<TreasureHuntPost> optionalTreasureHuntPost = treasureHuntPostRepository.findById(treasureHuntPostCommentRequest.getTreasureHuntPostId());

        if (optionalTreasureHuntPost.isEmpty())
            return null;

        TreasureHuntPost treasureHuntPost = optionalTreasureHuntPost.get();

        return treasureHuntPostCommentRepository.findByTreasureHuntPostAndParentIsNullOrderByLikeCountDescCreatedAtDesc(treasureHuntPost, PageRequest.of(page, PAGE_SIZE))
                .map(TreasureHuntPostCommentServiceJpaImpl::treasureHuntPostCommentServiceToTreasureHuntPostCommentServiceDto).getContent();
    }

    @Override
    public List<TreasureHuntPostCommentDto> getTreasureHuntPostChildrenComments(Authentication authentication, TreasureHuntPostCommentRequest treasureHuntPostCommentRequest, int page) {
        Optional<AppUser> optionalAppUser = AppUserServiceJpaImpl.authenticationToAppUser(authentication, memberRepository, appUserRepository);

        if (optionalAppUser.isEmpty())
            return null;

        AppUser appUser = optionalAppUser.get();

        Optional<TreasureHuntPost> optionalTreasureHuntPost = treasureHuntPostRepository.findById(treasureHuntPostCommentRequest.getTreasureHuntPostId());

        if (optionalTreasureHuntPost.isEmpty())
            return null;

        Optional<TreasureHuntPostComment> optionalTreasureHuntPostComment = treasureHuntPostCommentRepository.findById(treasureHuntPostCommentRequest.getParentTreasureHuntPostCommentId());

        if (optionalTreasureHuntPostComment.isEmpty())
            return null;

        TreasureHuntPostComment treasureHuntPostComment = optionalTreasureHuntPostComment.get();

        return treasureHuntPostCommentRepository.findByParentOrderByLikeCountDescCreatedAtDesc(treasureHuntPostComment, PageRequest.of(page, CHILD_PAGE_SIZE))
                .map(TreasureHuntPostCommentServiceJpaImpl::treasureHuntPostCommentServiceToTreasureHuntPostCommentServiceDto).getContent();
    }

    public static TreasureHuntPostCommentDto treasureHuntPostCommentServiceToTreasureHuntPostCommentServiceDto(TreasureHuntPostComment treasureHuntPostComment) {
        return new TreasureHuntPostCommentDto(
            treasureHuntPostComment.getTreasureHuntPostCommentId(),
            treasureHuntPostComment.getTreasureHuntPost().getTreasureHuntPostId(),
            treasureHuntPostComment.getAppUser().getAppUserId(),
            treasureHuntPostComment.getAppUser().getMyProfile().getNickName(),
            treasureHuntPostComment.getAppUser().getMyProfile().getProfileImageUrl(),
            treasureHuntPostComment.getComment(),
            treasureHuntPostComment.getLikeCount(),
            treasureHuntPostComment.getCreatedAt(),
            treasureHuntPostComment.getUpdatedAt(),
            (treasureHuntPostComment.getParent() == null ? null : treasureHuntPostComment.getParent().getTreasureHuntPostCommentId()),
            treasureHuntPostComment.getChildren().size(),
null
        );
    }
}
