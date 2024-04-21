package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findAllByUserTo(String username);
}
