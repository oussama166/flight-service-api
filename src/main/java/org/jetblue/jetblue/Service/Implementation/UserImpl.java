package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserImpl implements UserService {



    // Inject the user repo
    private final UserRepo userRepo;

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
    public boolean updateUser(String username,User user) {
        User userOpt = userRepo.findByUsername(username).orElse(null);
        if(userOpt == null) {
            return false;
        }
        else {
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
    public boolean deleteUser(String username) {
        User user = userRepo.findByUsername(username).orElse(null);
        if(user == null) {
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
