package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
}
