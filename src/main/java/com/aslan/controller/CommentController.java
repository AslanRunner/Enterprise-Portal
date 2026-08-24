package com.aslan.controller;

import com.aslan.dto.DtoCommentRequest;
import com.aslan.dto.DtoCommentResponse;
import com.aslan.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<DtoCommentResponse> addComment(@Valid @RequestBody DtoCommentRequest request) {
        DtoCommentResponse response = commentService.addComment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/announcement/{announcementId}")
    public ResponseEntity<List<DtoCommentResponse>> getCommentsByAnnouncementId(@PathVariable Long announcementId) {
        return ResponseEntity.ok(commentService.getCommentsByAnnouncementId(announcementId));
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<DtoCommentResponse> updateComment(@PathVariable Long commentId,
            @RequestBody DtoCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
