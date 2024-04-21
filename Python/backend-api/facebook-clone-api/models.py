from sqlalchemy.sql.sqltypes import TIMESTAMP
from sqlalchemy.sql.expression import text
from sqlalchemy import Column, Integer, String, ForeignKey
from database import Base


class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    first_name = Column(String, nullable=False)
    last_name = Column(String, nullable=False)
    gender = Column(String, nullable=False)
    picture = Column(String)
    email = Column(String, nullable=False, unique=True)
    password = Column(String, nullable=False)
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class Post(Base):
    __tablename__ = "posts"

    id = Column(Integer, primary_key=True, nullable=False)
    posted_by_id = Column(Integer, nullable=False)
    posted_to_id = Column(Integer)
    post_body = Column(String)
    post_file_type = Column(String)
    post_file = Column(String)
    post_likes = Column(Integer, default=0)
    no_of_comments = Column(Integer, default=0)
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class PostLikes(Base):
    __tablename__ = "post_likes"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    post_id = Column(
        Integer,
        ForeignKey("posts.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class Comment(Base):
    __tablename__ = "comments"

    id = Column(Integer, primary_key=True, nullable=False)
    comment_body = Column(String)
    comment_file_type = Column(String)
    comment_file = Column(String)
    comment_likes = Column(Integer, default=0)
    no_of_replies = Column(Integer, default=0)
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )

    post_id = Column(Integer, ForeignKey("posts.id", ondelete="CASCADE"), nullable=False)


class CommentLikes(Base):
    __tablename__ = "comment_likes"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    comment_id = Column(
        Integer,
        ForeignKey("comments.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class CommentReply(Base):
    __tablename__ = "comment_replies"

    id = Column(Integer, primary_key=True, nullable=False)
    reply_body = Column(String)
    reply_file_type = Column(String)
    reply_file = Column(String)
    reply_likes = Column(Integer, default=0)
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )

    comment_id = Column(Integer, ForeignKey("comments.id", ondelete="CASCADE"), nullable=False)


class CommentReplyLikes(Base):
    __tablename__ = "comment_reply_likes"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    reply_id = Column(
        Integer,
        ForeignKey("comment_replies.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class FriendRequest(Base):
    __tablename__ = "friend_requests"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )
    friend_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class Friend(Base):
    __tablename__ = "friends"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )
    friend_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class BlockList(Base):
    __tablename__ = "block_list"

    user_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )

    friend_id = Column(
        Integer,
        ForeignKey("users.id", ondelete="CASCADE"),
        primary_key=True,
        nullable=False
    )
    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )


class Message(Base):
    __tablename__ = "messages"

    id = Column(Integer, primary_key=True, nullable=False)
    user_to_id = Column(Integer, nullable=False)
    user_from_id = Column(Integer, nullable=False)
    message = Column(String, nullable=False)
    message_file_type = Column(String)
    message_file = Column(String)

    created_at = Column(
        TIMESTAMP(timezone=True),
        nullable=False, server_default=text('now()')
    )