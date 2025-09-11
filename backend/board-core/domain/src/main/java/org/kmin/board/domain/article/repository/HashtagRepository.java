package org.kmin.board.domain.article.repository;

import org.kmin.board.domain.article.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Set<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
