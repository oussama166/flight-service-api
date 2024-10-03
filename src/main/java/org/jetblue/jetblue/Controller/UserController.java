package org.jetblue.jetblue.Controller;


import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.UserBasicDTO;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    // Integration
    private UserService userService;


    UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * This endpoint is for creating the user inside the application
     *
     *
     * @return ResponseEntity
     * */
    @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            boolean created = userService.createUser(user);

            if (created) {
                // Return 201 Created and user details or a success message
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            } else {
                // If the user couldn't be created due to some validation or business logic failure
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be created");
            }
        } catch (Exception ex) {
            // Return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    /**
     * This endpoint is for finding users by there username
     *
     * @return UserDto
     * */
    @GetMapping(value = "/user/{username}" , produces = "application/json")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        try{
            UserBasicDTO userBasicDTO = userService.findUserByUsername(username);
            return ResponseEntity.ok(userBasicDTO);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }

    @PatchMapping(value = "/user/update/{username}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> updateUser(@PathVariable String username,@RequestBody User user) {
        try {
            boolean updated = userService.updateUser(username,user);
            if (updated) {
                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be updated");
            }
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }




}
