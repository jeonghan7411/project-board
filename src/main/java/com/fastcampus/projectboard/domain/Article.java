package com.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
@Entity
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoIncrement를 위해
    private Long id;

    @Setter
    @Column(nullable = false, length = 1000)
    private String title; // 제목

    @Setter
    @Column(nullable = false , length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그

    @ToString.Exclude //article 과 articleComment 서로 양방향으로 보고있기때문에 순환 참조가 일어나서 메모리 과부하
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();



    //jpa entity 는 Hibernate 구현체를 사용하는 경우 기본 생성자를 가지고 있어야함
    //최소한 protected 로 열어 줘야함 private는 오류 발생
    protected Article() {
    }

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    //전체다 비교 할 필요 없고 유니크한 id값만 비교해서 중복 여부 확인
    //id가 부여되지 않았다(데이터베이스 연결 x) 하면 동등성 검사 자체가 의마가 없는 것으로 보고
    // 다 다른 것으로 간주 혹은 처리하지 않겠다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
