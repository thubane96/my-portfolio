package com.icode.workspace.netchatv2.repositories;

import com.icode.workspace.netchatv2.models.CommentLikedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikedByRepository extends JpaRepository<CommentLikedBy, Long> {
}
