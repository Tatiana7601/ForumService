package cohort_65.java.forumservice.account.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "account")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Account {
    @Id
    String login;
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    Set<Role> roles;

    public Account(String login, String password) {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public Account(String login, String password, String firstName,
                   String lastName) {

        this();
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }




    public boolean addRole(String role) {

        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(Role role) {
        return roles.remove(role);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }


}
