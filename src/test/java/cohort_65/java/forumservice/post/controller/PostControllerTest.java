package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addNewPost() throws Exception {
        NewPostDto newPostDto = new NewPostDto("Title1", "Content1", Set.of("Java","Spring"));
        PostDto expected = new PostDto("1","Title1", "Content1","Author1", Set.of("Java","Spring"), 1, LocalDateTime.now(), List.of());

        when(postService.addNewPost(any(NewPostDto.class), eq("Author1"))). thenReturn(expected);
        mockMvc.perform(post("/forum/post/Author1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.author").value("Author1"))
                .andExpect(jsonPath("$.title").value("Title1"));
    }


    @Test
    void findPostById() throws Exception {
        PostDto expected = new PostDto("1","Title1", "Content1","Bob",
                Set.of("Java"), 0, LocalDateTime.now(), List.of());

        when(postService.getPostById("1")).thenReturn(expected);

        mockMvc.perform(get("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.author").value("Bob"))
                .andExpect(jsonPath("$.title").value("Title1"));
    }

    @Test
    void addLikeToPost() throws Exception {
        doNothing().when(postService).addLike("1");

        mockMvc.perform(put("/forum/post/1/like"))
                .andExpect(status().isNoContent());

        verify(postService).addLike("1");
    }

    @Test
    void findPostByAuthor() throws Exception {
        PostDto expected = new PostDto("1","Title1", "Content1","Alice",
                Set.of("Java"), 0, LocalDateTime.now(), List.of());

        when(postService.findPostsByAuthor("Alice")).thenReturn(List.of(expected));

        mockMvc.perform(get("/forum/posts/author/Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Alice"))
                .andExpect(jsonPath("$[0].title").value("Title1"));
    }

    @Test
    void addCommentToPost() throws Exception {
        NewCommentDto newComment = new NewCommentDto("Nice post!");
        CommentDto comment = new CommentDto("Bob", "Nice post!", 0, LocalDateTime.now());
        PostDto expected = new PostDto("1","Title1", "Content1","Alice",
                Set.of("Java"), 0, LocalDateTime.now(), List.of(comment));

        when(postService.addCommentToPost(eq("1"), eq("Bob"), any(NewCommentDto.class)))
                .thenReturn(expected);

        mockMvc.perform(put("/forum/post/1/comment/Bob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.author").value("Alice"))
                .andExpect(jsonPath("$.title").value("Title1"))
                .andExpect(jsonPath("$.comments[0].message").value("Nice post!"));
    }

    @Test
    void deletePostById() throws Exception {
        PostDto expected = new PostDto("1","Title1", "Content1","Alice",
                Set.of("Java"), 0, LocalDateTime.now(), List.of());

        when(postService.deletePostById("1")).thenReturn(expected);

        mockMvc.perform(delete("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.author").value("Alice"));
    }

    @Test
    void findPostsByTags() throws Exception {
        PostDto expected = new PostDto("1","Title1", "Content1","Alice",
                Set.of("Java"), 0, LocalDateTime.now(), List.of());

        when(postService.findPostsByTags(Set.of("Java"))).thenReturn(List.of(expected));

        mockMvc.perform(post("/forum/posts/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of("Java"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags[0]").value("Java"));
    }

    @Test
    void findPostsByPeriod() throws Exception {
        String from = LocalDateTime.now().minusDays(1).toLocalDate().toString(); // "2025-10-03"
        String to = LocalDateTime.now().toLocalDate().toString();
        PostPeriodDto periodDto = new PostPeriodDto(from, to);
        PostDto expected = new PostDto("1","Title1", "Content1","Alice",
                Set.of("Java"), 0, LocalDateTime.now(), List.of());

        when(postService.findPostsByPeriod(any(PostPeriodDto.class))).thenReturn(List.of(expected));

        mockMvc.perform(post("/forum/posts/findByPeriod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(periodDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].author").value("Alice"));
    }

    @Test
    void updatePost() throws Exception {
        CommentDto comment = CommentDto.builder()
                .user("Bob")
                .message("Nice post!")
                .likes(0)
                .dateCreated(LocalDateTime.now())
                .build();

        PostDto updated = new PostDto("1","UpdatedTitle", "UpdatedContent","Author1",
                Set.of("Spring"), 5, LocalDateTime.now(), List.of(comment));

        when(postService.updatePost(eq("1"), any(PostDto.class))).thenReturn(updated);

        mockMvc.perform(put("/forum/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("UpdatedTitle"))
                .andExpect(jsonPath("$.likes").value(5));
    }
}