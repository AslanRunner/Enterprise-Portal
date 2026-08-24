package com.aslan.dto;

import lombok.Data;

@Data
public class DtoCommentRequest {
    private String content;
    private Long announcementId;
    private Long personelId;
}
