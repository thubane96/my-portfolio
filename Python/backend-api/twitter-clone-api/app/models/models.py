import uuid
from sqlalchemy import Column, String, Integer, Boolean, DateTime, func, ForeignKey
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship, backref

from app.db.database import Base


class User(Base):
    __tablename__ = "users"

    user_uuid = Column(UUID(as_uuid=True),
                       primary_key=True, default=uuid.uuid4)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    username = Column(String, nullable=False)
    password = Column(String, nullable=False)
    bio = Column(String)
    birth_date = Column(String)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    tweets = relationship("Tweet", back_populates="user")
    comments = relationship("Comment", back_populates="user")
    comment_replies = relationship("CommentReply", back_populates="user")
    messages = relationship("Message", back_populates="user")
    bookmarks = relationship("Bookmark", back_populates="user")
    hidden_tweets = relationship("HiddenTweet", back_populates="user")
    uninterested_tweets = relationship(
        "UninterestingTweet", back_populates="user")
    pinned_tweets = relationship("PinnedTweet", back_populates="user")
    muted_users = relationship("MutedUser", back_populates="user")
    blocked_uses = relationship("BlockedUser", back_populates="user")
    followers = relationship("Follower", back_populates="user")
    followings = relationship("Following", back_populates="user")


class UserImage(Base):
    __tablename__ = "user_images"
    user_image_uuid = Column(
        UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    image_path = Column(String, nullable=False)
    user_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "users.user_uuid"), nullable=True)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", backref=backref("user_images", uselist=False))


class Tweet(Base):
    __tablename__ = "tweets"

    tweet_uuid = Column(UUID(as_uuid=True),
                        primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    tweet_body = Column(String)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="tweets")
    comments = relationship("Comment")


class TweetImage(Base):
    __tablename__ = "tweet_images"
    tweet_image_uuid = Column(UUID(as_uuid=True),
                              primary_key=True, default=uuid.uuid4)
    tweet_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "tweets.tweet_uuid"), nullable=True)
    image_path = Column(String, nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    tweet = relationship("Tweet", backref=backref(
        "tweet_images", uselist=False))


class Comment(Base):
    __tablename__ = "comments"
    comment_uuid = Column(UUID(as_uuid=True),
                          primary_key=True, default=uuid.uuid4)
    tweet_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "tweets.tweet_uuid"), nullable=False)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    comment_body = Column(String)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="comments")
    comment_replies = relationship("CommentReply")
    comment_image = relationship(
        "CommentImage", back_populates="comment", uselist=False)


class CommentImage(Base):
    __tablename__ = "comment_images"
    comment_image_uuid = Column(UUID(as_uuid=True),
                                primary_key=True, default=uuid.uuid4)
    comment_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "comments.comment_uuid"), nullable=True)
    image_path = Column(String, nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    comment = relationship("Comment", backref=backref(
        "comment_images", uselist=False))


class CommentReply(Base):
    __tablename__ = "comment_replies"
    comment_reply_uuid = Column(UUID(as_uuid=True),
                                primary_key=True, default=uuid.uuid4)
    comment_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "comments.comment_uuid"), nullable=False)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    comment_reply_body = Column(String)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="comment_replies")


class CommentReplyImage(Base):
    __tablename__ = "comment_reply_images"
    comment_reply_image_uuid = Column(UUID(as_uuid=True),
                                      primary_key=True, default=uuid.uuid4)
    comment_reply_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "comment_replies.comment_reply_uuid"), nullable=True)
    image_path = Column(String, nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    comment_reply = relationship(
        "CommentReply", backref=backref("comment_reply_images", uselist=False))


class Reaction(Base):
    __tablename__ = "reactions"
    reaction_uuid = Column(UUID(as_uuid=True),
                           primary_key=True, default=uuid.uuid4)
    parent_uuid = Column(UUID(as_uuid=True), nullable=False)
    is_tweet = Column(Boolean, default=False, nullable=False)
    is_comment = Column(Boolean, default=False, nullable=False)
    is_tweet_comment_reply = Column(Boolean, default=False, nullable=False)
    user_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())


class Message(Base):
    __tablename__ = "messages"
    message_uuid = Column(UUID(as_uuid=True),
                          primary_key=True, default=uuid.uuid4)
    communicator_uuid = Column(UUID(as_uuid=True), nullable=False)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    message_body = Column(String)
    message_image = Column(String)
    message_opened = Column(Boolean, nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="messsages")


class MessageImage(Base):
    __tablename__ = "message_images"
    message_image_uuid = Column(UUID(as_uuid=True),
                                primary_key=True, default=uuid.uuid4)
    message_uuid = Column(UUID(as_uuid=True), ForeignKey(
        "messages.message_uuid"), nullable=True)
    image_path = Column(String, nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    message = relationship(
        "Message", backref=backref("message_images", uselist=False))


class BlockedUser(Base):
    __tablename__ = "blocked_users"
    blocked_user_uuid = Column(UUID(as_uuid=True),
                               primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    blocked_friend_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="blocked_users")


class Bookmark(Base):
    __tablename__ = "bookmarks"
    bookmark_uuid = Column(UUID(as_uuid=True),
                           primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    tweet_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="bookmarks")


class Follower(Base):
    __tablename__ = "follows"
    follow_uuid = Column(UUID(as_uuid=True),
                         primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    friend_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="followers")


class Following(Base):
    __tablename__ = "followings"
    following_uuid = Column(UUID(as_uuid=True),
                            primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    friend_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="followers")


class HiddenTweet(Base):
    __tablename__ = "hidden_tweets"
    hidden_tweet_uuid = Column(UUID(as_uuid=True),
                               primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    tweet_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="hidden_tweets")


class UninterestingTweet(Base):
    __tablename__ = "uninterested_tweets"
    uninterested_tweet_uuid = Column(UUID(as_uuid=True),
                                     primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    tweet_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="uninterested_tweets")


class Notification(Base):
    __tablename__ = "notifications"
    notification_uuid = Column(UUID(as_uuid=True),
                               primary_key=True, default=uuid.uuid4)
    notification_content = Column(String)
    parent_uuid = Column(UUID(as_uuid=True), nullable=False)
    is_tweet = Column(Boolean, default=False, nullable=False)
    is_comment = Column(Boolean, default=False, nullable=False)
    is_tweet_comment_reply = Column(Boolean, default=False, nullable=False)
    user_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)


class PinnedTweet(Base):
    __tablename__ = "pinned_tweets"
    pinned_tweet_uuid = Column(UUID(as_uuid=True),
                               primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    tweet_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", backref=backref(
        "pinned_tweets", uselist=False))


class MutedUser(Base):
    __tablename__ = "muted_users"
    muted_user_uuid = Column(UUID(as_uuid=True),
                             primary_key=True, default=uuid.uuid4)
    user_uuid = Column(
        UUID(as_uuid=True), ForeignKey("users.user_uuid"), index=True
    )
    muted_friend_uuid = Column(UUID(as_uuid=True), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    user = relationship("User", back_populates="muted_users")
