package com.back.article.infrastructure.repository;

import com.back.article.domain.ArticleHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtag, Long> {
}
