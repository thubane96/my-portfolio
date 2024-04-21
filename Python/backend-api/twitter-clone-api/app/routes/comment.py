from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from uuid import UUID
from typing import List

from app.db.database import get_db
from app.schemas.comment import CommentCreateIn, CommentCreateOut, CoreComment
from app.utils.auth_util import verify_access_token
from app.services import comment_service
from app.repositories import comment_repository

router = APIRouter()


@router.post('/', response_model=CommentCreateOut)
def save_comment(comment: CommentCreateIn, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> CommentCreateOut:
    return comment_service.save_comment(comment, user_uuid, db)


@router.post('/react/{comment_uuid}')
def react(comment_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> bool:
    return comment_service.react(comment_uuid, user_uuid, db)


@router.get('/all/{tweet_uuid}', response_model=List[CoreComment])
def get_comments(tweet_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> List[CoreComment]:
    return comment_repository.get_comments(tweet_uuid, db)


@router.get('/{comment_uuid}', response_model=CoreComment)
def get_comment(comment_uuid: UUID, db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> CoreComment:
    return comment_service.get_comment(comment_uuid, db)


@router.delete('/{comment_uuid}', response_model=CoreComment)
def delete_comment(comment_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> CoreComment:
    return comment_service.delete_comment(comment_uuid, user_uuid, db)
