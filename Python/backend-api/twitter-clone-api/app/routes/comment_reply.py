from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from uuid import UUID
from typing import List

from app.db.database import get_db
from app.utils.auth_util import verify_access_token
from app.schemas.comment_reply import CommentReplyCreateIn
from app.services import comment_reply_service
from app.repositories import comment_reply_repository
from app.schemas.comment_reply import CommentReplyCreateIn, CommentReplyCreateOut

router = APIRouter()


@router.post('/', response_model=CommentReplyCreateOut)
def save_comment_reply(comment_reply: CommentReplyCreateIn, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> CommentReplyCreateOut:
    return comment_reply_service.save_comment_reply(comment_reply, user_uuid, db)


@router.post('/react/{comment_reply_uuid}')
def react(comment_reply_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> bool:
    return comment_reply_service.react(comment_reply_uuid, user_uuid, db)


@router.get('/{comment_reply_uuid}', response_model=CommentReplyCreateOut)
def get_comment_reply(comment_reply_uuid: UUID, db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> CommentReplyCreateOut:
    return comment_reply_service.get_comment_reply(comment_reply_uuid, db)


@router.get('/all/{comment_reply_uuid}', response_model=List[CommentReplyCreateOut])
def get_comment_replies(comment_reply_uuid: UUID, db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> List[CommentReplyCreateOut]:
    return comment_reply_repository.get_comment_replies(comment_reply_uuid, db)


@router.delete('/{comment_reply_uuid}', response_model=CommentReplyCreateOut)
def delete_comment_reply(comment_reply_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> CommentReplyCreateOut:
    return comment_reply_service.delete_comment_reply(comment_reply_uuid, user_uuid, db)
