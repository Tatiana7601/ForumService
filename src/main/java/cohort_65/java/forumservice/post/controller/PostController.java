package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("post/{author}")
    public PostDto addNewPost(@RequestBody NewPostDto newPostDto,
                              @PathVariable String author ){
        return postService.addNewPost(newPostDto, author);
    }

    @GetMapping("/post/{id}")
    public PostDto findPostById(@PathVariable String id){
        return postService.getPostById(id);
    }

    @PutMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLikeToPost(@PathVariable String id){
         postService.addLike(id);
    }

    @GetMapping("/posts/author/{author}")
    public Iterable<PostDto> findPostByAuthor(@PathVariable String author){
        return postService.findPostsByAuthor(author);
    }

    @PutMapping("/post/{id}/comment/{user}")
    public PostDto addCommentToPost(@PathVariable String id,
                                    @PathVariable String user,
                                    @RequestBody NewCommentDto newCommentDto){
        return postService.addCommentToPost(id, user, newCommentDto);
    }

    @DeleteMapping("/post/{id}")
   public PostDto deletePostById(@PathVariable String id){
        return postService.deletePostById(id);
   }

   @PostMapping("/posts/tags")
   public Iterable<PostDto> findPostsByTags(@RequestBody Set<String> tags){
        return postService.findPostsByTags(tags);
   }

   @PostMapping("/posts/findByPeriod")
   public Iterable<PostDto> findPostsByPeriod(@RequestBody PostPeriodDto periodDto){
        return postService.findPostsByPeriod(periodDto);
   }

   @PutMapping("/post/{id}")
   public PostDto updatePost(@PathVariable String id, @RequestBody  PostDto postDto){
       return postService.updatePost(id, postDto);
   }

}
