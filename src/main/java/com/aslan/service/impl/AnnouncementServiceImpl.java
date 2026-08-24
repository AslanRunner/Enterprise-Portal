package com.aslan.service.impl;

import com.aslan.dto.DtoAnnouncementRequest;
import com.aslan.dto.DtoAnnouncementResponse;
import com.aslan.dto.DtoCommentResponse;
import com.aslan.entity.Announcement;
import com.aslan.entity.Comment;
import com.aslan.entity.Personel;
import com.aslan.repository.AnnouncementRepository;
import com.aslan.repository.PersonelRepository;
import com.aslan.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private PersonelRepository personelRepository;

    @Override
    public DtoAnnouncementResponse createAnnouncement(DtoAnnouncementRequest request) {
        Personel personel = personelRepository.findById(request.getPersonelId())
                .orElseThrow(() -> new RuntimeException("Personel not found"));
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setAuthor(personel);
        return mapToDto(announcementRepository.save(announcement));
    }

    @Override
    public List<DtoAnnouncementResponse> getAllAnnouncement() {
        return announcementRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void likeAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        announcement.setLikeCount(announcement.getLikeCount() + 1);
        announcementRepository.save(announcement);
    }

    @Override
    public DtoAnnouncementResponse getAnnouncementById(Long announcementId) {
        return mapToDto(
                announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Override
    public DtoAnnouncementResponse updateAnnouncement(Long announcementId, DtoAnnouncementRequest request) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        return mapToDto(announcementRepository.save(announcement));
    }

    @Override
    public void deleteAnnouncement(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }

    private DtoAnnouncementResponse mapToDto(Announcement announcement) {
        DtoAnnouncementResponse dto = new DtoAnnouncementResponse();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setCreationTime(announcement.getCreationTime());
        dto.setLikeCount(announcement.getLikeCount());
        dto.setAuthorName(announcement.getAuthor() != null
                ? announcement.getAuthor().getFirstName() + " " + announcement.getAuthor().getLastName()
                : null);

        List<DtoCommentResponse> comments = new ArrayList<>();
        if (announcement.getComments() != null) {
            comments = announcement.getComments().stream().map(this::mapCommentToDto).collect(Collectors.toList());
        }
        dto.setComments(comments);
        return dto;
    }

    private DtoCommentResponse mapCommentToDto(Comment comment) {
        DtoCommentResponse dto = new DtoCommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreationTime(comment.getCreationTime());
        dto.setAuthorName(comment.getAuthor() != null
                ? comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName()
                : null);
        return dto;
    }
}
