package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.CommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.dto.PostPeriodDto;
import cohort_65.java.forumservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PostDto addLikeToPost(@PathVariable String id){
        return postService.addLike(id);
    }

    @GetMapping("/posts/author/{author}")
    public List<PostDto> findPostByAuthor(@PathVariable String author){
        return postService.findPostsByAuthor(author);
    }

    @PutMapping("/post/{id}/comment")
    public PostDto addCommentToPost(@PathVariable String id,
                                    @RequestBody CommentDto commentDto){
        return postService.addCommentToPost(id,commentDto);
    }

    @DeleteMapping("/post/{id}")
   public PostDto deletePostById(@PathVariable String id){
        return postService.deletePostById(id);
   }

   @PostMapping("/posts/findByTags")
   public List<PostDto> findPostsByTags(@RequestBody PostDto postDto){
        return postService.findPostsByTags(postDto);
   }

   @PostMapping("/posts/findByPeriod")
   public List<PostDto> findPostsByPeriod(@RequestBody PostPeriodDto periodDto){
        return postService.findPostsByPeriod(periodDto);
   }

   @PutMapping("/post/{id}")
   public PostDto updatePost(@PathVariable String id, @RequestBody  PostDto postDto){
       return postService.updatePost(id, postDto);
   }

}
