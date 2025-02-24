package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DAO.UserPrincipal;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomDetailsUserProvider implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User us = userService.findUserByUsername(username);
            return new UserPrincipal(us);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
