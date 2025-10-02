package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.NewPostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateNewPostWhenDataIsValid() throws Exception {
        NewPostDto newPostDto = new NewPostDto();
        newPostDto.setTitle("Мой первый пост");
        newPostDto.setContent("Контент моего поста");
        newPostDto.setTags(Set.of("java","spring"));

        String author = "author1";

        mockMvc.perform(post("/forum/post/{author}", author)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Мой первый пост"))
                .andExpect(jsonPath("$.content").value("Контент моего поста"))
                .andExpect(jsonPath("$.author").value(author))
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", containsInAnyOrder("java", "spring")));
    }

}
