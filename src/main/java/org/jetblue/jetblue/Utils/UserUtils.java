package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        String currentUsername;

        if (principal instanceof UserDetails) {
            currentUsername = ((UserDetails) principal).getUsername();
        } else {
            currentUsername = principal.toString();
        }
        return currentUsername;
    }

    public static void verifyUser(User user) {
        if (!user.isVerified()) {
            throw new IllegalArgumentException("User is not verified");
        }
    }

    public static boolean verifyUserDocuments(User user) {
        if (user.getDocuments().isEmpty()) {
            return false; // User has documents, so they are considered verified
        }

        for (Document userDocument : user.getDocuments()) {
            if (userDocument.getType().equals("Passport") || userDocument.getType().equals("Visa")) {
                if (userDocument.getPath() == null || userDocument.getPath().isEmpty()) {
                    return false; // DocumentRepo is not uploaded
                }
            }
        }
        return true;
    }
}
