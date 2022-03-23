package com.fromlabs.inventory.recipeservice.media;

import com.fromlabs.inventory.recipeservice.media.bean.MediaDto;

public interface MediaService {

    public MediaDto upload(MediaDto mediaDto);

    public MediaDto getMedia(Long id);
}
