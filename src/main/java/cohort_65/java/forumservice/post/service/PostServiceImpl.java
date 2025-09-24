package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.dto.CommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.dto.PostPeriodDto;
import cohort_65.java.forumservice.post.dto.exception.TaskNotFoundExeption;
import cohort_65.java.forumservice.post.model.Comment;
import cohort_65.java.forumservice.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    final PostRepository postRepository;
    final ModelMapper modelMapper;

    @Override
    public PostDto addNewPost(NewPostDto newPostDto, String author) {
        Post post = new Post(newPostDto.getTitle(),
                newPostDto.getContent(), author, newPostDto.getTags());
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto getPostById(String id) {
        Post post = postRepository.findById(id).
                orElseThrow(() -> new TaskNotFoundExeption("Post with id" + id + " not found"));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto addLike(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Post with id" + id + " not found"));
        post.addLike();
        postRepository.save(post);
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> findPostsByAuthor(String author) {
        List<Post> posts = postRepository.findByAuthor(author);
        return posts.stream()
                .map(post -> modelMapper.map(post,PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addCommentToPost(String id, CommentDto newCommentDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Post with id " + id + " not found"));


        Comment newComment = modelMapper.map(newCommentDto, Comment.class);

        post.getComments().add(newComment);
        postRepository.save(post);

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto deletePostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Post with id " + id + " not found"));
        postRepository.delete(post);

        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> findPostsByTags(PostDto postDto) {

        List<String> tags = new ArrayList<>(postDto.getTags());
        List<Post> posts = postRepository.findByTagsIn(tags);

        return modelMapper.map(posts, new org.modelmapper.TypeToken<List<PostDto>>(){}.getType());
    }

    @Override
    public List<PostDto> findPostsByPeriod(PostPeriodDto periodDto) {

        LocalDateTime start = LocalDate.parse(periodDto.getDateFrom()).atStartOfDay();
        LocalDateTime end = LocalDate.parse(periodDto.getDateTo()).atTime(23, 59, 59);

        List<Post> posts = postRepository.findByDateCreatedBetween(start, end);

        return modelMapper.map(posts, new org.modelmapper.TypeToken<List<PostDto>>(){}.getType());
    }

    @Override
    public PostDto updatePost(String id, PostDto postDto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundExeption("Post not found with id: " + id));

        modelMapper.map(postDto, post);
        Post updatedPost = postRepository.save(post);

        return modelMapper.map(updatedPost, PostDto.class);
    }
}
