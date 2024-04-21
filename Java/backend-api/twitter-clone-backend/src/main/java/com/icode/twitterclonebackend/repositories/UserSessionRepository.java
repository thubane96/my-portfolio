package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
}
