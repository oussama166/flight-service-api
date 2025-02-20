package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceRequest;
import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceResponse;
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
    UserPreferenceResponse getUserPreference(String username) throws Exception;


    /**
     * Set user preference
     * @param user
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference setUserPreference(User user, UserPreferenceRequest userPreference) ;

    /**
     * Set user preference
     * @param username
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference setUserPreference(String username, UserPreferenceRequest userPreference) ;


    /**
     * update user preference
     * @param user
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference updateUserPreference(User user, UserPreferenceRequest userPreference);

    /**
     * update user preference
     * @param username
     * @param userPreference
     *
     * @return userPreference
     * */
    UserPreference updateUserPreference(String username, UserPreferenceRequest userPreference);




}
