from sqlalchemy.orm import Session, joinedload
from uuid import UUID
from datetime import datetime

from app.models.models import Comment


def save_comment(comment, db: Session):
    db.add(comment)
    db.commit()
    db.refresh(comment)
    return comment


def get_comment(comment_uuid: UUID, db: Session):
    return db.query(Comment).options(joinedload(Comment.image)).filter(Comment.comment_uuid == comment_uuid).first()


def get_comments(tweet_uuid: UUID, db: Session):
    return db.query(Comment).options(joinedload(Comment.image)).filter(Comment.tweet_uuid == tweet_uuid).all()


def delete_comment(db_comment, db: Session):
    if db_comment:
        db_comment.deleted_at = datetime.now()
        db_comment.image.delete_at = datetime.now()

        for comment in db_comment.comment_replies:
            comment.deleted_at = datetime.now()
    db.commit()
    return db_comment
