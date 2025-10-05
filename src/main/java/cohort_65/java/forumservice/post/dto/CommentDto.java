package cohort_65.java.forumservice.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    String user;
    String message;
    Integer likes;
    LocalDateTime dateCreated = LocalDateTime.now();


}
