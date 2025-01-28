package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;

public interface UserService {
    /**
     * This function is for finding user by the username and return all the username info
     *
     * @param username
     * @return User user
     */
    UserBasicDTO findUserByUsername(String username) throws Exception;

    /**
     * This function is for register new user
     *
     * @param user
     */

    boolean createUser(User user);

    /**
     * This function is for updating user info with insert new info about user
     *
     * @param username
     * @param user
     * @return boolean
     */
    boolean updateUser(String username, User user);
    /**
     * This function is for updating the user password by sending the actual password and the new password
     * @param username -- Username store in database
     * @param OldPassword -- Old password of user
     * @param password -- New password of user
     * */

    boolean updateUserPassword(String username, String OldPassword, String password) throws Exception;

    /**
     * This function is to delete user
     *
     * @param username
     * @return boolean
     */

    boolean deleteUser(String username);


    /**
     * This function is set data from user
     *
     * @param user
     * @return UserDTO
     */

    UserBasicDTO getUserBasicDTO(User user);


}
