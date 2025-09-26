package cohort_65.java.forumservice.account.dto;

import cohort_65.java.forumservice.account.model.Role;
import lombok.Getter;

import java.util.Set;

@Getter
public class NewAccountDto {
    String   firstName;
    String  lastName;
    String  login;
    String  password;
    Set<Role> roles;
}
