from pydantic import BaseModel
from typing import Optional
from uuid import UUID
from datetime import datetime
from typing import List

from app.schemas.comment_image import CoreCommentImage
from app.schemas.comment_reply import CommentReplyCreateOut


class CommentCreateIn(BaseModel):
    tweet_uuid: UUID
    comment_body: Optional[str]
    comment_image: Optional[str]


class CommentCreateOut(BaseModel):
    comment_uuid: UUID
    tweet_uuid: UUID
    image_uuid: Optional[UUID]
    user_uuid: UUID
    comment_body: Optional[str]
    image: Optional[CoreCommentImage]
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True


class CoreComment(CommentCreateOut):
    comment_replies: Optional[CommentReplyCreateOut]
