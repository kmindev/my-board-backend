package org.kmin.board.api.article.infrastructure.repository;

import org.kmin.board.api.article.domain.ArticleHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtag, Long> {
}
