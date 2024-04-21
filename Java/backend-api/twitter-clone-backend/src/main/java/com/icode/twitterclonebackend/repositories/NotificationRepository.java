package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
}
