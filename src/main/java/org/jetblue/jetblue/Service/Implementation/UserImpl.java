package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserImpl implements UserService {

    // Inject the user repo
    private UserRepo userRepo;

    // Constructor
    public UserImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserBasicDTO findUserByUsername(String username) throws Exception {
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()) {
            return getUserBasicDTO(user.get());
        }
        throw new Exception("User not found");
    }

    @Override
    public boolean createUser(User user) {
        // try to find user
        User userOpt = userRepo.findByUsername(user.getUsername()).orElse(null);
        if(userOpt != null) {
            return false;
        }
        else {
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public boolean updateUser(User user) {
        User userOpt = userRepo.findByUsername(user.getUsername()).orElse(null);
        if(userOpt != null) {
            return false;
        }
        else {
            userRepo.save(user);
        }

        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public boolean connect(String username, String password) {
        return false;
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
