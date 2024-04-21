package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {
}
