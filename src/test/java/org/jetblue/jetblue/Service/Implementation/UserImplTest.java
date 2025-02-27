package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.User.UserMapper;
import org.jetblue.jetblue.Mapper.User.UserResponseBasic;
import org.jetblue.jetblue.Mapper.User.UserUpdateRequest;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.ENUM.Gender;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the <strong>UserImpl</strong> service class.<br>
 * <p>
 * This class contains tests for the following methods in the <strong>UserImpl</strong> class:<br>
 * - <strong>findUserBasicByUsername</strong> : Retrieves user information by userName and return <i>UserResponseBasic</i> <br/>
 * - <strong>findUserByUsername</strong> : Retrieves user information by userName and return <i>User</i> info without safety parsing </br>
 * - <strong>creatUser</strong> : Create new user and return true when is successes and false when failed the insertion <br/>
 * - <strong>updateUser</strong> : Update the information of existing user and return boolean
 */
@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    UserRepo userRepo;



    @InjectMocks
    UserImpl userImpl;

    User user;
    UserUpdateRequest userUpdateRequest;



    @BeforeEach
    void setup() {
        // Initialize user object here
        user = User
                .builder()
                .username("oussama166")
                .birthday(LocalDate.of(2002, 10, 6))
                .address("address")
                .name("name")
                .phone("phone")
                .origin("origin")
                .gender(Gender.MALE)
                .lastName("lastName")
                .middleName("middleName")
                .verified(true)
                .email("email@gmail.com")
                .password("{bcrypt}$2a$10$eG9JRFqLlBG8xoTRYIBq7O1aOH93uEA6ggBEzPfb4QLmIpLzFgPde")
                .build();
        userUpdateRequest = UserUpdateRequest
                .builder()
                .username("oussama166")
                .birthday(LocalDate.of(2002, 10, 6))
                .address("address")
                .name("name")
                .phone("phone")
                .origin("origin")
                .gender(Gender.MALE)
                .lastName("lastName")
                .middleName("middleName")
                .email("email@gmail.com")
                .build();

    }

    /// <=================================== Test Find userBasic Response by username ===================================> ///
    /**
     * Testing the `findUserBasicByUsername` method to return safe info to client
     * - Test the case of all things work fine ✅
     * - Test where the string is empty ✅
     * - Test where the function throw exception ✅
     */
    @Test
    @DisplayName("Should return userResponseBasic of user info")
    void testUserResponseBasic() throws Exception {

        // Expect the behavior of the findUsername FUNCTION
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        UserResponseBasic rs = userImpl.findUserBasicByUsername("oussama166");

        assertNotNull(rs);
        assertEquals(UserMapper.toUserResponseBasic(user), rs);
    }

    @Test
    @DisplayName("Should return null when the username is null")
    void testUserResponseBasicNull() throws Exception {
        UserResponseBasic rs = userImpl.findUserBasicByUsername(null);
        assertNull(rs);

        verify(userRepo, never()).findByUsername(any(String.class));
    }

    @Test
    @DisplayName("Should return null when the username is empty")
    void testUserResponseBasicBlank() throws Exception {
        UserResponseBasic rs = userImpl.findUserBasicByUsername("");
        assertNull(rs);

        verify(userRepo, never()).findByUsername(any(String.class));
    }

    @Test
    @DisplayName("Should raise error when the user is not found")
    void testUserResponseBasicNotFound() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(Exception.class, () -> userImpl.findUserBasicByUsername("oussama166"), "User not found");

        assertNotNull(ex);
        assertEquals("User not found", ex.getMessage());

        verify(userRepo, atLeast(1)).findByUsername(any(String.class));

    }

    /// <=================================== Test Find user by username ===================================> ///
    /**
     * Testing the helper function `findUserByUsername` to return the user info to system
     * - Test the case of all things work fine ✅
     * - Test where the string is empty ✅
     * - Test where the function throw exception ✅
     */
    @Test
    @DisplayName("Should return user when the username exist")
    void testUserExist() throws Exception {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        User rs = userImpl.findUserByUsername(user.getUsername());

        assertNotNull(rs);
        assertEquals(user, rs);

    }

    @Test
    @DisplayName("Should return null when the username is empty")
    void testUserExistEmpty() throws Exception {
        User rs = userImpl.findUserByUsername("");
        assertNull(rs);
        verify(userRepo, never()).findByUsername(any(String.class));

    }

    @Test
    @DisplayName("Should return null when the username is null")
    void testUserExistNull() throws Exception {
        User rs = userImpl.findUserByUsername(null);
        assertNull(rs);

        verify(userRepo, never()).findByUsername(any(String.class));
    }

    @Test
    @DisplayName("Should raise Exception when the user is not found")
    void testUserExistNotFound() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(Exception.class, () -> userImpl.findUserByUsername("oussama166"), "User not found");

        assertNotNull(ex);
        assertEquals("User not found", ex.getMessage());

        verify(userRepo, atLeast(1)).findByUsername(any(String.class));

    }

    /// <=================================== Test Create user function ===================================> ///
    /**
     * Testing the `createUser`
     * - Test the case of all things work fine ✅
     * - Test the case
     */
    @Test
    @DisplayName("Should return true when the user create successfully")
    void testUserCreateSuccessfully() {
        when(userRepo.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(userRepo.save(any(User.class))).thenReturn(user);

        boolean rs = userImpl.createUser(user);

        assertTrue(rs);

        verify(userRepo, atLeast(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepo, atLeast(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return false when the user object has issue 'Username' and 'Email' are blank's")
    void testUserCreateFailedWhenUsernameAndEmailBlank() {

        User user = User
                .builder()
                .username("")
                .birthday(LocalDate.of(2002, 10, 6))
                .address("address")
                .name("name")
                .phone("phone")
                .origin("origin")
                .gender(Gender.MALE)
                .lastName("lastName")
                .middleName("middleName")
                .verified(true)
                .email("")
                .build();

        boolean rs = userImpl.createUser(user);
        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should return false when the user object has issue 'Username' is blank")
    void testUserCreateFailedWhenUsernameBlank() {

        User user = User
                .builder()
                .username("")
                .birthday(LocalDate.of(2002, 10, 6))
                .address("address")
                .name("name")
                .phone("phone")
                .origin("origin")
                .gender(Gender.MALE)
                .lastName("lastName")
                .middleName("middleName")
                .verified(true)
                .email("email@test.ma")
                .build();

        boolean rs = userImpl.createUser(user);
        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should return false when the user object has issue 'Email' is blank")
    void testUserCreateFailedWhenEmailBlank() {

        User user = User
                .builder()
                .username("username123")
                .birthday(LocalDate.of(2002, 10, 6))
                .address("address")
                .name("name")
                .phone("phone")
                .origin("origin")
                .gender(Gender.MALE)
                .lastName("lastName")
                .middleName("middleName")
                .verified(true)
                .email("")
                .build();

        boolean rs = userImpl.createUser(user);
        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should return false when the user Object is null")
    void testUserCreateFailedWhenObjectIsNull() {
        boolean rs = userImpl.createUser(null);
        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should return false when the user already exist with same userName and email")
    void testUserCreateFailedWhenUsernameAndEmailAlreadyExist() {
        when(userRepo.findByUsernameOrEmail("oussama166", "email@gmail.com")).thenReturn(Optional.of(user));

        boolean rs = userImpl.createUser(user);

        assertFalse(rs);

        verify(userRepo, atLeast(1)).findByUsernameOrEmail("oussama166", "email@gmail.com");

    }

    /// <=================================== Test Update user function ===================================> ///
    /**
     * Testing the `updateUser`
     * - Test case when all things work fine ✅
     * - Test case when userName is blank ✅
     * - Test case when user object is null ✅
     * - Test case when user doesn't exist ✅
     */

    @Test
    @DisplayName("Should return true when the user information was successfully updated")
    void testUserUpdateSuccessfully() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        boolean rs = userImpl.updateUser("oussama166", userUpdateRequest);
        assertTrue(rs);

        verify(userRepo, atLeast(1)).findByUsername(any(String.class));
        verify(userRepo, atLeast(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should return false when the username is blank")
    void testUserUpdateFailedWhenUsernameIsBlank() {

        boolean rs = userImpl.updateUser("", userUpdateRequest);

        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should return false when the updateUser object is blank")
    void testUserUpdateFailedWhenObjectIsBlank() {

        boolean rs = userImpl.updateUser("username123", null);

        assertFalse(rs);

        verify(userRepo, never()).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should return false when the user doesn't exist")
    void testUserUpdateFailedWhenUserDoesNotExist() {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.ofNullable(null));

        boolean rs = userImpl.updateUser("username123", userUpdateRequest);
        assertFalse(rs);

        verify(userRepo, atLeast(1)).findByUsername(any(String.class));
        verify(userRepo, never()).save(any(User.class));
    }

    /// <=================================== Test Update user password ===================================> ///
    /**
     * Testing the `updateUserPassword`
     * -  Test case when all things work fine✅
     */
    @Test
    @DisplayName("Should return true when the user password is correctly changed")
    void shouldReturnTrueWhenUserPasswordIsCorrectlyChanged() throws Exception {
        // Arrange
        String OldPassword = "Ronaldo7";
        String OldHashedPassword = "{bcrypt}$2a$10$ittW08xIh7l.gD6upvtScO0FgjzKUWFfEKwm8gdj8laSSGtXWjRvK";
        String NewPassword = "password456";
        String newHashedPassword = "{bcrypt}$2a$10$eG9JRFqLlBG8xoTRYIBq7O1aOH93uEA6ggBEzPfb4QLmIpLzFgPde";

        var userEncoderMock = mock(BCryptPasswordEncoder.class);

        // Mock behavior
        when(userRepo.findByUsername("oussama166")).thenReturn(Optional.of(user)); // Return user
        when(userEncoderMock.matches(OldHashedPassword, user.getPassword())).thenReturn(true); // Simulate match
//        when(userEncoderMock.encode(NewPassword)).thenReturn(newHashedPassword); // Mock encoding

        // Ensure USER_CHALLENGE_PASSWORD is set to a valid value
        UserImpl.USER_CHALLENGE_PASSWORD = 1;

        // Act
        boolean result = userImpl.updateUserPassword("oussama166", OldPassword, NewPassword);

        // Assert
        assertTrue(result);
        verify(userRepo, times(1)).findByUsername("oussama166");
        verify(userEncoderMock, times(1)).matches(OldPassword, OldHashedPassword);
        verify(userEncoderMock, times(1)).encode(NewPassword);
        verify(userRepo, times(1)).save(user);

    }
}
