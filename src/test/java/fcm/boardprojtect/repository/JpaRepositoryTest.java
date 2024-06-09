package fcm.boardprojtect.repository;

import fcm.boardprojtect.config.JpaConfig;
import fcm.boardprojtect.domain.Article;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;


@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(JpaRepositoryTest.class);
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("SELECT TEST")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        Assertions.assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("INSERT TEST")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        // Given
        long prevCount = articleRepository.count();

        // When
        articleRepository.save( Article.of("test1", "content1", "#spring"));
        long currentCount = articleRepository.count();

        // Then
        Assertions.assertThat(currentCount)
                .isEqualTo(prevCount + 1);
    }

    @DisplayName("UPDATE TEST")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();

        // When
        String updated_hashtag = "#SPRING_BOOT";
        article.setHashtag(updated_hashtag);

        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        Assertions.assertThat(savedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updated_hashtag);
    }


    @DisplayName("DELETE TEST")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        // Given
        Article findArticle = articleRepository.findById(1L).orElseThrow();
        long prevArticleCount = articleRepository.count();
        long prevArticleCommentCount = articleCommentRepository.count();

        int deletedSize = findArticle.getArticleComments().size();

        // When
        articleRepository.delete(findArticle);
        logger.info("******************************");
        long currentArticleCount = articleRepository.count();
        long currentArticleCommentCount = articleCommentRepository.count();

        // Then
        Assertions.assertThat(currentArticleCount)
                .isEqualTo(prevArticleCount - 1);

        Assertions.assertThat(currentArticleCommentCount)
                .isEqualTo(prevArticleCommentCount - deletedSize);
    }
}
