package com.aslan.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DtoAnnouncementResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationTime;
    private Integer likeCount;
    private String authorName;
    private List<DtoCommentResponse> comments;
}
