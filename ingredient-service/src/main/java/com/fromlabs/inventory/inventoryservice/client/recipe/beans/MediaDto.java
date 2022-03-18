package com.fromlabs.inventory.inventoryservice.client.recipe.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private Long id;
    private String name;
    private String mediaType;
    private String link;
    private MultipartFile mediaFile;
    private String createdAt;
    private String updatedAt;
}
