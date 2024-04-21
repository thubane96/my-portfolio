from typing import List
from fastapi import Depends, HTTPException, Header, status, APIRouter
from sqlalchemy.orm import Session
from database import get_db
from util.oauth import get_current_user
import models, schemas

router = APIRouter(
    prefix="/friend",
    tags=["friend"]
)


@router.post('/invite/{friend_id}', status_code=status.HTTP_201_CREATED, response_model=schemas.FriendRequest)
async def send_a_friend_request(friend_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For sending a friend request, need to pass friend_id as a path parameter"""

    if user_id == friend_id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="user not allowed to send a friend request to themselves"
        )

    friend = db.query(models.User).filter(models.User.id == friend_id).first()

    if not friend:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {friend_id} was not found!"
        )

    friend_request = models.FriendRequest(user_id=user_id, friend_id=friend_id)
    db.add(friend_request)
    db.commit()
    db.refresh(friend_request)

    return friend_request


@router.get("/requests", response_model=List[schemas.FriendRequest])
async def get_friend_requests(db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves friend request sent to the current user"""
    all_requests = db.query(models.FriendRequest).filter(models.FriendRequest.friend_id == user_id).all()

    return all_requests


@router.put('/accept/{friend_id}')
async def accept_a_friend_request(friend_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For accepting a friend request, need to pass friend_id as a path parameter"""
    if user_id == friend_id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="user not allowed to accept a friend request from themselves"
        )

    friend_request = db.query(models.FriendRequest).filter(models.FriendRequest.user_id == friend_id,
                                                           models.FriendRequest.friend_id == user_id)

    if not friend_request.first():
        print('about to throw a 404 ')
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="friend request not found!"
        )

    friend_request.delete(synchronize_session=False)
    db.add(models.Friend(user_id=user_id, friend_id=friend_id))
    db.add(models.Friend(user_id=friend_id, friend_id=user_id))
    db.commit()

    return {"message": "Friend request accepted"}


@router.delete('/unfriend/{friend_id}')
async def unfriend_a_friend(friend_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For unfriending a friend, need to pass friend_id as a path parameter"""
    if user_id == friend_id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="user not allowed to unfriend themselves"
        )

    friend_1 = db.query(models.Friend).filter(models.Friend.user_id == user_id, models.Friend.friend_id == friend_id)
    friend_2 = db.query(models.Friend).filter(models.Friend.user_id == friend_id, models.Friend.friend_id == user_id)

    if not friend_1.first() and not friend_2.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="users are not friends"
        )

    friend_1.delete(synchronize_session=False)
    friend_2.delete(synchronize_session=False)
    db.commit()

    return {"messae": "unfriended successfully"}


@router.post('/block/{friend_id}', response_model=schemas.BlockList)
async def block_a_user(friend_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For blocking a friend, need to pass friend_id as a path parameter"""

    if user_id == friend_id:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN,
            detail="user not allowed to block themselves"
        )

    friend_1 = db.query(models.Friend).filter(models.Friend.user_id == user_id, models.Friend.friend_id == friend_id)
    friend_2 = db.query(models.Friend).filter(models.Friend.user_id == friend_id, models.Friend.friend_id == user_id)

    if not friend_1.first() and not friend_2.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="users are not friends"
        )

    friend_1.delete(synchronize_session=False)
    friend_2.delete(synchronize_session=False)

    block_data = models.BlockList(user_id=user_id, friend_id=friend_id)

    db.add(block_data)
    db.commit()
    db.refresh(block_data)

    return block_data


@router.get('/all', response_model=List[schemas.Friend])
async def get_friends(db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves all friends"""
    friends = db.query(models.Friend).filter(models.Friend.user_id == user_id).all()

    return friends