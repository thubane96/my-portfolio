from sqlalchemy.orm import Session, joinedload
from uuid import UUID
from datetime import datetime

from app.schemas.comment_reply import CommentReplyCreateIn
from app.models.models import CommentReply


def save_comment_reply(comment_reply, db: Session):
    db.add(comment_reply)
    db.commit()
    db.refresh(comment_reply)
    return comment_reply


def get_comment_reply(comment_reply_uuid: UUID, db: Session):
    return db.query(CommentReply).options(joinedload(CommentReply.image)).filter(CommentReply.comment_reply_uuid == comment_reply_uuid).first()


def get_comment_replies(comment_uuid: UUID, db: Session):
    return db.query(CommentReply).options(joinedload(CommentReply.image)).filter(CommentReply.comment_uuid == comment_uuid).all()


def delete_comment_reply(db_comment_reply, db: Session):
    db_comment_reply.deleted_at = datetime.now()
    db_comment_reply.image.deleted_at = datetime.now()
    db.commit()
    return db_comment_reply
