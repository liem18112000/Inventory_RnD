package com.fromlabs.inventory.recipeservice.media.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<MediaEntity, Long> {
}