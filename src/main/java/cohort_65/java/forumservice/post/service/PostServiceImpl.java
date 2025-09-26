package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.dto.exception.PostNotFoundExeption;
import cohort_65.java.forumservice.post.model.Comment;
import cohort_65.java.forumservice.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
                orElseThrow(() -> new PostNotFoundExeption("Post with id" + id + " not found"));
        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public void addLike(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundExeption("Post with id" + id + " not found"));
        post.addLike();
        postRepository.save(post);
    }

    @Override
    public Iterable<PostDto> findPostsByAuthor(String author) {
        List<Post> posts = postRepository.findByAuthor(author);
        return posts.stream()
                .map(post -> modelMapper.map(post,PostDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDto addCommentToPost(String id, String user, NewCommentDto newCommentDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundExeption("Post with id " + id + " not found"));

        Comment newComment = modelMapper.map(newCommentDto, Comment.class);

        post.getComments().add(newComment);
        post = postRepository.save(post);

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public PostDto deletePostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundExeption("Post with id " + id + " not found"));
        postRepository.delete(post);

        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public Iterable<PostDto> findPostsByTags(Set<String> tags) {

        return StreamSupport
                .stream(postRepository.findAllByTagsIgnoreCaseIn(tags).spliterator(),
                        false)
                .map(post -> modelMapper.map(post, PostDto.class)).toList();
    }

    @Override
    public Iterable<PostDto> findPostsByPeriod(PostPeriodDto periodDto) {

        LocalDateTime start = LocalDate.parse(periodDto.getDateFrom()).atStartOfDay();
        LocalDateTime end = LocalDate.parse(periodDto.getDateTo()).atTime(23, 59, 59);

        List<Post> posts = postRepository.findByDateCreatedBetween(start, end);

        return modelMapper.map(posts, new org.modelmapper.TypeToken<List<PostDto>>(){}.getType());
    }

    @Override
    public PostDto updatePost(String id, PostDto postDto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundExeption("Post not found with id: " + id));

        modelMapper.map(postDto, post);
        Post updatedPost = postRepository.save(post);

        return modelMapper.map(updatedPost, PostDto.class);
    }
}
