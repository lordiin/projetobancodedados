package projetobancodedados.app.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByMatricula(userDTO.getMatricula().toLowerCase())
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
        newUser.setMatricula(userDTO.getMatricula().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setNome(userDTO.getNome());
        newUser.setSobrenome(userDTO.getSobrenome());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
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
        user.setMatricula(userDTO.getMatricula().toLowerCase());
        user.setNome(userDTO.getNome());
        user.setSobrenome(userDTO.getSobrenome());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
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
        userRepository.save(
            user.getMatricula(),
            user.getImagem(),
            user.getImagemContentType(),
            user.getPassword(),
            user.getNome(),
            user.getSobrenome(),
            user.getEmail(),
            user.isActivated()
        );
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
                user.setMatricula(userDTO.getMatricula().toLowerCase());
                user.setNome(userDTO.getNome());
                user.setSobrenome(userDTO.getSobrenome());
                user.setImagem(userDTO.getImagem());
                user.setImagemContentType(userDTO.getImagemContentType());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setActivated(userDTO.isActivated());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                userRepository.save(
                    user.getId(),
                    user.getMatricula(),
                    user.getImagem(),
                    user.getImagemContentType(),
                    user.getPassword(),
                    user.getNome(),
                    user.getSobrenome(),
                    user.getEmail(),
                    user.isActivated()
                );
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByMatricula(login)
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
     */
    public void updateUser(String nome, String sobrenome, String email, byte[] imagem, String imagemContentType) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByMatricula)
            .ifPresent(user -> {
                user.setNome(nome);
                user.setSobrenome(sobrenome);
                user.setEmail(email);
                user.setImagem(imagem);
                user.setImagemContentType(imagemContentType);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                String nativeQuery =
                    "UPDATE usuario SET matricula = :matricula, imagem = :imagem, imagem_content_type = :imagemContentType, password_hash = :password, " +
                    "nome = :nome, sobrenome = :sobrenome, email = :email, activated = :activated WHERE id = :id";

                entityManager
                    .createNativeQuery(nativeQuery)
                    .setParameter("matricula", user.getMatricula())
                    .setParameter("imagem", user.getImagem())
                    .setParameter("imagemContentType", user.getImagemContentType())
                    .setParameter("password", user.getPassword())
                    .setParameter("nome", user.getNome())
                    .setParameter("sobrenome", user.getSobrenome())
                    .setParameter("email", user.getEmail())
                    .setParameter("activated", user.isActivated())
                    .setParameter("id", user.getId())
                    .executeUpdate();

                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByMatricula)
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
        return userRepository.findOneWithAuthoritiesByMatricula(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByMatricula);
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
