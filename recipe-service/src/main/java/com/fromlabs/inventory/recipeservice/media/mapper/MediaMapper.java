package com.fromlabs.inventory.recipeservice.media.mapper;

import com.fromlabs.inventory.recipeservice.media.bean.MediaDto;
import com.fromlabs.inventory.recipeservice.media.entity.MediaEntity;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class MediaMapper {
    public MediaDto toDto(MediaEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        return MediaDto.builder()
                .id(entity.getId())
                .name(entity.getTitle())
                .link(entity.getMediaLink())
                .mediaType(entity.getMediaType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
