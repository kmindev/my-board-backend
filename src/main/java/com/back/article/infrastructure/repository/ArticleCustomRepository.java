package com.back.article.infrastructure.repository;

import com.back.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ArticleCustomRepository {

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);

}
