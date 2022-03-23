package com.fromlabs.inventory.recipeservice.media.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class MediaDto {
    private Long id;
    private String name;

    @Builder.Default
    private String mediaType = MediaType.IMAGE.getType();

    private String link;
    private MultipartFile mediaFile;
    private String createdAt;
    private String updatedAt;
    private final String accessedAt = Instant.now().toString();
}
