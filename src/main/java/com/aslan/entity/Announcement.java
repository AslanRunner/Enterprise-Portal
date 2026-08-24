package com.aslan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personel_id", nullable = false)
    private Personel author;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true) // Deleting an announcement cascades to remove all associated comments
    private List<Comment> comments;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime = LocalDateTime.now();

}
