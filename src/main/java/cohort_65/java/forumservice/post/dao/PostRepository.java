package cohort_65.java.forumservice.post.dao;

import cohort_65.java.forumservice.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findByAuthor(String author);

    List<Post> findByTagsIn(List<String> tags);

    List<Post> findByDateCreatedBetween(LocalDateTime start, LocalDateTime end);


    Iterable<Post> findAllByTagsIgnoreCaseIn(Set<String> tags);
}
