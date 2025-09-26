package cohort_65.java.forumservice.account.dto;

import cohort_65.java.forumservice.account.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    String login;
    String firstName;
    String lastName;
    Set<Role> roles;

}
