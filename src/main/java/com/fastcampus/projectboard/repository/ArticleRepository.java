package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // Article에 기본 검색 기능을 추가해줌
        QuerydslBinderCustomizer<QArticle>
{
    //검색에 대한 세부적인 규칙 재구성
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${value}'
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '${value}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // like '%${value}%' containsIgnoreCase 대소문자 구분안하게
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); // like '%${value}%' containsIgnoreCase 대소문자 구분안하게
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); //
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); //
    }
}
