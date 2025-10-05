package cohort_65.java.forumservice.post.dto;

import jdk.jshell.Snippet;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    String id;
    String title;
    String content;
    String author;

    @Singular
    Set<String> tags;
    int likes;
    LocalDateTime dateCreated;
    @Singular
    List<CommentDto> comments;


}
