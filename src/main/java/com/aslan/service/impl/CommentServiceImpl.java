package com.aslan.service.impl;

import com.aslan.dto.DtoCommentRequest;
import com.aslan.dto.DtoCommentResponse;
import com.aslan.entity.Announcement;
import com.aslan.entity.Comment;
import com.aslan.entity.Personel;
import com.aslan.repository.AnnouncementRepository;
import com.aslan.repository.CommentRepository;
import com.aslan.repository.PersonelRepository;
import com.aslan.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private PersonelRepository personelRepository;

    @Override
    public DtoCommentResponse addComment(DtoCommentRequest request) {
        Personel personel = personelRepository.findById(request.getPersonelId()).orElseThrow(() -> new RuntimeException("Personel not found"));
        Announcement announcement = announcementRepository.findById(request.getAnnouncementId()).orElseThrow(() -> new RuntimeException("Announcement not found"));
        
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(personel);
        comment.setAnnouncement(announcement);
        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<DtoCommentResponse> getCommentsByAnnouncementId(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(() -> new RuntimeException("Not found"));
        return announcement.getComments().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public DtoCommentResponse updateComment(Long commentId, DtoCommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Not found"));
        comment.setContent(request.getContent());
        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private DtoCommentResponse mapToDto(Comment comment) {
        DtoCommentResponse dto = new DtoCommentResponse();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreationTime(comment.getCreationTime());
        dto.setAuthorName(comment.getAuthor() != null ? comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName() : null);
        return dto;
    }
}
