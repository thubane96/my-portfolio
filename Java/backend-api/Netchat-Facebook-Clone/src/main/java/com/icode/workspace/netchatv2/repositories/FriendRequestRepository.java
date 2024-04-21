package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.FriendRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequests, Long> {
    List<FriendRequests> findAllByUserFrom(String username);

    List<FriendRequests> findAllByUserTo(String username);

    FriendRequests findByUserTo(String username);
}
