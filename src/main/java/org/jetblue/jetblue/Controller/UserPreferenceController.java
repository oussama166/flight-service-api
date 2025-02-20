package org.jetblue.jetblue.Controller;


import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceRequest;
import org.jetblue.jetblue.Mapper.UserPreference.UserPreferenceResponse;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.jetblue.jetblue.Service.UserPreferenceService;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserPreferenceController {

    // Implement the  repo
    private final UserPreferenceService userPreferenceService;


    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    @GetMapping(
            value = "/userPreference/{username}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getUserPreference(
            @PathVariable(value = "username") String username
    ) {
        try {
            UserPreferenceResponse userPreference = userPreferenceService.getUserPreference(username);
            if (userPreference == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(userPreference);
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Server error occurred " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/setUserPreference/{username}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setUserPreference(
            @PathVariable(value = "username")
            @DefaultValue(value = "oussama166")
            String username,
            @RequestBody UserPreferenceRequest userPreference
    ) {
        UserPreference userPreferenceSaved = userPreferenceService.setUserPreference(username, userPreference);
        try {
            if (userPreferenceSaved == null) {
                return ResponseEntity.badRequest().body("User not found to set preference to him !!!");
            } else {
                return ResponseEntity.ok(userPreferenceSaved);
            }
        } catch (Exception e) {
            return ResponseEntity.ok("Server error occurred");
        }
    }

    @PatchMapping(
            value = "/updateUserPreference/{username}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateUserPreference(
            @PathVariable String username,
            @RequestBody UserPreferenceRequest userPreference
    ){
        try {
            UserPreference userPreferenceFeed = userPreferenceService.updateUserPreference(username, userPreference);
            return ResponseEntity.ok(userPreferenceFeed);
        }
        catch (Exception e) {
            return ResponseEntity.ok("Server error occurred " + e.getMessage());
        }
    }
}
