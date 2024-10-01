package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;

public interface UserService {
    /**
     * This function is for finding user by the username and return all the username info
     *
     * @param username
     *
     * @return User user
     * */
    UserBasicDTO findUserByUsername(String username) throws Exception;

    /**
     *
     * This function is for register new user
     *
     * @param user
     *
     * */

    boolean createUser(User user);

    /**
     *
     * This function is for updating user info with insert new info about user
     *
     * @param user
     *
     * @return boolean
     * */
    boolean updateUser(User user);

    /**
    * This function is to delete user
    * @param username
     *
     * @return boolean
    * */

    boolean deleteUser(String username);



    /**
     * This function allow to user to connect with credential
     *
     * @param username
     * @param password
     *
     * @return boolean
     * */

    boolean connect(String username , String password);



    /**
     * This function is set data from user
     * @param user
     * @return UserDTO
     * */

    UserBasicDTO getUserBasicDTO(User user);



}
