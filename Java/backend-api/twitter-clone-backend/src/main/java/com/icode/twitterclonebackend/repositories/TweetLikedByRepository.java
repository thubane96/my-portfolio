package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.TweetLikedBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetLikedByRepository extends JpaRepository<TweetLikedBy, Long> {
}
