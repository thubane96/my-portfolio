package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.NotInterestedTweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotInterestedTweetRepository extends JpaRepository<NotInterestedTweet, Long> {
}
