package com.aslan.service;

import com.aslan.dto.DtoAnnouncementRequest;
import com.aslan.dto.DtoAnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    DtoAnnouncementResponse createAnnouncement(DtoAnnouncementRequest request);

    List<DtoAnnouncementResponse> getAllAnnouncement();

    void likeAnnouncement(Long announcementId);

    DtoAnnouncementResponse getAnnouncementById(Long announcementId);

    DtoAnnouncementResponse updateAnnouncement(Long announcementId, DtoAnnouncementRequest request);

    void deleteAnnouncement(Long announcementId);
}
