from typing import List
from fastapi import APIRouter, Depends, FastAPI, HTTPException, status, Header
from sqlalchemy.orm import Session
import models, schemas
from util.oauth import get_current_user
from database import get_db

router = APIRouter(
    prefix="/post",
    tags=["post"]
)


@router.get('/all', response_model=List[schemas.Post])
async def get_posts(db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves all posts"""

    posts = db.query(models.Post).all()

    return posts


@router.get('/{post_id}', response_model=schemas.Post)
async def get_post(post_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves a specific post using a reqiured post id passed in the path parameter"""
    post = db.query(models.Post).filter(models.Post.id == post_id).first()

    if not post:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"post with id of {post_id} was not found!"
        )

    return post


@router.post('/save', status_code=status.HTTP_201_CREATED, response_model=schemas.Post)
async def save_post(post: schemas.CreatePost, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """
        Saves a post, required fields are:

        - **post_to_id**: Optional, if your posting on a friend's page
        - **posted_to_id**: Optional
        - **post_body**: Optional
        - **post_file_type**: Optional
        - **post_file**: Optional
    """
    if post.post_body is None and post.post_file is None:
        raise HTTPException(
            status_code=status.HTTP_406_NOT_ACCEPTABLE,
            detail="entitiy missing both post body & post file"
        )

    if post.posted_to_id is not None and post.posted_to_id == user_id:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="current user cannot post a post to themselves"
        )

    new_post = models.Post(posted_by_id=user_id, **post.dict())

    db.add(new_post)

    db.commit()
    db.refresh(new_post)

    return new_post


@router.put('/like/{post_id}')
async def like_post(post_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For liking a post, need to pass post_id as a path parameter"""
    post = db.query(models.Post).filter(models.Post.id == post_id).first()
    if not post:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"post with id of {post_id} was not found!"
        )

    like = db.query(models.PostLikes).filter(models.PostLikes.user_id == user_id, models.PostLikes.post_id == post_id)

    if not like.first():
        post.post_likes = post.post_likes + 1
        new_like = models.PostLikes(user_id=user_id, post_id=post_id)
        db.add(new_like)
        db.commit()
        db.refresh(new_like)
        return new_like

    post.post_likes = post.post_likes - 1
    like.delete(synchronize_session=False)
    db.commit()

    return {"message": "post unliked"}


@router.delete('/delete/{post_id}')
async def delete_post(post_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For deleting a post, need to pass post_id as a path parameter"""
    post = db.query(models.Post).filter(models.Post.id == post_id)

    post_1 = post.first()

    if not post_1:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"post with id of {post_id} was not found!"
        )

    if post_1.posted_by_id != user_id and post_1.posted_to_id != user_id:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="not authorized to delete the post"
        )

    post.delete(synchronize_session=False)
    db.commit()

    return {"message": "post deleted"}
