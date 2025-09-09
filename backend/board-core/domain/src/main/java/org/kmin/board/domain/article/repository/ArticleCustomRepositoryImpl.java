package org.kmin.board.domain.article.repository;

import org.kmin.board.domain.article.Article;


import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.kmin.board.domain.article.QArticle;
import org.kmin.board.domain.article.QArticleHashtag;
import org.kmin.board.domain.article.QHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;
        QArticleHashtag articleHashtag = QArticleHashtag.articleHashtag;

        List<Article> articles = queryFactory
                .select(article)
                .from(article)
                .innerJoin(article.articleHashtags, articleHashtag)
                .innerJoin(articleHashtag.hashtag, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(article.countDistinct())
                .from(article)
                .innerJoin(article.articleHashtags, articleHashtag)
                .innerJoin(articleHashtag.hashtag, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames))
                .fetchOne();

        return new PageImpl<>(articles, pageable, count == null ? 0 : count);
    }

}
