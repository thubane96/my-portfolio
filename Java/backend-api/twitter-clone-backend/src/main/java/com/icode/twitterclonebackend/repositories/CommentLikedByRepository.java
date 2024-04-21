package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.CommentLikedBy;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.jpa.repository.JpaRepository;

@ReadingConverter
public interface CommentLikedByRepository extends JpaRepository<CommentLikedBy, Long> {
}
