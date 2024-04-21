package com.icode.twitterclonebackend.repositories;

import com.icode.twitterclonebackend.models.TweetMuteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetMutedUserRepository extends JpaRepository<TweetMuteUser, Long> {
}
