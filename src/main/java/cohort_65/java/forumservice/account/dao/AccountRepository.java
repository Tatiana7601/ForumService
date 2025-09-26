package cohort_65.java.forumservice.account.dao;

import cohort_65.java.forumservice.account.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account,String> {
    Optional<Account> findByLogin(String login);
}
