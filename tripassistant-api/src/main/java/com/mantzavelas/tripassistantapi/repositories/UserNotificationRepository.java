package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

}
