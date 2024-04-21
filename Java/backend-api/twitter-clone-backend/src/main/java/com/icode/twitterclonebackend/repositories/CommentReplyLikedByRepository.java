package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.CommentReplyLikedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReplyLikedByRepository extends JpaRepository<CommentReplyLikedBy, Long> {
}
