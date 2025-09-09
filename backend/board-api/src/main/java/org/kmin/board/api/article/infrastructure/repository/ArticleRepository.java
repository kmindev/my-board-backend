package org.kmin.board.api.article.infrastructure.repository;

import org.kmin.board.api.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleCustomRepository {

    Page<Article> findByTitleContaining(String searchValue, Pageable pageable);

    Page<Article> findByContentContaining(String searchValue, Pageable pageable);

    Page<Article> findByUserAccount_UserIdContaining(String userAccountUserId, Pageable pageable);

    Page<Article> findByUserAccount_NicknameContaining(String userAccountNickname, Pageable pageable);


}
