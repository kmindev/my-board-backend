package org.kmin.board.domain.article.repository;

import org.kmin.board.domain.article.ArticleHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtag, Long> {
}
