package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserImpl implements UserService {

    static int USER_CHALLENGE_PASSWORD = 3;

    // Inject the user repo
    private final UserRepo userRepo;

    // Constructor
    public UserImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserBasicDTO findUserByUsername(String username) throws Exception {
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isPresent()) {
            return getUserBasicDTO(user.get());
        }
        throw new Exception("User not found");
    }

    @Override
    public boolean createUser(User user) {
        // try to find user
        User userOpt = userRepo.findByUsername(user.getUsername()).orElse(null);
        if (userOpt != null) {
            return false;
        } else {

            userRepo.save(user);
            return true;
        }
    }

    @Override
    public boolean updateUser(String username, User user) {
        User userOpt = userRepo.findByUsername(username).orElse(null);
        if (userOpt == null) {
            return false;
        } else {
            userOpt.setUsername(user.getUsername());
            userOpt.setEmail(user.getEmail());
            userOpt.setAddress(user.getAddress());
            userOpt.setPhone(user.getPhone());
            userOpt.setBirthday(user.getBirthday());
            userOpt.setGender(user.getGender());
            userOpt.setFrequentFlyerNumber(String.valueOf(user.getFrequentFlyerNumber()));
            userOpt.setLastName(user.getLastName());
            userOpt.setName(user.getName());
            userOpt.setMiddleName(user.getMiddleName());
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
    public boolean deleteUser(String username) {
        User user = userRepo.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        } else {
            userRepo.delete(user);
            return true;
        }
    }


    @Override
    public UserBasicDTO getUserBasicDTO(User user) {
        return new UserBasicDTO(
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getOrigin(),
                user.getBirthday(),
                user.getGender(),
                user.isVerified()
        );
    }
}
