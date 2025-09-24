package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dto.CommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.dto.PostPeriodDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PostService {

    PostDto addNewPost(NewPostDto newPostDto, String author);

    PostDto getPostById(String id);

    PostDto addLike(@PathVariable String id);

    List<PostDto> findPostsByAuthor(String author);

    PostDto addCommentToPost(String id, CommentDto newComment);

    PostDto deletePostById(String id);

    List<PostDto> findPostsByTags(PostDto postDto);

    List<PostDto> findPostsByPeriod(PostPeriodDto periodDto);

    PostDto updatePost(String id, PostDto postDto);
}
