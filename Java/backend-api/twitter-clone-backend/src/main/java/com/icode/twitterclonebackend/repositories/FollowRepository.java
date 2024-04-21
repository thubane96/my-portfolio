package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
