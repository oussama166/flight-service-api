package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.User.UserMapper;
import org.jetblue.jetblue.Mapper.User.UserResponseBasic;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.ENUM.Gender;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    UserRepo userRepo;

    @InjectMocks  // Injecting UserService implementation (UserImpl)
    UserImpl userImpl;  // This will automatically use the implementation of the interface

    User user;

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
                .build();
    }

    /**
     * Testing the `findUserBasicByUsername` method to return safe info to client
     *  - Test the case of all things work fine ✅
     *  - Test where the string is empty ✅
     *  - Test where the function throw exception ✅
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
    }

    @Test
    @DisplayName("Should return null when the username is empty")
    void testUserResponseBasicBlank() throws Exception {
        UserResponseBasic rs = userImpl.findUserBasicByUsername("");
        assertNull(rs);
    }

    @Test
    @DisplayName("Should raise error when the user is not found")
    void testUserResponseBasicNotFound() throws Exception {
        when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Exception ex = assertThrowsExactly(Exception.class, () -> userImpl.findUserBasicByUsername("oussama166"),"User not found");

        assertNotNull(ex);
        assertEquals("User not found", ex.getMessage());

    }
}
