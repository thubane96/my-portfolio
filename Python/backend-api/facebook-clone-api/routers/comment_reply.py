from fastapi import APIRouter, Depends, FastAPI, HTTPException, Header, status
import models, schemas
from database import get_db
from sqlalchemy.orm import Session
from util.oauth import get_current_user

router = APIRouter(prefix="/comment_reply", tags=["comment_reply"])


@router.post('/save', status_code=status.HTTP_201_CREATED, response_model=schemas.CommentReply)
async def reply_on_comment(comment_reply: schemas.CommentReplyCreate, db: Session = Depends(get_db)
                           , user_id: int = Depends(get_current_user)):
    """
        Saves a comment reply, required fields are:

        - **reply_body**: Optional
        - **reply_file_type**: Optional
        - **reply_file**: Optional
        - **comment_id**: required
    """
    comment = db.query(models.Comment).filter(models.Comment.id == comment_reply.comment_id).first()

    if not comment:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment with id of {comment_reply.comment_id} was not found!"
        )

    comment.no_of_replies = comment.no_of_replies + 1
    new_comment_reply = models.CommentReply(**comment_reply.dict())
    db.add(new_comment_reply)
    db.commit()
    db.refresh(new_comment_reply)

    return new_comment_reply


@router.put('/like/{comment_reply_id}')
async def like_comment_reply(comment_reply_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For liking a comment reply, need to pass comment_reply_id as a path parameter"""

    comment_reply = db.query(models.CommentReply).filter(models.CommentReply.id == comment_reply_id)
    comment_reply_1 = comment_reply.first()

    if not comment_reply_1:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment reply with id of {comment_reply_id} was not found!"
        )

    like = db.query(models.CommentReplyLikes).filter(models.CommentReplyLikes.user_id == user_id,
                                                     models.CommentReplyLikes.reply_id == comment_reply_id)

    if not like.first():
        comment_reply_1.reply_likes = comment_reply_1.reply_likes + 1
        new_like = models.CommentReplyLikes(user_id=user_id, reply_id=comment_reply_id)
        db.add(new_like)
        db.commit()

        return {"message": "comment reply liked successfully"}

    comment_reply_1.reply_likes = comment_reply_1.reply_likes - 1
    like.delete(synchronize_session=False)
    db.commit()

    return {"message": "comment reply unliked successfully"}


@router.delete('/delete/{comment_reply_id}')
async def delete_comment_reply(comment_reply_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For deleting a comment reply, need to pass comment_reply_id as a path parameter"""
    comment_reply = db.query(models.CommentReply).filter(models.CommentReply.id == comment_reply_id)
    comment_reply_1 = comment_reply.first()

    if not comment_reply_1:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment reply with id of {comment_reply_id} was not found!"
        )

    comment = db.query(models.Comment).filter(models.Comment.id == comment_reply_1.comment_id).first()

    comment.no_of_replies = comment.no_of_replies - 1

    comment_reply.delete(synchronize_session=False)
    db.commit()

    return {"message": "comment reply deleted successfully"}