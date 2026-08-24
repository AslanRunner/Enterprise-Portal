package com.aslan.service;

import com.aslan.dto.DtoCommentRequest;
import com.aslan.dto.DtoCommentResponse;

import java.util.List;


public interface CommentService {
    DtoCommentResponse addComment(DtoCommentRequest request);
    List<DtoCommentResponse> getCommentsByAnnouncementId(Long announcementId);
    DtoCommentResponse updateComment(Long commentId, DtoCommentRequest request);
    void deleteComment(Long commentId);
}
