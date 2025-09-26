package cohort_65.java.forumservice.account.service;

import cohort_65.java.forumservice.account.dto.AccountDto;
import cohort_65.java.forumservice.account.dto.NewAccountDto;
import cohort_65.java.forumservice.account.dto.UpdateUserDto;

public interface AccountService {

    AccountDto register (NewAccountDto newAccountDto);

    void removeUser(String login);

    AccountDto getUserByLogin(String login);

    void removeRole(String login, String role);

    AccountDto updateUser(String login, UpdateUserDto updateUserDto);

    AccountDto addRole(String login, String role);
}
