package cohort_65.java.forumservice.account.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "account")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Account {
    String id;
    String login;
    String password;
    @Setter
    String firstName;
    @Setter
    String lastName;
    @Setter
    Set<Role> roles = new HashSet<>();

    public Account(String login, String password, String firstName,
                   String lastName) {

        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = new HashSet<>();
    }


    public boolean addRole(Role role) {
        return roles.add(role);
    }

    public boolean removeRole(Role role) {
        return roles.remove(role);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }


}
