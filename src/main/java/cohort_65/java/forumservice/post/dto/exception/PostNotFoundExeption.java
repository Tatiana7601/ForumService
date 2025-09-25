package cohort_65.java.forumservice.post.dto.exception;

public class PostNotFoundExeption extends RuntimeException {
    public PostNotFoundExeption(String message) {
        super(message);
    }
}
