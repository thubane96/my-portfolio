package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
}
