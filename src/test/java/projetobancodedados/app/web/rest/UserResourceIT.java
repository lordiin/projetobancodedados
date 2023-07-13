package projetobancodedados.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import projetobancodedados.app.IntegrationTest;
import projetobancodedados.app.domain.Authority;
import projetobancodedados.app.domain.User;
import projetobancodedados.app.repository.UserRepository;
import projetobancodedados.app.security.AuthoritiesConstants;
import projetobancodedados.app.service.dto.AdminUserDTO;
import projetobancodedados.app.service.mapper.UserMapper;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_nome = "john";
    private static final String UPDATED_nome = "jhipsternome";

    private static final String DEFAULT_sobrenome = "doe";
    private static final String UPDATED_sobrenome = "jhipstersobrenome";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setNome(DEFAULT_nome);
        user.setSobrenome(DEFAULT_sobrenome);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Setups the database with one user.
     */
    public static User initTestUser(UserRepository userRepository, EntityManager em) {
        userRepository.deleteAll();
        User user = createEntity(em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        return user;
    }

    @BeforeEach
    public void initTest() {
        user = initTestUser(userRepository, em);
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User
        AdminUserDTO user = new AdminUserDTO();
        user.setLogin(DEFAULT_LOGIN);
        user.setNome(DEFAULT_nome);
        user.setSobrenome(DEFAULT_sobrenome);
        user.setEmail(DEFAULT_EMAIL);
        user.setActivated(true);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isCreated());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
            User testUser = users.get(users.size() - 1);
            assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
            assertThat(testUser.getNome()).isEqualTo(DEFAULT_nome);
            assertThat(testUser.getSobrenome()).isEqualTo(DEFAULT_sobrenome);
            assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        });
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        AdminUserDTO user = new AdminUserDTO();
        user.setId(DEFAULT_ID);
        user.setLogin(DEFAULT_LOGIN);
        user.setNome(DEFAULT_nome);
        user.setSobrenome(DEFAULT_sobrenome);
        user.setEmail(DEFAULT_EMAIL);
        user.setActivated(true);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        AdminUserDTO user = new AdminUserDTO();
        user.setLogin(DEFAULT_LOGIN); // this login should already be used
        user.setNome(DEFAULT_nome);
        user.setSobrenome(DEFAULT_sobrenome);
        user.setEmail("anothermail@localhost");
        user.setActivated(true);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        AdminUserDTO user = new AdminUserDTO();
        user.setLogin("anotherlogin");
        user.setNome(DEFAULT_nome);
        user.setSobrenome(DEFAULT_sobrenome);
        user.setEmail(DEFAULT_EMAIL); // this email should already be used
        user.setActivated(true);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get all the users
        restUserMockMvc
            .perform(get("/api/admin/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_nome)))
            .andExpect(jsonPath("$.[*].sobrenome").value(hasItem(DEFAULT_sobrenome)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
            .perform(get("/api/admin/users/{login}", user.getLogin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_nome))
            .andExpect(jsonPath("$.sobrenome").value(DEFAULT_sobrenome))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGEURL))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/admin/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        AdminUserDTO user = new AdminUserDTO();
        user.setId(updatedUser.getId());
        user.setLogin(updatedUser.getLogin());
        user.setNome(UPDATED_nome);
        user.setSobrenome(UPDATED_sobrenome);
        user.setEmail(UPDATED_EMAIL);
        user.setActivated(updatedUser.isActivated());
        user.setImageUrl(UPDATED_IMAGEURL);
        user.setLangKey(UPDATED_LANGKEY);
        user.setCreatedBy(updatedUser.getCreatedBy());
        user.setCreatedDate(updatedUser.getCreatedDate());
        user.setLastModifiedBy(updatedUser.getLastModifiedBy());
        user.setLastModifiedDate(updatedUser.getLastModifiedDate());
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeUpdate);
            User testUser = users.stream().filter(usr -> usr.getId().equals(updatedUser.getId())).findFirst().get();
            assertThat(testUser.getNome()).isEqualTo(UPDATED_nome);
            assertThat(testUser.getSobrenome()).isEqualTo(UPDATED_sobrenome);
            assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
        });
    }

    @Test
    @Transactional
    void updateUserLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        AdminUserDTO user = new AdminUserDTO();
        user.setId(updatedUser.getId());
        user.setLogin(UPDATED_LOGIN);
        user.setNome(UPDATED_nome);
        user.setSobrenome(UPDATED_sobrenome);
        user.setEmail(UPDATED_EMAIL);
        user.setActivated(updatedUser.isActivated());
        user.setImageUrl(UPDATED_IMAGEURL);
        user.setLangKey(UPDATED_LANGKEY);
        user.setCreatedBy(updatedUser.getCreatedBy());
        user.setCreatedDate(updatedUser.getCreatedDate());
        user.setLastModifiedBy(updatedUser.getLastModifiedBy());
        user.setLastModifiedDate(updatedUser.getLastModifiedDate());
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeUpdate);
            User testUser = users.stream().filter(usr -> usr.getId().equals(updatedUser.getId())).findFirst().get();
            assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
            assertThat(testUser.getNome()).isEqualTo(UPDATED_nome);
            assertThat(testUser.getSobrenome()).isEqualTo(UPDATED_sobrenome);
            assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
        });
    }

    @Test
    @Transactional
    void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.randomAlphanumeric(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setNome("java");
        anotherUser.setSobrenome("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        AdminUserDTO user = new AdminUserDTO();
        user.setId(updatedUser.getId());
        user.setLogin(updatedUser.getLogin());
        user.setNome(updatedUser.getNome());
        user.setSobrenome(updatedUser.getSobrenome());
        user.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        user.setActivated(updatedUser.isActivated());
        user.setImageUrl(updatedUser.getImageUrl());
        user.setLangKey(updatedUser.getLangKey());
        user.setCreatedBy(updatedUser.getCreatedBy());
        user.setCreatedDate(updatedUser.getCreatedDate());
        user.setLastModifiedBy(updatedUser.getLastModifiedBy());
        user.setLastModifiedDate(updatedUser.getLastModifiedDate());
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateUserExistingLogin() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.randomAlphanumeric(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setNome("java");
        anotherUser.setSobrenome("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        userRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).get();

        AdminUserDTO user = new AdminUserDTO();
        user.setId(updatedUser.getId());
        user.setLogin("jhipster"); // this login should already be used by anotherUser
        user.setNome(updatedUser.getNome());
        user.setSobrenome(updatedUser.getSobrenome());
        user.setEmail(updatedUser.getEmail());
        user.setActivated(updatedUser.isActivated());
        user.setImageUrl(updatedUser.getImageUrl());
        user.setLangKey(updatedUser.getLangKey());
        user.setCreatedBy(updatedUser.getCreatedBy());
        user.setCreatedDate(updatedUser.getCreatedDate());
        user.setLastModifiedBy(updatedUser.getLastModifiedBy());
        user.setLastModifiedDate(updatedUser.getLastModifiedDate());
        user.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Delete the user
        restUserMockMvc
            .perform(delete("/api/admin/users/{login}", user.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId(DEFAULT_ID);
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void testUserDTOtoUser() {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setNome(DEFAULT_nome);
        userDTO.setSobrenome(DEFAULT_sobrenome);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getNome()).isEqualTo(DEFAULT_nome);
        assertThat(user.getSobrenome()).isEqualTo(DEFAULT_sobrenome);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.isActivated()).isTrue();
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedBy(DEFAULT_LOGIN);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(DEFAULT_LOGIN);
        user.setLastModifiedDate(Instant.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        AdminUserDTO userDTO = userMapper.userToAdminUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getNome()).isEqualTo(DEFAULT_nome);
        assertThat(userDTO.getSobrenome()).isEqualTo(DEFAULT_sobrenome);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isTrue();
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isNotEqualTo(null).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isZero();
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB).hasSameHashCodeAs(authorityB);
    }

    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(userRepository.findAll());
    }
}
