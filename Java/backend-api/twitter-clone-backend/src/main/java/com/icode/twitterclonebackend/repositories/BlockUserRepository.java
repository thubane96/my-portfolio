package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.BlockedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockedUsers, Long> {
}
