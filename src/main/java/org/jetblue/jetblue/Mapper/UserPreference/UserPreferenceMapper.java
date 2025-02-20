package org.jetblue.jetblue.Mapper.UserPreference;

import lombok.Builder;
import org.jetblue.jetblue.Mapper.User.UserMapper;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceMapper {

    public static UserPreference toUserPreference(UserPreferenceRequest userPreference) {
        return UserPreference.builder()
                .seatPreference(userPreference.seatPreference())
                .mealPreference(userPreference.mealPreference())
                .notificationPreference(userPreference.notificationPreference())
                .build();
    }

    public static UserPreferenceResponse toUserPreferenceResponse(UserPreference userPreference) {
        return UserPreferenceResponse
                .builder()
                .seatPreference(userPreference.getSeatPreference())
                .mealPreference(userPreference.getMealPreference())
                .notificationPreference(userPreference.getNotificationPreference())
                .user(UserMapper.toUserResponseBasic(userPreference.getUser()))
                .build();
    }
}
