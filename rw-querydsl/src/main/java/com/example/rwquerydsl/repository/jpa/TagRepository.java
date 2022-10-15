package com.example.rwquerydsl.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.rwquerydsl.domain.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTag(String tag);
}
