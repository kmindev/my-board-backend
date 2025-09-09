package org.kmin.board.api.article.infrastructure.repository;

import org.kmin.board.api.article.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Set<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
