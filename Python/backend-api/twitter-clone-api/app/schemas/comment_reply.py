from pydantic import BaseModel
from typing import Optional
from uuid import UUID
from datetime import datetime

from app.schemas.comment_reply_image import CoreCommentReplyImage


class CommentReplyCreateIn(BaseModel):
    tweet_uuid: UUID
    comment_reply_body: Optional[str]
    comment_reply_image: Optional[str]


class CommentReplyCreateOut(BaseModel):
    comment_reply_uuid: UUID
    comment_uuid: UUID
    image_uuid: Optional[UUID]
    user_uuid: UUID
    comment_reply_body: Optional[str]
    image: Optional[CoreCommentReplyImage]
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True
