package com.fromlabs.inventory.recipeservice.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class MediaDto {
    private Long id;
    private String name;
    private String mediaType;
    private String link;
    private MultipartFile mediaFile;
    private String createdAt;
}
