from fastapi import APIRouter, Depends, FastAPI, HTTPException, Header, status
from sqlalchemy.orm import Session
import models, schemas
from util.oauth import get_current_user
from database import get_db

router = APIRouter(prefix="/comment", tags=["comment"])


@router.get('/{comment_id}', response_model=schemas.Comment)
async def get_comment(comment_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For retrieving a comment, need to pass comment_id as a path parameter"""
    comment = db.query(models.Comment).filter(models.Comment.id == comment_id).first()

    if not comment:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment with id of {comment_id} was not found!"
        )

    return comment


@router.post('/save', status_code=status.HTTP_201_CREATED)
async def comment_on_post(comment: schemas.CreateComment, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """
        Saves a comment, required fields are:

        - **comment_body**: Optional
        - **comment_file_type**: Optional
        - **comment_file**: Optional
        - **post_id**: required
    """
    post = db.query(models.Post).filter(models.Post.id == comment.post_id).first()

    if not post:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"post with id of {comment.post_id} was not found!"
        )

    post.no_of_comments = post.no_of_comments + 1
    new_comment = models.Comment(**comment.dict())
    db.add(new_comment)
    db.commit()
    db.refresh(new_comment)

    return new_comment


@router.put('/like/{comment_id}')
async def like_comment(comment_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For liking a comment, need to pass comment_id as a path parameter"""

    comment = db.query(models.Comment).filter(models.Comment.id == comment_id).first()

    if not comment:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment with id of {comment_id} was not found!"
        )

    like = db.query(models.CommentLikes).filter(models.CommentLikes.comment_id == comment_id,
                                                models.CommentLikes.user_id == user_id)

    if not like.first():
        comment.comment_likes = comment.comment_likes + 1
        new_like = models.CommentLikes(user_id=user_id, comment_id=comment_id)
        db.add(new_like)
        db.commit()
        db.refresh(new_like)

        return new_like

    comment.comment_likes = comment.comment_likes - 1
    like.delete(synchronize_session=False)
    db.commit()

    return {"message": "comment unliked"}


@router.delete('/delete/{comment_id}')
async def delete_comment(comment_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For deleting a comment, need to pass comment_id as a path parameter"""
    comment = db.query(models.Comment).filter(models.Comment.id == comment_id)

    comment_1 = comment.first()

    if not comment_1:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"comment with id of {comment_id} was not found!"
        )

    post = db.query(models.Post).filter(models.Post.id == comment_1.post_id).first()

    if not post:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"post with of {comment.post_id} was not found"
        )

    post.no_of_comments = post.no_of_comments - 1
    comment.delete(synchronize_session=False)
    db.commit()

    return {"message": "comment deleted"}