package com.icode.twitterclonebackend.services;

import com.icode.twitterclonebackend.exception.BookmarkNotFoundException;
import com.icode.twitterclonebackend.exception.CommentNotFoundException;
import com.icode.twitterclonebackend.exception.TweetNotFoundException;
import com.icode.twitterclonebackend.exception.UserLikedNotFound;
import com.icode.twitterclonebackend.models.*;
import com.icode.twitterclonebackend.repositories.*;
import com.icode.twitterclonebackend.util.TweetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class TweetService {

    private TweetRepository tweetRepo;
    private TweetUtil tweetUtil;
    private TweetLikedByRepository tweetLikedByRepo;
    private CommentRepository commentRepo;
    private CommentLikedByRepository commentLikedByRepo;
    private CommentReplyRepository commentReplyRepo;
    private CommentReplyLikedByRepository commentReplyLikedByRepo;
    private NotInterestedTweetRepository notInterestedTweetRepo;
    private UserRepository userRepo;
    private NotificationRepository notificationRepo;

    private List<Tweet> tweets = new ArrayList<>();
    private List<Tweet> widgetTweets = new ArrayList<>();
    private List<Tweet> bookmarkedTweets = new ArrayList<>();

    @Autowired
    public TweetService(TweetRepository tweetRepo, TweetUtil tweetUtil, TweetLikedByRepository tweetLikedByRepo,
                        CommentRepository commentRepo, CommentLikedByRepository commentLikedByRepo,
                        CommentReplyRepository commentReplyRepo,
                        CommentReplyLikedByRepository commentReplyLikedByRepo,
                        NotInterestedTweetRepository notInterestedTweetRepo,
                        UserRepository userRepo, NotificationRepository notificationRepo){
        this.tweetRepo = tweetRepo;
        this.tweetUtil = tweetUtil;
        this.tweetLikedByRepo = tweetLikedByRepo;
        this.commentRepo = commentRepo;
        this.commentLikedByRepo = commentLikedByRepo;
        this.commentReplyRepo = commentReplyRepo;
        this.commentReplyLikedByRepo = commentReplyLikedByRepo;
        this.notInterestedTweetRepo = notInterestedTweetRepo;
        this.userRepo = userRepo;
        this.notificationRepo = notificationRepo;
    }

    public void saveTweet(String tweetBody, MultipartFile tweetImage, String username){

        Tweet newTweet = new Tweet();

        if (tweetImage != null){
            try {
                newTweet.setTweetImage(Base64.getEncoder().encodeToString(tweetImage.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tweetBody != null) {
            newTweet.setTweetBody(tweetBody);
        }

        if(tweetBody != null || tweetImage != null){
            newTweet.setLikes(0);
            newTweet.setTweetedBy(username);
            tweetRepo.save(newTweet);
        }
    }

    public List<Tweet> getWidgetTweets(String username){

        List<Tweet> tweetList = tweetRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        User user = userRepo.findByUsername(username);

        widgetTweets.clear();
        for(Tweet tweet: tweetList){
            boolean isMuteFound = user.getTweetMuteUserList().stream()
                    .anyMatch(mute -> tweet.getTweetedBy().equals(mute.getUsernameToMute()));
            boolean isInterested = user.getNotInterestedTweetList().stream()
                    .anyMatch(intrst -> tweet.getId().equals(intrst.getTweetId()));
            boolean isHidden = user.getHiddenTweetsList().stream()
                    .anyMatch(hiden -> tweet.getId().equals(hiden.getTweetId()));
            boolean isBlocked = user.getBlockedUsersList().stream()
                    .anyMatch(block -> tweet.getTweetedBy().equals(block.getUsername()));

            if (!isMuteFound && !isInterested && !isHidden && !isBlocked){
                tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
                tweetFunction(tweet, username);
                widgetTweets.add(tweet);
            }

        }

        return widgetTweets;
    }

    public List<Tweet> getAllTweets(String username){
        List<Tweet> tweetList = tweetRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

        for(Tweet tweet: tweetList){
            tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
            tweetFunction(tweet, username);
        }

        return tweetList;
    }

    public List<Tweet> fetchTweets(String username){
        User user = userRepo.findByUsername(username);
        List<Tweet> tweetList = tweetRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        tweets.clear();
        for (Tweet tweet: tweetList){
            if(tweet.getTweetedBy().equals(username)){
                tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
                tweetFunction(tweet, username);
                tweets.add(tweet);
            }else if (!user.getFollowList().isEmpty()){
                boolean isMuteFound = user.getTweetMuteUserList().stream()
                        .anyMatch(mute -> tweet.getTweetedBy().equals(mute.getUsernameToMute()));
                boolean isInterested = user.getNotInterestedTweetList().stream()
                        .anyMatch(intrst -> tweet.getId().equals(intrst.getTweetId()));
                boolean isHidden = user.getHiddenTweetsList().stream()
                        .anyMatch(hiden -> tweet.getId().equals(hiden.getTweetId()));
                boolean isBlocked = user.getBlockedUsersList().stream()
                        .anyMatch(block -> tweet.getTweetedBy().equals(block.getUsername()));

                if (!isMuteFound && !isInterested && !isHidden && !isBlocked)
                {
                    for (Follow follow: user.getFollowList()){
                        if (tweet.getTweetedBy().equals(follow.getUsername())){

                            tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
                            tweetFunction(tweet, username);
                            tweets.add(tweet);

                        }
                    }
                }

            }

        }
        return tweets;
    }

    public Tweet fetchTweet(Long tweetId, String username){
        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );
        tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
        tweetFunction(tweet, username);

        return tweet;
    }

    public List<Tweet> getBookmarks(String username){

        List<Tweet> tweets = tweetRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        User user = userRepo.findByUsername(username);
        bookmarkedTweets.clear();
        for (Tweet tweet: tweets){
            boolean isBookmarkFound = user.getBookmarkList().stream()
                    .anyMatch(mark -> tweet.getId().equals(mark.getTweetId()));
            if (isBookmarkFound){
                tweet.setTime(tweetUtil.tweetTime(tweet.getCreatedAt().toString()));
                tweetFunction(tweet, username);
                bookmarkedTweets.add(tweet);
            }
        }
        return bookmarkedTweets;
    }

    public void tweetFunction(Tweet tweet, String username){

        User user = userRepo.findByUsername(username);

        if (user.getPinnedTweet() != null){
            tweet.setTweetPinned(user.getPinnedTweet().getTweetId().equals(tweet.getId()));
        }else {
            tweet.setTweetPinned(false);
        }

        tweet.setUserMuted(user.getTweetMuteUserList().stream()
                .anyMatch(mute -> tweet.getTweetedBy().equals(mute.getUsernameToMute())));
        tweet.setUserBlocked(user.getBlockedUsersList().stream()
                .anyMatch(block -> tweet.getTweetedBy().equals(block.getUsername())));
        tweet.setTweetBookmarked(user.getBookmarkList().stream()
                .anyMatch(mark -> tweet.getId().equals(mark.getTweetId())));
        tweet.setUserFollowed(user.getFollowList().stream()
                .anyMatch(follow -> tweet.getTweetedBy().equals(follow.getUsername())));
        tweet.setTweetHasImage(tweet.getTweetImage() != null);
        tweet.setIsUserLiked(tweet.getTweetLikedBy().stream().anyMatch(likeBy -> username.equals(likeBy.getLikedBy())));
        tweet.setNumberOfComments(tweet.getComments().size());


        for (Comment comment: tweet.getComments()){

            comment.setCommentHasImage(comment.isCommentHasImage() == null);
            comment.setTime(tweetUtil.tweetTime(comment.getCreatedAt().toString()));
            commentReplyFunction(comment, username);
            comment.setIsUserLiked(comment.getCommentLikedByList().stream()
                    .anyMatch(commentLikedBy -> username.equals(commentLikedBy.getLikedBy())));
            comment.setUserFollowed(user.getFollowList().stream()
                    .anyMatch(follow -> comment.getCommentedBy().equals(follow.getUsername())));
            comment.setUserBlocked(user.getBlockedUsersList().stream()
                    .anyMatch(block -> comment.getCommentedBy().equals(block.getUsername())));
            comment.setUserMuted(user.getTweetMuteUserList().stream()
                    .anyMatch(mute -> comment.getCommentedBy().equals(mute.getUsernameToMute())));

            if (comment.isUserBlocked()){
                tweet.getComments().remove(comment);
            }
        }
    }

    public void likeTweet(Long tweetId, String username){

        User user = userRepo.findByUsername(username);
        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );
        TweetLikedBy tweetLikedBy = new TweetLikedBy(username, tweet);

        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setUsername(tweet.getTweetedBy());
        notifications.setForTweet(true);
        notifications.setForUser(false);
        notifications.setForComment(false);
        notifications.setForCommentReply(false);
        notifications.setTweetId(tweet.getId());
        notifications.setOpened(false);

        if (!tweet.getTweetLikedBy().isEmpty()){

            boolean isTweetLiked = tweet.getTweetLikedBy().stream()
                    .anyMatch(like -> username.equals(like.getLikedBy()));

            if (isTweetLiked){
                TweetLikedBy tweetLiked = tweet.getTweetLikedBy().stream()
                        .filter(like -> username.equals(like.getLikedBy()))
                        .findFirst()
                        .orElseThrow( ()-> new UserLikedNotFound("user has not liked the tweet"));
                tweet.setLikes(tweet.getLikes() - 1);
                tweet.getTweetLikedBy().remove(tweetLiked);
                tweetRepo.save(tweet);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" unliked your tweet");
                if (!username.equals(tweet.getTweetedBy())) notificationRepo.save(notifications);
            }else {
                tweet.setLikes(tweet.getLikes() + 1);
                tweet.getTweetLikedBy().add(tweetLikedBy);
                tweetRepo.save(tweet);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your tweet");
                if (!username.equals(tweet.getTweetedBy())) notificationRepo.save(notifications);
            }

        }else {
            tweet.setLikes(tweet.getLikes() + 1);
            tweet.getTweetLikedBy().add(tweetLikedBy);
            tweetRepo.save(tweet);
            notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your tweet");
            if (!username.equals(tweet.getTweetedBy())) notificationRepo.save(notifications);
        }
    }

    public void addComment(Long tweetId, String commentBody, MultipartFile commentImage, String username){

        User user = userRepo.findByUsername(username);
        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );

        Comment newComment = new Comment();

        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setUsername(tweet.getTweetedBy());
        notifications.setForComment(true);
        notifications.setForCommentReply(false);
        notifications.setForTweet(false);
        notifications.setForUser(false);
        notifications.setTweetId(tweet.getId());
        notifications.setOpened(false);
        notifications.setContent(user.getFirstName()+" "+user.getLastName()+" commented on your tweet");
        if (commentImage != null){
            try {
                newComment.setCommentImage(Base64.getEncoder().encodeToString(commentImage.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (commentBody != null){
            newComment.setCommentBody(commentBody);
        }

        if (commentImage != null || commentBody != null){
            newComment.setCommentLikes(0);
            newComment.setCommentedBy(username);
            tweet.addComment(newComment);
            commentRepo.save(newComment);
            if (!tweet.getTweetedBy().equals(username)) notificationRepo.save(notifications);
        }
    }

    public void likeComment(Long commentId, String username){

        User user = userRepo.findByUsername(username);
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException("comment with id of "+ commentId +" was not found!")
        );

        CommentLikedBy commentLikedBy = new CommentLikedBy(username, comment);

        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setTweetId(comment.getTweet().getId());
        notifications.setUsername(comment.getCommentedBy());
        notifications.setCommentId(commentId);
        notifications.setForComment(true);
        notifications.setForTweet(false);
        notifications.setForCommentReply(false);
        notifications.setForUser(false);
        notifications.setOpened(false);

        if (!comment.getCommentLikedByList().isEmpty()){

            boolean isLikeFound = comment.getCommentLikedByList().stream()
                    .anyMatch(like -> username.equals(like.getLikedBy()));

            if (isLikeFound){

                CommentLikedBy foundCommentLike = comment.getCommentLikedByList().stream()
                        .filter(like -> username.equals(like.getLikedBy()))
                        .findFirst()
                        .orElseThrow(() -> new UserLikedNotFound("user with username of "+username+" did not like the comment"));

                comment.setCommentLikes(comment.getCommentLikes() - 1);
                comment.getCommentLikedByList().remove(foundCommentLike);
                commentRepo.save(comment);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" unliked your comment");
                if (!username.equals(comment.getCommentedBy())) notificationRepo.save(notifications);
            }else {
                comment.setCommentLikes(comment.getCommentLikes() + 1);
                comment.getCommentLikedByList().add(commentLikedBy);
                commentRepo.save(comment);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your comment");
                if (!username.equals(comment.getCommentedBy())) notificationRepo.save(notifications);
            }

        }else {
            comment.setCommentLikes(comment.getCommentLikes() + 1);
            comment.getCommentLikedByList().add(commentLikedBy);
            commentRepo.save(comment);
            notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your comment");
            if (!username.equals(comment.getCommentedBy())) notificationRepo.save(notifications);
        }

    }

    public Comment getComment(Long commentId,String username){
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new TweetNotFoundException("comment reply with id of "+commentId+" was not found!")
        );
        comment.setTime(tweetUtil.tweetTime(comment.getCreatedAt().toString()));
        commentReplyFunction(comment, username);
        return comment;
    }

    public void commentReplyFunction(Comment comment, String username){

        comment.setIsUserLiked(comment.getCommentLikedByList().stream()
                .anyMatch(likeBy -> username.equals(likeBy.getLikedBy())));
        comment.setCommentHasImage(comment.getCommentImage() == null);
        comment.setNumberOfCommentReplies(comment.getCommentReplyList().size());
        User user = userRepo.findByUsername(username);

        for (CommentReply commentReply: comment.getCommentReplyList()){

            commentReply.setTime(tweetUtil.tweetTime(commentReply.getCreatedAt().toString()));
            commentReply.setIsUserLiked(commentReply.getCommentReplyLikedByList().stream()
                    .anyMatch(commentLikedBy -> username.equals(commentLikedBy.getLikedBy())));
            commentReply.setCommentReplyHasImage(commentReply.getCommentReplyImage() == null);
            commentReply.setUserFollowed(user.getFollowList().stream()
                    .anyMatch(follow -> commentReply.getCommentRepliedBy().equals(follow.getUsername())));
            commentReply.setUserBlocked(user.getBlockedUsersList().stream()
                    .anyMatch(block -> commentReply.getCommentRepliedBy().equals(block.getUsername())));
            commentReply.setUserMuted(user.getTweetMuteUserList().stream()
                    .anyMatch(mute -> commentReply.getCommentRepliedBy().equals(mute.getUsernameToMute())));

            if (commentReply.isUserBlocked()){
                comment.getCommentReplyList().remove(commentReply);
            }
        }
    }

    public void addCommentReply(Long commentId, String commentReplyBody,
                                MultipartFile commentReplyImage, String username){

        User user = userRepo.findByUsername(username);
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new TweetNotFoundException("Comment with id of "+commentId+" was not found!")
        );

        CommentReply newCommentReply = new CommentReply();

        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setTweetId(comment.getTweet().getId());
        notifications.setUsername(comment.getCommentedBy());
        notifications.setCommentId(commentId);
        notifications.setForComment(true);
        notifications.setForTweet(false);
        notifications.setForCommentReply(false);
        notifications.setForUser(false);
        notifications.setOpened(false);
        notifications.setContent(user.getFirstName()+" "+user.getLastName()+" commented on your comment");
        if (commentReplyImage != null){
            try {
                newCommentReply.setCommentReplyImage(Base64.getEncoder().encodeToString(commentReplyImage.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (commentReplyBody != null){
            newCommentReply.setCommentReplyBody(commentReplyBody);
        }

        if (commentReplyBody != null || commentReplyImage != null){
            newCommentReply.setCommentReplyLikes(0);
            newCommentReply.setCommentRepliedBy(username);
            comment.addCommentReply(newCommentReply);
            commentReplyRepo.save(newCommentReply);
            if (!username.equals(comment.getCommentedBy())) notificationRepo.save(notifications);
        }
    }

    public void likeCommentReply(Long commentReplyId, String username){

        User user = userRepo.findByUsername(username);
        CommentReply commentReply = commentReplyRepo.findById(commentReplyId).orElseThrow(
                () -> new CommentNotFoundException("comment reply with id of "+ commentReplyId +" was not found!")
        );

        CommentReplyLikedBy commentReplyLikedBy = new CommentReplyLikedBy(username, commentReply);

        Notifications notifications = new Notifications();
        notifications.setNotificationFrom(username);
        notifications.setTweetId(commentReply.getComment().getTweet().getId());
        notifications.setUsername(commentReply.getCommentRepliedBy());
        notifications.setCommentReplyId(commentReplyId);
        notifications.setCommentId(commentReply.getComment().getId());
        notifications.setForComment(true);
        notifications.setForTweet(false);
        notifications.setForCommentReply(false);
        notifications.setForUser(false);
        notifications.setOpened(false);

        if (!commentReply.getCommentReplyLikedByList().isEmpty()){

            boolean isCommentReplyLikeFound = commentReply.getCommentReplyLikedByList().stream()
                    .anyMatch(like -> username.equals(like.getLikedBy()));

            if (isCommentReplyLikeFound){

                CommentReplyLikedBy foundCommentReplyLike = commentReply.getCommentReplyLikedByList().stream()
                        .filter(like -> username.equals(like.getLikedBy()))
                        .findFirst()
                        .orElseThrow(() -> new CommentNotFoundException("comment reply like was not found"));

                commentReply.setCommentReplyLikes(commentReply.getCommentReplyLikes() - 1);
                commentReply.getCommentReplyLikedByList().remove(foundCommentReplyLike);
                commentReplyRepo.save(commentReply);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" unliked your comment");
                if (!username.equals(commentReply.getCommentRepliedBy())) notificationRepo.save(notifications);

            }else {
                commentReply.setCommentReplyLikes(commentReply.getCommentReplyLikes() + 1);
                commentReply.getCommentReplyLikedByList().add(commentReplyLikedBy);
                commentReplyRepo.save(commentReply);
                notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your comment");
                if (!username.equals(commentReply.getCommentRepliedBy())) notificationRepo.save(notifications);
            }
        }else {
            commentReply.setCommentReplyLikes(commentReply.getCommentReplyLikes() + 1);
            commentReply.getCommentReplyLikedByList().add(commentReplyLikedBy);
            commentReplyRepo.save(commentReply);
            notifications.setContent(user.getFirstName()+" "+user.getLastName()+" liked your comment");
            if (!username.equals(commentReply.getCommentRepliedBy())) notificationRepo.save(notifications);
        }
    }

    public void deleteTweet(Long tweetId, String username){

        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );

        if (tweet.getTweetedBy().equals(username)){
            tweetRepo.deleteById(tweetId);
        }
    }

    public void deleteComment(Long tweetId, Long commentId, String username){

        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );

        Comment comment = tweet.getComments()
                .stream()
                .filter(com -> (com.getId() == commentId))
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException("comment with id of "+ commentId +" was not found!"));

        if (comment.getCommentedBy().equals(username)){
            tweet.getComments().remove(comment);
            tweetRepo.save(tweet);
        }

    }

    public void deleteCommentReply(Long commentId, Long commentReplyId, String username){

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment with id of "+ commentId +" was not found!"));

        CommentReply commentReply = comment.getCommentReplyList()
                .stream()
                .filter(comReply -> (comReply.getId() == commentReplyId))
                .findFirst()
                .orElseThrow(
                () -> new CommentNotFoundException("comment reply with id of "+ commentReplyId +" was not found!"));

        if (commentReply.getCommentRepliedBy().equals(username)){
            comment.getCommentReplyList().remove(commentReply);
            commentRepo.save(comment);
        }

    }

    public void putTweetOnNotInterestedList(Long tweetId, String username){

        Tweet tweet = tweetRepo.findById(tweetId).orElseThrow(
                () -> new TweetNotFoundException("Tweet with id of "+tweetId+" was not found!")
        );

        if (!tweet.getTweetedBy().equals(username)) {
            User user = userRepo.findByUsername(username);
            NotInterestedTweet notInterestedTweet = new NotInterestedTweet(tweetId, tweet.getTweetedBy(), user);
            user.getNotInterestedTweetList().add(notInterestedTweet);
            userRepo.save(user);
        }

    }

    public void addPinnedTweet(Long tweetId, String username){
        User user = userRepo.findByUsername(username);
        PinnedTweet pinnedTweet = new PinnedTweet(tweetId, user);
        user.setPinnedTweet(pinnedTweet);
        userRepo.save(user);
    }

    public void addTweetToList(Long tweetId, String username){

        User user = userRepo.findByUsername(username);
        TweetList tweet = new TweetList(tweetId, user);

        if (!user.getTweetList().isEmpty()){

            boolean isTweetFound = user.getTweetList().stream()
                    .anyMatch(found -> tweetId.equals(found.getTweetId()));

            if (isTweetFound){
                TweetList tweetFound = user.getTweetList().stream()
                        .filter(found -> tweetId.equals(found.getTweetId()))
                        .findFirst()
                        .orElseThrow(
                                () -> new TweetNotFoundException("tweet was not found on the tweet list!")
                        );

                user.getTweetList().remove(tweetFound);
                userRepo.save(user);
            }else {
                user.getTweetList().add(tweet);
                userRepo.save(user);
            }
        }else{
            user.getTweetList().add(tweet);
            userRepo.save(user);
        }
    }

    public void bookmarkTweet(Long tweetId, String username){

        User user = userRepo.findByUsername(username);
        Bookmarks bookmark = new Bookmarks(tweetId, user);

        if (!user.getBookmarkList().isEmpty()){

            boolean isBookmarkFound = user.getBookmarkList().stream()
                    .anyMatch(mark -> tweetId.equals(mark.getTweetId()));

            if (isBookmarkFound){
                Bookmarks bookmarkFound = user.getBookmarkList().stream()
                        .filter(mark -> tweetId.equals(mark.getTweetId()))
                        .findFirst()
                        .orElseThrow(
                                () -> new BookmarkNotFoundException("bookmark was not found")
                        );

                user.getBookmarkList().remove(bookmarkFound);
            }else {
                user.getBookmarkList().add(bookmark);
            }
            userRepo.save(user);

        }else {
            user.getBookmarkList().add(bookmark);
            userRepo.save(user);
        }
    }

    public void hideTweet(Long tweetId, String username){

        User user = userRepo.findByUsername(username);
        HiddenTweets hiddenTweet = new HiddenTweets(tweetId, user);

        if (!user.getHiddenTweetsList().isEmpty()){

            boolean isHiddenTweetFound = user.getHiddenTweetsList().stream()
                    .anyMatch(hidden -> tweetId.equals(hidden.getTweetId()));

            if (isHiddenTweetFound){

                HiddenTweets hiddenTweetFound = user.getHiddenTweetsList().stream()
                        .filter(hidden -> tweetId.equals(hiddenTweet.getTweetId()))
                        .findFirst()
                        .orElseThrow( ()-> new TweetNotFoundException("tweet was not found on hidden list"));

                user.getHiddenTweetsList().remove(hiddenTweetFound);
                userRepo.save(user);

            }else {
                user.getHiddenTweetsList().add(hiddenTweet);
                userRepo.save(user);
            }
        }else {
            user.getHiddenTweetsList().add(hiddenTweet);
            userRepo.save(user);
        }

    }

    public void openNotification(Long notificationId){
        Notifications notification = notificationRepo.findById(notificationId).orElseThrow();
        notification.setOpened(true);
        notificationRepo.save(notification);
    }

    public List<Notifications> getNotifications(){
        List<Notifications> notifications = notificationRepo.findAll();
        for(Notifications notification: notifications){
            notification.setTime(notification.getCreatedAt().toString());
        }
        return notifications;
    }





















}
