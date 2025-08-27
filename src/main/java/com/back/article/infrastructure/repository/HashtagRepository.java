package com.back.article.infrastructure.repository;

import com.back.article.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Set<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
