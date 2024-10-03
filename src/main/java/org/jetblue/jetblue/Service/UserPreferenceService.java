package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.springframework.stereotype.Service;


public interface UserPreferenceService {
    /**
     * Getting the preference of user
     * @param user
     * @return UserPreference
     * */
    UserPreference getUserPreference(User user) throws Exception;

    /**
     * Getting the preference of user
     * @param username
     * @return UserPreference
     * */
    UserPreference getUserPreference(String username) throws Exception;


    /**
     * Set user preference
     * @param user
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference setUserPreference(User user, UserPreference userPreference) ;

    /**
     * Set user preference
     * @param username
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference setUserPreference(String username, UserPreference userPreference) ;


    /**
     * update user preference
     * @param user
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference updateUserPreference(User user, UserPreference userPreference);

    /**
     * update user preference
     * @param username
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference updateUserPreference(String username, UserPreference userPreference);




}
