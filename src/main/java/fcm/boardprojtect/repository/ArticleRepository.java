package fcm.boardprojtect.repository;

import fcm.boardprojtect.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}