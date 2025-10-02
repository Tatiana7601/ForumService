package cohort_65.java.forumservice.account.dto;

import cohort_65.java.forumservice.account.model.Role;
import lombok.*;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    String login;
    String firstName;
    String lastName;
    @Singular
    Set<Role> roles;

}
