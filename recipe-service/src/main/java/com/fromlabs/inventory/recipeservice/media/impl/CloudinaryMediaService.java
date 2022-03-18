package com.fromlabs.inventory.recipeservice.media.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fromlabs.inventory.recipeservice.media.bean.MediaDto;
import com.fromlabs.inventory.recipeservice.media.MediaService;
import com.fromlabs.inventory.recipeservice.media.entity.MediaEntity;
import com.fromlabs.inventory.recipeservice.media.entity.MediaRepository;
import com.fromlabs.inventory.recipeservice.media.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudinaryMediaService implements MediaService {

    public static final String MEDIA_URL = "secure_url";
    private final Cloudinary cloudinary;

    private final MediaRepository repository;

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void validateUpload(final MediaDto mediaDto) {
        if (Objects.isNull(mediaDto)) {
            throw new IllegalArgumentException("Media is null");
        }

        if (!StringUtils.hasText(mediaDto.getName())) {
            throw new IllegalArgumentException("Media name is null or empty");
        }

        if (Objects.isNull(mediaDto.getMediaFile())) {
            throw new IllegalArgumentException("Media file is null");
        }
    }

    @Override
    public MediaDto upload(MediaDto mediaDto) {

        this.validateUpload(mediaDto);
        Map uploadResult;
        try {
            uploadResult = uploadMediaToCloudinary(mediaDto.getMediaFile());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        final var media = this.repository.findByTitle(mediaDto.getName());
        final var savedMedia = media
            .map(entity -> updateMediaLink(entity, uploadResult))
            .orElseGet(() -> saveMedia(mediaDto, uploadResult));
        return MediaMapper.toDto(savedMedia);
    }

    private Map uploadMediaToCloudinary(MultipartFile mediaFile) throws IOException {
        File uploadedFile = this.convertMultiPartToFile(mediaFile);
        final var uploadResult = this.cloudinary.uploader()
                .upload(uploadedFile, ObjectUtils.emptyMap());
        boolean isDeleted = uploadedFile.delete();
        if (isDeleted){
            log.info("Temporary file successfully deleted");
        } else {
            log.warn("Temporary file doesn't exist");
        }
        return uploadResult;
    }

    private MediaEntity updateMediaLink(
            @NotNull MediaEntity media, final @NotNull Map uploadResult) {
        media.setMediaLink(uploadResult.get(MEDIA_URL).toString());
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            media.setExtraInformation(mapper.writeValueAsString(uploadResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
        media.setUpdatedAt(Instant.now().toString());
        return this.repository.save(media);
    }

    private MediaEntity saveMedia(
            final @NotNull MediaDto mediaDto, final Map uploadResult) {
        var entity = new MediaEntity();
        entity.setTitle(mediaDto.getName());
        entity.setMediaLink(uploadResult.get(MEDIA_URL).toString());
        entity.setCreatedAt(Instant.now().toString());
        entity.setMediaType(mediaDto.getMediaType());
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            entity.setExtraInformation(mapper.writeValueAsString(uploadResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
        entity.setUpdatedAt(entity.getCreatedAt());
        return this.repository.save(entity);
    }

    @Override
    public MediaDto getMedia(final @NotNull Long id) {
        return MediaMapper.toDto(this.repository.findById(id).orElse(null));
    }
}
