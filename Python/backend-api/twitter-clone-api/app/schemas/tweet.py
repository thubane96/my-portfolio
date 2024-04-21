from pydantic import BaseModel
from typing import Optional
from uuid import UUID
from datetime import datetime
from typing import List

from app.schemas.tweet_image import CoreTweetImage
from app.schemas.comment import CommentCreateOut


class TweetCreateIn(BaseModel):
    tweet_body: Optional[str]
    tweet_image: Optional[str]


class TweetCreateOut(BaseModel):
    tweet_uuid: UUID
    image_uuid: Optional[UUID]
    user_uuid: UUID
    tweet_body: Optional[str]
    image: Optional[CoreTweetImage]
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True


class CoreTweet(TweetCreateOut):
    comments: Optional[List[CommentCreateOut]]
