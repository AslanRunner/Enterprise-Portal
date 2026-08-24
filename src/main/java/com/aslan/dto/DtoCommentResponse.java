package com.aslan.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoCommentResponse {
    private Long id;
    private String content;
    private LocalDateTime creationTime;
    private String authorName;
}
