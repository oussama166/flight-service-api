package org.jetblue.jetblue.Utils;

import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.Document;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.jetblue.jetblue.Utils.PathEncoded.DecodeFilePath;

@Slf4j
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

    public static void validateUser(String username) {
        String currentUsername = getCurrentUsername();
        if (!currentUsername.equals(username)) {
            throw new SecurityException("Username does not match the authenticated user");
        }
    }

    public static boolean verifyUserDocuments(Passenger passenger) {
        log.info("Verifying user documents");
        log.info("{} documents found for user: {}", passenger.getDocuments().size(), passenger.getUser().getUsername());

        if (passenger.getDocuments().isEmpty()) {
            return false; // User has documents, so they are considered verified
        }
        boolean PassportExist = false;
        boolean VisaExist = false;
        for (Document userDocument : passenger.getDocuments()) {
            if (userDocument.getType().getDisplayName().equals("Passport")) {
                System.out.println("Passport found: " + DecodeFilePath(userDocument.getPath()));
                PassportExist = true;
            }
            if (userDocument.getType().getDisplayName().equals("Visa")) {
                System.out.println("Visa found: " + DecodeFilePath(userDocument.getPath()));
                VisaExist = true;
            }
        }
        return PassportExist && VisaExist;
    }
}
