package com.fromlabs.inventory.recipeservice.media;

public interface MediaService {

    public MediaDto upload(MediaDto mediaDto);

    public MediaDto getMedia(Long id);
}
