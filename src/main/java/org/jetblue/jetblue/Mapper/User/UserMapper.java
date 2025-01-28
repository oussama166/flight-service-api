package org.jetblue.jetblue.Mapper.User;

import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserMapper {
    Logger logger = LoggerFactory.getLogger(UserMapper.class);

    public static User toUser(UserRequest userRequest) {
        var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return User.builder()
                .username(userRequest.username())
                .name(userRequest.name())
                .lastName(userRequest.lastName())
                .middleName(userRequest.middleName())
                .email(userRequest.email())
                .birthday(userRequest.birthday())
                .origin(userRequest.origin())
                .address(userRequest.address())
                .gender(userRequest.gender())
                .phone(userRequest.phone())
                .password(BCrypt.hashpw(userRequest.password(), BCrypt.gensalt()))
                .role(userRequest.role())
                .verified(userRequest.verified())
                .password(passwordEncoder.encode(userRequest.password()))
                .role(userRequest.role())
                .build();
    }
}
