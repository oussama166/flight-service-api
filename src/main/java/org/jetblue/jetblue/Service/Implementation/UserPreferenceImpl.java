package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceMapper;
import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceRequest;
import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceResponse;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.jetblue.jetblue.Repositories.UserPreferenceRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.UserPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceImpl implements UserPreferenceService {

    Logger log = LoggerFactory.getLogger(UserPreferenceImpl.class);
    // Integration
    public final UserPreferenceRepo userPreferenceRepo;
    public final UserRepo userRepo;

    public UserPreferenceImpl(UserPreferenceRepo userPreference, UserRepo userRepo, UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceRepo = userPreference;
        this.userRepo = userRepo;
    }

    @Override
    public UserPreference getUserPreference(User user) throws Exception {
        UserPreference userPreference = userPreferenceRepo.findByUsername(user.getUsername()).orElse(null);
        if (userPreference == null) {
            throw new Exception("No preference for user !!!");
        }
        return userPreference;
    }

    @Override
    public UserPreferenceResponse getUserPreference(String username) throws Exception {
        UserPreference userPreference = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreference == null) {
            throw new Exception("No preference for user !!!");
        }
        return UserPreferenceMapper.toUserPreferenceResponse(userPreference);
    }

    @Override
    public UserPreference setUserPreference(User user, UserPreferenceRequest userPreference) {
        UserPreference userPreferenceToSet = userPreferenceRepo.findByUsername(user.getUsername()).orElse(null);
        if (userPreferenceToSet == null) {
            UserPreference newUserPreference = UserPreference
                    .builder()
                    .mealPreference(userPreference.mealPreference())
                    .seatPreference(userPreference.seatPreference())
                    .notificationPreference(userPreference.notificationPreference())
                    .build();
            newUserPreference.setUser(user);
            userPreferenceRepo.save(newUserPreference);
            return newUserPreference;
        }
        return userPreferenceToSet;

    }

    @Override
    public UserPreference setUserPreference(String username, UserPreferenceRequest userPreference) {
        UserPreference userPreferenceToSet = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreferenceToSet == null) {
            // get the use credential
            User userInfo = userRepo.findByUsername(username).orElse(null);

            if(userInfo == null) {
                return null;
            }

            UserPreference newUserPreference = UserPreference
                    .builder()
                    .mealPreference(userPreference.mealPreference())
                    .seatPreference(userPreference.seatPreference())
                    .notificationPreference(userPreference.notificationPreference())
                    .build();
            newUserPreference.setUser(userInfo);
            userPreferenceRepo.save(newUserPreference);
            return newUserPreference;
        }

        return userPreferenceToSet;

    }

    @Override
    public UserPreference updateUserPreference(User user, UserPreferenceRequest userPreference) {
        UserPreference userPreferenceToUpdate = userPreferenceRepo.findByUsername(user.getUsername()).orElse(null);
        if (userPreferenceToUpdate == null) {
            return setUserPreference(user, userPreference);
        }

        return update(UserPreferenceMapper.toUserPreference(userPreference), userPreferenceToUpdate);

    }

    @Override
    public UserPreference updateUserPreference(String username, UserPreferenceRequest userPreference) {
        UserPreference userPreferenceToUpdate = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreferenceToUpdate == null) {
            return setUserPreference(username, userPreference);
        }
        return update(UserPreferenceMapper.toUserPreference(userPreference), userPreferenceToUpdate);


    }


    private UserPreference update(UserPreference userPreference, UserPreference userPreferenceToUpdate) {
        userPreferenceToUpdate.setMealPreference(userPreference.getMealPreference());
        userPreferenceToUpdate.setSeatPreference(userPreference.getSeatPreference());
        userPreferenceToUpdate.setNotificationPreference(userPreference.getNotificationPreference());
        userPreferenceRepo.save(userPreferenceToUpdate);
        return userPreferenceToUpdate;
    }
}
