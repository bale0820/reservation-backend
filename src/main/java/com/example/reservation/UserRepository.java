package com.example.reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByemail(String email);

    Optional<User> findByUserId(String userId);


    @Modifying
    @Query("UPDATE User u SET u.phone = :phone WHERE u.email = :email")
    void updatePhoneByEmail(@Param("email") String email, @Param("phone") String phone);
}