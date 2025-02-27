package org.jetblue.jetblue.Service.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.User.UserMapper;
import org.jetblue.jetblue.Mapper.User.UserRequest;
import org.jetblue.jetblue.Mapper.User.UserResponseBasic;
import org.jetblue.jetblue.Mapper.User.UserUpdateRequest;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.jetblue.jetblue.Repositories.UserPreferenceRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserImpl implements UserService {

    static int USER_CHALLENGE_PASSWORD = 3;

    // Inject the user repo
    private final UserRepo userRepo;
    private final UserPreferenceRepo userPreferenceRepo;


    @Override
    public UserResponseBasic findUserBasicByUsername(String username) throws Exception {
        if (username == null || username.isBlank()) return null;
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return UserMapper.toUserResponseBasic(user.get());
        }
        throw new Exception("User not found");
    }

    @Override
    public User findUserByUsername(String username) throws Exception {
        if (username == null || username.isBlank()) return null;
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new Exception("User not found");
    }

    @Override
    public boolean createUser(User user) {
        if (user == null) return false;
        if (user.getUsername().isBlank() || user.getEmail().isBlank()) return false;
        // try to find user
        User userOpt = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail()).orElse(null);
        if (userOpt != null) {
            return false;
        } else {
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public boolean updateUser(String username, UserUpdateRequest user) {
        if (username.isBlank() || user == null) return false;
        User userOpt = userRepo.findByUsername(username).orElse(null);
        if (userOpt == null) {
            return false;
        } else {
            userOpt.setName(user.name());
            userOpt.setLastName(user.lastName());
            userOpt.setMiddleName(user.middleName());
            userOpt.setGender(user.gender());
            userOpt.setUsername(user.username());
            userOpt.setAddress(user.address());
            userOpt.setBirthday(user.birthday());
            userOpt.setOrigin(user.origin());
            userOpt.setPhone(user.phone());
            userOpt.setEmail(user.email());
            userOpt.setFrequentFlyerNumber(String.valueOf(user.frequentFlyerNumber()));
            userRepo.save(userOpt);
            return true;
        }

    }

    @Override
    public boolean updateUserPassword(String username, String OldPassword, String password) throws Exception {

        User user = userRepo.findByUsername(username).orElse(null);
        var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // IN THIS STATE THE USER NEED TO BE NOTIFY
        if (user == null) {
            return false;
        }

        if (USER_CHALLENGE_PASSWORD == 0) {
            user.setVerified(false);
            userRepo.save(user);
            throw new Exception("User was been banned");
        }

        // check if the old password match
        if (!passwordEncoder.matches(OldPassword, user.getPassword())) {
            USER_CHALLENGE_PASSWORD -= 1;
            return false;
        } else {
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
            return true;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(String username) {
        User user = userRepo.findByUsername(username).orElse(null);

        if (user == null) {
            return false;
        } else {
            userPreferenceRepo.deleteUserPreferenceByUser(user);
            userRepo.delete(user);
            return true;
        }
    }


}
