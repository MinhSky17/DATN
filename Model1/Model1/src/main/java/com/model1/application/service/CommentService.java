package com.model1.application.service;

import com.model1.application.entity.Comment;
import com.model1.application.model.request.CreateCommentPostRequest;
import com.model1.application.model.request.CreateCommentProductRequest;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest,long userId);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId);
}
