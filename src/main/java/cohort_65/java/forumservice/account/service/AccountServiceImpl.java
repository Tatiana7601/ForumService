package cohort_65.java.forumservice.account.service;

import cohort_65.java.forumservice.account.dao.AccountRepository;
import cohort_65.java.forumservice.account.dto.AccountDto;
import cohort_65.java.forumservice.account.dto.NewAccountDto;
import cohort_65.java.forumservice.account.dto.UpdateUserDto;
import cohort_65.java.forumservice.account.dto.exception.AccountExistsException;
import cohort_65.java.forumservice.account.dto.exception.RoleNotFoundExeption;
import cohort_65.java.forumservice.account.model.Account;
import cohort_65.java.forumservice.account.model.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import cohort_65.java.forumservice.account.dto.exception.AccountNotFoundException;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    final AccountRepository accountRepository;
    final ModelMapper modelMapper;

    @Override
    public AccountDto register(NewAccountDto newAccountDto) {
        if (accountRepository.existsByLogin(newAccountDto.getLogin())) {
            throw new AccountExistsException();
        }
        Account account = modelMapper.map(newAccountDto, Account.class);
        accountRepository.save(account);

        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public void removeUser(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("User with login: " + login + "not found "));
        accountRepository.delete(account);
    }

    @Override
    public AccountDto getUserByLogin(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("User with login '" + login + "' not found"));
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    public void removeRole(String login, String role) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException("User with login '" + login + "' not found"));
        try {
            if (!account.getRoles().remove(Role.valueOf(role.toUpperCase()))) {
                throw new RoleNotFoundExeption(
                        "User '" + login + "' does not have role '" + role + "'");
            }
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundExeption("Role '" + role + "' does not exist");
        }

        accountRepository.save(account);

    }

    @Override
    public AccountDto updateUser(String login, UpdateUserDto updateUserDto) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException(
                        "User with login '" + login + "' not found"));

        if (updateUserDto.getFirstName() != null) account.setFirstName(updateUserDto.getFirstName());
        if (updateUserDto.getLastName() != null) account.setLastName(updateUserDto.getLastName());


        accountRepository.save(account);

        return modelMapper.map(account, AccountDto.class);
    }
//todo переробити метод
    @Override
    public AccountDto addRole(String login, String role) {

        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new AccountNotFoundException(
                        "User with login '" + login + "' not found"));

        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RoleNotFoundExeption("Role '" + role + "' does not exist");
        }
        if (account.getRoles().contains(roleEnum)) {
            return modelMapper.map(account, AccountDto.class);
        }

        account.getRoles().add(roleEnum);
        accountRepository.save(account);

        return modelMapper.map(account, AccountDto.class);
    }


}
