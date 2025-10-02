package cohort_65.java.forumservice.account.controller;

import cohort_65.java.forumservice.account.dto.AccountDto;
import cohort_65.java.forumservice.account.dto.NewAccountDto;
import cohort_65.java.forumservice.account.dto.UpdateUserDto;
import cohort_65.java.forumservice.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public AccountDto register(@RequestBody NewAccountDto newAccountDto) {
        return accountService.register(newAccountDto);
    }


    @DeleteMapping("/user/{login}")
    public void removeUser(@PathVariable String login) {
        accountService.removeUser(login);
    }

    @GetMapping("/user/{login}")
    public AccountDto getUser(@PathVariable String login) {

        return accountService.getUserByLogin(login);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public void removeRole(@PathVariable String login, @PathVariable String role) {
        accountService.removeRole(login, role);
    }

    @PutMapping("/user/{login}")
    public AccountDto updateUser(@PathVariable String login,
                                 @RequestBody UpdateUserDto updateUserDto) {
    return accountService.updateUser(login,updateUserDto);
    }
        // todo зробити загальний метод для видалення/додавання ролі
    @PutMapping("/user/{login}/role/{role}")
    public AccountDto addRole(@PathVariable String login,@PathVariable String role) {
        return accountService.addRole(login,role);
    }

}
