package projetobancodedados.app.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.config.Constants;
import projetobancodedados.app.domain.Authority;
import projetobancodedados.app.domain.User;
import projetobancodedados.app.repository.AuthorityRepository;
import projetobancodedados.app.repository.UserRepository;
import projetobancodedados.app.security.AuthoritiesConstants;
import projetobancodedados.app.security.SecurityUtils;
import projetobancodedados.app.service.dto.AdminUserDTO;
import projetobancodedados.app.service.dto.UserDTO;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setNome(userDTO.getNome());
        newUser.setSobrenome(userDTO.getSobrenome());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setNome(userDTO.getNome());
        user.setSobrenome(userDTO.getSobrenome());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setNome(userDTO.getNome());
                user.setSobrenome(userDTO.getSobrenome());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userRepository.save(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param nome first name of user.
     * @param sobrenome  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     */
    public void updateUser(String nome, String sobrenome, String email, String langKey) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setNome(nome);
                user.setSobrenome(sobrenome);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                userRepository.save(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }
}
