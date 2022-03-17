package com.fromlabs.inventory.recipeservice.media.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity(name = "MediaEntity")
@Table(name = "media")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "media_link")
    private String mediaLink;

    @Column(name = "media_encoded", columnDefinition = "TEXT")
    private String mediaEncoded;

    @Column(name = "media_type", nullable = false)
    private String mediaType = "image";

    @Column(name = "created_at")
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MediaEntity that = (MediaEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
