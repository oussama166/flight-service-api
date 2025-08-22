package org.jetblue.jetblue.Mapper.UserPreference;

import lombok.Builder;
import org.jetblue.jetblue.Mapper.User.UserResponseBasic;
import org.jetblue.jetblue.Models.ENUM.Notification;

@Builder
public record UserPreferenceResponse(
        String seatPreference,
        String mealPreference,
        Notification notificationPreference,
        UserResponseBasic user
) {
}
