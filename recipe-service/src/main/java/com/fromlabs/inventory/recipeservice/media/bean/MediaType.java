package com.fromlabs.inventory.recipeservice.media.bean;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MediaType {
    IMAGE("image", List.of("png", "jpg", "jfif", "jpeg", "gif")),
    VIDEO("video", List.of("mp4", "webm"));

    private final String type;
    private final List<String> allowExt;

    MediaType(String type, List<String> allowExt) {
        this.type = type;
        this.allowExt = allowExt;
    }

    public static MediaType getMediaType(String type){
        return Arrays.stream(MediaType.values())
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }

    public static boolean isMediaExtInclude(MediaType mediaType, String allowExt) {
        return mediaType.getAllowExt().contains(allowExt);
    }
}
