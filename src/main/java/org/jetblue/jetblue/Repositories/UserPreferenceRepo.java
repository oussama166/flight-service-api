package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DAO.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserPreferenceRepo extends JpaRepository<UserPreference, Long> {

    @Query(value = "SELECT usp FROM UserPreference usp  WHERE usp.user.username = ?1")
    Optional<UserPreference> findByUsername(String username);

    void deleteUserPreferenceByUser(User user);
}
