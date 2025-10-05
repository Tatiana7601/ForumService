package cohort_65.java.forumservice.post.repository;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@DataMongoTest
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    Post post1;
    Post post2;
    Post post3;
    Post post4;
    Post post5;

    @BeforeEach
    void SetUp() {
        postRepository.deleteAll();

        post1 = new Post();
        post1.setAuthor("Alice");
        post1.setTags(Set.of("java", "spring"));
        post1.setDateCreated(LocalDateTime.now().minusDays(1));

        post2 = new Post();
        post2.setAuthor("Bob");
        post2.setTags(Set.of("mongodb", "java"));
        post2.setDateCreated(LocalDateTime.now());

        post3 = new Post();
        post3.setAuthor("Charlie");
        post3.setTags(Set.of("python", "spring"));
        post3.setDateCreated(LocalDateTime.now().minusDays(3));

        post4 = new Post();
        post4.setAuthor("Alice");
        post4.setTags(Set.of("docker", "kubernetes"));
        post4.setDateCreated(LocalDateTime.now().minusDays(2));

        post5 = new Post();
        post5.setAuthor("Eve");
        post5.setTags(Set.of("java", "mongodb"));
        post5.setDateCreated(LocalDateTime.now().minusDays(5));
    }


    @Test
    void findByAuthor() {
        List<Post> result = postRepository.findByAuthor("Alice");
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Post::getAuthor).containsOnly("Alice");
    }

    @Test
    void findByTagsIn() {
        List<Post> result = postRepository.findByTagsIn(List.of("java"));
        assertThat(result).hasSize(3);
    }

    @Test
    void findByDateCreatedBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(3);
        LocalDateTime end = LocalDateTime.now();
        List<Post> result = postRepository.findByDateCreatedBetween(start, end);
        assertThat(result).hasSize(4);
    }

    @Test
    void findAllByTagsIgnoreCaseIn() {
        Iterable<Post> result = postRepository.findAllByTagsIgnoreCaseIn(Set.of("java"));
        assertThat(result).hasSize(3);
    }
    //===== NEGATIVE TESTS  =====

    @Test
    void findByAuthorNotFound() {
        List<Post> result = postRepository.findByAuthor("NonExistent");
        assertThat(result).isEmpty();
    }

    @Test
    void findByTagsInEmpty() {
        List<Post> result = postRepository.findByTagsIn(List.of("ruby"));
        assertThat(result).isEmpty();
    }

    @Test
    void findByDateCreatedBetweenNoMatches() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now().minusDays(6);
        List<Post> result = postRepository.findByDateCreatedBetween(start, end);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllByTagsIgnoreCaseInEmpty() {
        Iterable<Post> result = postRepository.findAllByTagsIgnoreCaseIn(Set.of("ruby"));
        assertThat(result).isEmpty();
    }

//==== TESTS WITH EMPTY KITS  =====

    @Test
    void findByTagsInWithEmptyList() {
        List<Post> result = postRepository.findByTagsIn(List.of());
        assertThat(result).isEmpty();
    }

    @Test
    void findAllByTagsIgnoreCaseInWithEmptySet() {
        Iterable<Post> result = postRepository.findAllByTagsIgnoreCaseIn(Set.of());
        assertThat(result).isEmpty();
    }
    // ===== ADVANCED COMBINED TESTS ======
    @Test
    void findByAuthorAndTag_Positive() {
        List<Post> result = postRepository.findByAuthor("Alice").stream()
                .filter(post -> post.getTags().contains("spring"))
                .toList();
        assertThat(result).hasSize(1); // post1
        assertThat(result.get(0).getAuthor()).isEqualTo("Alice");
        assertThat(result.get(0).getTags()).contains("spring");
    }

    @Test
    void findByAuthorAndTag_Negative() {
        List<Post> result = postRepository.findByAuthor("Alice").stream()
                .filter(post -> post.getTags().contains("python"))
                .toList();
        assertThat(result).isEmpty();
    }

    @Test
    void findByAuthorAndMultipleTags() {
        List<Post> result = postRepository.findByAuthor("Bob").stream()
                .filter(post -> post.getTags().contains("java") || post.getTags().contains("mongodb"))
                .toList();
        assertThat(result).hasSize(1); // post2
        assertThat(result.get(0).getAuthor()).isEqualTo("Bob");
    }

    @Test
    void findByAuthorAndTagAndDate() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(4);
        List<Post> result = postRepository.findByAuthor("Eve").stream()
                .filter(post -> post.getTags().contains("java"))
                .filter(post -> post.getDateCreated().isBefore(threshold))
                .toList();
        assertThat(result).hasSize(1); // post5
        assertThat(result.get(0).getAuthor()).isEqualTo("Eve");
    }

    @Test
    void findByAuthorAndTagIgnoreCase() {
        List<Post> result = postRepository.findByAuthor("Charlie").stream()
                .filter(post -> post.getTags().stream().anyMatch(tag -> tag.equalsIgnoreCase("SPRING")))
                .toList();
        assertThat(result).hasSize(1); // post3
        assertThat(result.get(0).getAuthor()).isEqualTo("Charlie");
    }
}
