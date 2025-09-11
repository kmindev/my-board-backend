package org.kmin.board.domain.article.repository;

import org.kmin.board.domain.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ArticleCustomRepository {

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);

}
