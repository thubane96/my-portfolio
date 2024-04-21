package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Messages, Long> {
}
