from datetime import datetime
from pydantic import BaseModel, EmailStr
from typing import List, Optional


class TokenData(BaseModel):
    token: str
    token_type: str

    class Config:
        orm_mode = True


class UserBase(BaseModel):
    first_name: str
    last_name: str
    gender: str
    picture: Optional[str]
    email: EmailStr


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int
    created_at: datetime

    class Config():
        orm_mode = True


class UserTest(User):
    password: str

    class Config:
        orm_mode = True


class UserUpdateDetails(BaseModel):
    first_name: str
    last_name: str


class UpdateUserPicture(BaseModel):
    picture: str


class Signin(BaseModel):
    username: EmailStr
    password: str


class CommentReplyBase(BaseModel):
    reply_body: Optional[str]
    reply_file_type: Optional[str]
    reply_file: Optional[str]


class CommentReplyCreate(CommentReplyBase):
    comment_id: int


class CommentReply(CommentReplyCreate):
    reply_likes: int
    created_at: datetime

    class Config():
        orm_mode = True


class CommentBase(BaseModel):
    comment_body: Optional[str]
    comment_file_type: Optional[str]
    comment_file: Optional[str]


class CreateComment(CommentBase):
    post_id: int


class Comment(CreateComment):
    id: int
    comment_likes: int
    no_of_replies: int
    created_at: datetime

    class Config():
        orm_mode = True


class PostBase(BaseModel):
    posted_to_id: Optional[int]
    post_body: Optional[str]
    post_file_type: Optional[str]
    post_file: Optional[str]


class CreatePost(PostBase):
    pass


class Post(PostBase):
    id: int
    posted_by_id: int
    post_likes: int
    no_of_comments: int
    created_at: datetime

    class Config():
        orm_mode = True


class GetID(BaseModel):
    id: int


class PostLikes(BaseModel):
    user_id: int
    post_id: int
    created_at: datetime

    class Config():
        orm_mode = True


class CommentLikes(BaseModel):
    user_id: int
    comment_id: int
    created_at: datetime

    class Config():
        orm_mode = True


class CommentReplyLikes(BaseModel):
    user_id: int
    reply_id: int
    created_at: datetime

    class Config():
        orm_mode = True


class FriendRequestBase(BaseModel):
    user_id: int
    friend_id: int


class CreateFriendRequest(FriendRequestBase):
    pass


class FriendRequest(FriendRequestBase):
    created_at: datetime

    class Config():
        orm_mode = True


class FriendBase(BaseModel):
    user_id: int
    friend_id: int


class AddFriend(FriendBase):
    pass


class Friend(FriendBase):
    user_id: int
    friend_id: int
    created_at: datetime

    class Config():
        orm_mode = True


class BlockList(BaseModel):
    user_id: int
    friend_id: int
    created_at: datetime

    class Config():
        orm_mode = True


class UpdateUserPassword(BaseModel):
    old_password: str
    new_password: str


class MessageBase(BaseModel):
    user_to_id: int
    message: str
    message_file_type: Optional[str]
    message_file: Optional[str]


class CreateMessage(MessageBase):
    pass


class Message(MessageBase):
    id: int
    user_from_id: int
    created_at: datetime

    class Config():
        orm_mode = True