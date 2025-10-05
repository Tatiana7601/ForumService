package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.dto.exception.PostNotFoundExeption;
import cohort_65.java.forumservice.post.model.Comment;
import cohort_65.java.forumservice.post.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    PostRepository postRepository;

    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    PostServiceImpl postServiceImpl;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.typeMap(Comment.class, CommentDto.class).addMappings(mapper -> {
            mapper.map(Comment::getUser, CommentDto::setUser);
            mapper.map(Comment::getMessage, CommentDto::setMessage);
            mapper.map(Comment::getLikes, CommentDto::setLikes);
            mapper.map(Comment::getDateCreated, CommentDto::setDateCreated);
        });
        postServiceImpl = new PostServiceImpl(postRepository, modelMapper);
    }

    @Test
    void addNewPost_shouldReturnPostDto() {
        // 1. Підготовка вхідних даних
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Title");
        newPostDto.setContent("Content");
        newPostDto.setTags(Set.of("java", "spring"));

        // 2. Підготовка об'єкта, який поверне репозиторій
        Post savedPost = new Post("Title", "Content", "author", newPostDto.getTags());
        savedPost.setId("1");

        // 3. Мокаємо save() репозиторію
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // 4. Виклик тестованого методу
        PostDto result = postServiceImpl.addNewPost(newPostDto, "Author");

        // 5. Перевірка результату
        assertNotNull(result, "The result must not be null.");
        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertEquals("author", result.getAuthor());
        assertEquals(Set.of("java", "spring"), result.getTags());

        // 6. Перевірка, що репозиторій save() викликаний один раз
        verify(postRepository).save(any(Post.class));
    }



    @Test
    void getPostById_shouldReturnPostDto() {
        // 1. Створюємо один штучний пост
        Post post = new Post("Title", "Content", "author", Set.of("java", "spring"));
        post.setId("1");

        // 2. Мокаємо репозиторій: коли шукаємо за ID "1", повертаємо наш пост
        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        // 3. Викликаємо тестований метод
        PostDto result = postServiceImpl.getPostById("1");

        // 4. Перевіряємо результат
        assertNotNull(result, "The result must not be null.");
        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertEquals("author", result.getAuthor());
        assertEquals(Set.of("java", "spring"), result.getTags());

        // 5. Перевіряємо, що репозиторій викликаний один раз з правильним ID
        verify(postRepository).findById("1");
    }

    @Test
    void getPostById_shouldThrowExceptionIfPostNotFound() {
        // 1. Підготовка - репозиторій повертає пустий Optional
        String nonExistentId = "999";
        when(postRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // 2. Перевірка виключення
        PostNotFoundExeption exception = assertThrows(
                PostNotFoundExeption.class,
                () -> postServiceImpl.getPostById(nonExistentId)
        );

        // 3. Перевірка повідомлення виключення
        assertTrue(exception.getMessage().contains("Post with id"));
    }

    @Test
    void addLike_shouldIncreaseLikesAndSavePost() {
        Post post = new Post("Title", "Content", "author", Set.of("java", "spring"));
        post.setId("1");
        post.setLikes(0);

        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        postServiceImpl.addLike("1");
        assertEquals(1, post.getLikes(),"The number of likes should increase by 1");
        verify(postRepository).save(post);
        verify(postRepository).findById("1");
    }

    @Test
    void addLike_shouldThrowExceptionIfPostNotFound() {
        // 1. Мокаємо репозиторій: пост з таким ID не знайдено
        when(postRepository.findById("999")).thenReturn(Optional.empty());

        // 2. Перевіряємо, що метод кидає PostNotFoundExeption
        PostNotFoundExeption exception = assertThrows(
                PostNotFoundExeption.class,
                () -> postServiceImpl.addLike("999")
        );

        assertTrue(exception.getMessage().contains("Post with id999 not found"));

        // 3. Перевіряємо виклик findById
        verify(postRepository).findById("999");
    }


    @Test
    void findPostsByAuthor_shouldReturnPostsForGivenAuthor() {
        // 1. Підготовка тестових даних
        String author = "Author1";
        Post post1 = new Post("Title1", "Content1", author, Set.of("java"));
        post1.setId("1");
        Post post2 = new Post("Title2", "Content2", author, Set.of("spring"));
        post2.setId("2");

        // 2. Мокаємо виклик репозиторію
        when(postRepository.findByAuthor(author)).thenReturn(List.of(post1, post2));

        // 3. Викликаємо метод сервісу
        Iterable<PostDto> result = postServiceImpl.findPostsByAuthor(author);

        // 4. Перевірка результату
        List<PostDto> resultList = (List<PostDto>) result; // перетворюємо Iterable в List для зручності
        assertEquals(2, resultList.size());
        assertEquals("Title1", resultList.get(0).getTitle());
        assertEquals("Title2", resultList.get(1).getTitle());
        assertTrue(resultList.stream().allMatch(dto -> dto.getAuthor().equals(author)));

        // 5. Перевірка, що репозиторій викликаний один раз з правильним автором
        verify(postRepository, times(1)).findByAuthor(author);
    }

    @Test
    void addCommentToPost_shouldAddCommentAndReturnPostDto() {
        // 1. Підготовка тестових даних
        Post post = new Post("Title", "Content", "Author", Set.of("java"));
        post.setId("1");
        post.setComments(new ArrayList<>());

        // 2. Мокаємо репозиторій: знайти пост за id
        when(postRepository.findById("1")).thenReturn(Optional.of(post));
        // Мокаємо save: повертає той самий пост після додавання коментаря
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // 3. Створюємо новий коментар
        NewCommentDto newCommentDto = new NewCommentDto();
        newCommentDto.setMessage("Nice post!");


        // 4. Викликаємо метод сервісу
        PostDto updatedPostDto = postServiceImpl.addCommentToPost("1", "User1", newCommentDto);

        // 5. Перевірка результату
        assertNotNull(updatedPostDto);
        assertEquals(1, updatedPostDto.getComments().size());
        assertEquals("Nice post!", updatedPostDto.getComments().get(0).getMessage());



        // 5. Перевірка викликів репозиторію
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void deletePostById() {
        // 1. Підготовка посту
        Post post = new Post("Title", "Content", "Author", Set.of("java"));
        post.setId("1");

        // 2. Мок репозиторію
        when(postRepository.findById("1")).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(post);

        // 3. Виклик сервісного методу
        PostDto result = postServiceImpl.deletePostById("1");

        // 4. Перевірки
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Content", result.getContent());
        assertEquals("Author", result.getAuthor());

        // 5. Перевірка, що delete викликаний один раз
        verify(postRepository).delete(post);
    }

    @Test
    void findPostsByTags_shouldReturnPostsWithGivenTags() {
        // 1. Підготовка постів
        Post post1 = new Post("Title1", "Content1", "Author1", Set.of("java", "spring"));
        Post post2 = new Post("Title2", "Content2", "Author2", Set.of("java", "backend"));
        Post post3 = new Post("Title3", "Content3", "Author3", Set.of("java", "devops")); // новий пост

        // 2. Мок репозиторію
        when(postRepository.findAllByTagsIgnoreCaseIn(Set.of("java"))).thenReturn(List.of(post1, post2, post3));

        // 3. Виклик сервісного методу
        Iterable<PostDto> results = postServiceImpl.findPostsByTags(Set.of("java"));

        // 4. Перевірка результатів
        List<PostDto> resultList = StreamSupport.stream(results.spliterator(), false).toList();
        assertEquals(3, resultList.size()); // тепер очікуємо три пости
        assertTrue(resultList.stream().anyMatch(p -> p.getTitle().equals("Title1")));
        assertTrue(resultList.stream().anyMatch(p -> p.getTitle().equals("Title2")));
        assertTrue(resultList.stream().anyMatch(p -> p.getTitle().equals("Title3")));
    }

    @Test
        void findPostsByPeriod_shouldReturnPostsWithinPeriod () {
            // 1. Підготовка постів
            Post post1 = new Post("Title1", "Content1", "Author1", Set.of("java"));
            post1.setDateCreated(LocalDateTime.of(2025, 10, 1, 10, 0));
            Post post2 = new Post("Title2", "Content2", "Author2", Set.of("spring"));
            post2.setDateCreated(LocalDateTime.of(2025, 10, 2, 12, 0));

            // 2. Мок репозиторію
            when(postRepository.findByDateCreatedBetween(
                    LocalDate.of(2025, 10, 1).atStartOfDay(),
                    LocalDate.of(2025, 10, 3).atTime(23, 59, 59)))
                    .thenReturn(List.of(post1, post2));

            // 3. Підготовка DTO періоду
            PostPeriodDto periodDto = new PostPeriodDto();
            periodDto.setDateFrom("2025-10-01");
            periodDto.setDateTo("2025-10-03");

            // 4. Виклик сервісного методу
            Iterable<PostDto> results = postServiceImpl.findPostsByPeriod(periodDto);

            // 5. Перевірка результатів
            List<PostDto> resultList = StreamSupport.stream(results.spliterator(), false).toList();
            assertEquals(2, resultList.size());
            assertTrue(resultList.stream().anyMatch(p -> p.getTitle().equals("Title1")));
            assertTrue(resultList.stream().anyMatch(p -> p.getTitle().equals("Title2")));
        }


    @Test
    void updatePost_shouldReturnUpdatedPostDto() {
        // 1. Підготовка існуючого посту
        Post existingPost = new Post("OldTitle", "OldContent", "Author", Set.of("java"));
        existingPost.setId("1");

        // 2. Підготовка DTO для оновлення
        PostDto updateDto = new PostDto();
        updateDto.setTitle("NewTitle");
        updateDto.setContent("NewContent");
        updateDto.setAuthor("Author");
        updateDto.setTags(Set.of("spring"));

        // 3. Мок репозиторію
        when(postRepository.findById("1")).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);

        // 4. Виклик сервісного методу
        PostDto result = postServiceImpl.updatePost("1", updateDto);

        // 5. Перевірка результату
        assertNotNull(result);
        assertEquals("NewTitle", result.getTitle());
        assertEquals("NewContent", result.getContent());
        assertEquals("Author", result.getAuthor());
        assertEquals(Set.of("spring"), result.getTags());

        // 6. Перевірка виклику save
        verify(postRepository).save(existingPost);
    }
}