package com.aslan.dto;

import lombok.Data;

@Data
public class DtoAnnouncementRequest {
    private Long id;
    private String title;
    private String content;
    private Long personelId;
}
