package com.example.tabi.postcounter.service;

import com.example.tabi.postcounter.entity.PostCounter;
import com.example.tabi.postcounter.repository.PostCounterRepository;
import com.example.tabi.postcounter.vo.PostCounterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCounterServiceJpaImpl implements PostCounterService {
    private final PostCounterRepository postCounterRepository;

    @Override
    public PostCounter createPostCounter() {
        PostCounter postCounter = new PostCounter();
        postCounter.setLikeCount(0);
        postCounter.setPlayCount(0);
        postCounter.setShareCount(0);
        postCounter.setCommentCount(0);
        postCounter.setReportCount(0);

        postCounterRepository.save(postCounter);
        postCounterRepository.flush();

        return postCounter;
    }

    public static PostCounterDto postCounterToPostCounterDto(PostCounter postCounter) {
        return new PostCounterDto(postCounter.getPostCounterId(), postCounter.getLikeCount(), postCounter.getPlayCount(), postCounter.getShareCount(), postCounter.getCommentCount(), postCounter.getReportCount());
    }
}
