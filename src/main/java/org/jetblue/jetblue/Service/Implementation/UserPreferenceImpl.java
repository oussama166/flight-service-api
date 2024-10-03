package org.jetblue.jetblue.Service.Implementation;

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

    public UserPreferenceImpl(UserPreferenceRepo userPreference, UserRepo userRepo) {
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
    public UserPreference getUserPreference(String username) throws Exception {
        UserPreference userPreference = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreference == null) {
            throw new Exception("No preference for user !!!");
        }
        return userPreference;
    }

    @Override
    public UserPreference setUserPreference(User user, UserPreference userPreference) {
        UserPreference userPreferenceToSet = userPreferenceRepo.findByUsername(user.getUsername()).orElse(null);
        if (userPreferenceToSet == null) {
            UserPreference newUserPreference = UserPreference
                    .builder()
                    .mealPreference(userPreference.getMealPreference())
                    .seatPreference(userPreference.getSeatPreference())
                    .notificationPreference(userPreference.getNotificationPreference())
                    .build();
            newUserPreference.setUser(user);
            userPreferenceRepo.save(newUserPreference);
            return newUserPreference;
        }
        return userPreferenceToSet;

    }

    @Override
    public UserPreference setUserPreference(String username, UserPreference userPreference) {
        UserPreference userPreferenceToSet = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreferenceToSet == null) {
            // get the use credential
            User userInfo = userRepo.findByUsername(username).orElse(null);

            if(userInfo == null) {
                return null;
            }

            UserPreference newUserPreference = UserPreference
                    .builder()
                    .mealPreference(userPreference.getMealPreference())
                    .seatPreference(userPreference.getSeatPreference())
                    .notificationPreference(userPreference.getNotificationPreference())
                    .build();
            newUserPreference.setUser(userInfo);
            userPreferenceRepo.save(newUserPreference);
            return newUserPreference;
        }

        return userPreferenceToSet;

    }

    @Override
    public UserPreference updateUserPreference(User user, UserPreference userPreference) {
        UserPreference userPreferenceToUpdate = userPreferenceRepo.findByUsername(user.getUsername()).orElse(null);
        if (userPreferenceToUpdate == null) {
            return setUserPreference(user, userPreference);
        }

        return update(userPreference, userPreferenceToUpdate);

    }

    @Override
    public UserPreference updateUserPreference(String username, UserPreference userPreference) {
        UserPreference userPreferenceToUpdate = userPreferenceRepo.findByUsername(username).orElse(null);
        if (userPreferenceToUpdate == null) {
            return setUserPreference(username, userPreference);
        }
        return update(userPreference, userPreferenceToUpdate);


    }


    private UserPreference update(UserPreference userPreference, UserPreference userPreferenceToUpdate) {
        userPreferenceToUpdate.setMealPreference(userPreference.getMealPreference());
        userPreferenceToUpdate.setSeatPreference(userPreference.getSeatPreference());
        userPreferenceToUpdate.setNotificationPreference(userPreference.getNotificationPreference());
        userPreferenceRepo.save(userPreferenceToUpdate);
        return userPreferenceToUpdate;
    }
}
