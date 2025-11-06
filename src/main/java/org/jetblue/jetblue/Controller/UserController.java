package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.jetblue.jetblue.Mapper.User.*;
import org.jetblue.jetblue.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {
  // Integration
  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * This endpoint is for creating the user inside the application
   *
   * @return ResponseEntity
   */
  @Operation(
    summary = "Create User",
    description = "Creates a new user in the application"
  )
  @PostMapping(
    value = "/user",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> createUser(@RequestBody UserRequest user) {
    try {
      boolean created = userService.createUser(
        UserMapper.toUser(user),
        user.passportNumber(),
        user.passportExpirationDate()
      );

      if (created) {
        // Return 201 Created and user details or a success message
        return ResponseEntity
          .status(HttpStatus.CREATED)
          .body("User created successfully");
      } else {
        // If the user couldn't be created due to some validation or business logic failure
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("User could not be created");
      }
    } catch (Exception ex) {
      // Return 500 Internal Server Error
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + ex.getMessage());
    }
  }

  /**
   * This endpoint is for creating the user inside the application
   *
   * @return ResponseEntity
   */
  @Operation(
    summary = "Update User Password",
    description = "Updates an existing user's password in the application"
  )
  @PostMapping(
    value = "/user/password",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> updateUserPassword(
    @RequestBody @Valid UserPasswordRequest user
  ) {
    try {
      boolean created = userService.updateUserPassword(
        user.userName(),
        user.oldPassword(),
        user.password()
      );

      if (created) {
        // Return 201 Created and user details or a success message
        return ResponseEntity
          .status(HttpStatus.CREATED)
          .body("User created successfully");
      } else {
        // If the user couldn't be created due to some validation or business logic failure
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("User could not be created");
      }
    } catch (Exception ex) {
      // Return 500 Internal Server Error
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + ex.getMessage());
    }
  }

  /**
   * This endpoint is for finding users by there username
   *
   * @return UserDto
   */
  @Operation(
    summary = "Get User by Username",
    description = "Retrieves user details by username"
  )
  @GetMapping(value = "/user/{username}", produces = "application/json")
  public ResponseEntity<?> getUser(@PathVariable String username) {
    try {
      UserResponseBasic userBasicDTO = userService.findUserBasicByUsername(
        username
      );
      return ResponseEntity.ok(userBasicDTO);
    } catch (Exception ex) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + ex.getMessage());
    }
  }

  @Operation(
    summary = "Update User",
    description = "Updates an existing user's details in the application"
  )
  @PatchMapping(
    value = "/user/update/{username}",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> updateUser(
    @PathVariable String username,
    @RequestBody @Valid UserUpdateRequest user
  ) {
    try {
      boolean updated = userService.updateUser(username, user);
      if (updated) {
        return ResponseEntity
          .status(HttpStatus.OK)
          .body("User updated successfully");
      } else {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("User could not be updated");
      }
    } catch (Exception ex) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An error occurred: " + ex.getMessage());
    }
  }

  @Operation(
    summary = "Delete User",
    description = "Deletes an existing user from the application"
  )
  @DeleteMapping(
    value = "/user/delete/{username}",
    consumes = "application/json"
  )
  public ResponseEntity<?> deleteUser(@PathVariable String username) {
    boolean deleted = userService.deleteUser(username);

    if (deleted) {
      return ResponseEntity
        .status(HttpStatus.OK)
        .body("User deleted successfully");
    }
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body("User could not be deleted");
  }
}
