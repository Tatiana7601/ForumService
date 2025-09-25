package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dto.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

public interface PostService {

    PostDto addNewPost(NewPostDto newPostDto, String author);

    PostDto getPostById(String id);

    void addLike( String id);

    Iterable<PostDto> findPostsByAuthor(String author);

    PostDto addCommentToPost(String id, String user, NewCommentDto newCommentDto);

    PostDto deletePostById(String id);

    Iterable<PostDto> findPostsByTags(Set<String> tags);

    Iterable<PostDto> findPostsByPeriod(PostPeriodDto periodDto);

    PostDto updatePost(String id, PostDto postDto);
}
