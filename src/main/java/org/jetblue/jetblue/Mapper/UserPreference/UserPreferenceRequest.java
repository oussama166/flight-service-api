package org.jetblue.jetblue.Mapper.UserPreference;

import org.jetblue.jetblue.Models.ENUM.Notification;

public record UserPreferenceRequest(
        String seatPreference,
        String mealPreference,
        Notification notificationPreference
) {
}
