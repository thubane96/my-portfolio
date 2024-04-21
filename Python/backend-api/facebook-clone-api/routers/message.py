from fastapi import Depends, HTTPException, APIRouter, Header, status
from sqlalchemy.orm import Session
from sqlalchemy import or_
from util.oauth import get_current_user
from database import get_db
from typing import List
import schemas, models

router = APIRouter(
    prefix="/message",
    tags=["message"]
)


@router.post('/send')
async def send_message(message_data: schemas.CreateMessage, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """
    For sending a message, required fields are:

    - **user_to_id**: required
    - **message**: required
    - **message_file_type**: Optional
    - **message_file**: Optional

    """
    if message_data.user_to_id == user_id:
        raise HTTPException(
            status_code=status.HTTP_406_NOT_ACCEPTABLE,
            detail="not allowed to send a message to yourself"
        )

    friend = db.query(models.User).filter(models.User.id == message_data.user_to_id).first()

    if not friend:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {message_data.user_to_id}"
        )

    are_user_friends = db.query(models.Friend).filter(models.Friend.friend_id == message_data.user_to_id,
                                                      models.Friend.user_id == user_id).first()

    if not are_user_friends:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="not allowed, users are not friends!"
        )

    message = models.Message(user_from_id=user_id, **message_data.dict())

    db.add(message)
    db.commit()
    db.refresh(message)

    return message


@router.delete('/delmessage/{message_id}')
async def delete_message(message_id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """For deleting a message, need to pass message_id as a path parameter"""
    message = db.query(models.Message).filter(models.Message.id == message_id)

    if not message.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"message with id of {message_id} was not found!"
        )

    message.delete(synchronize_session=False)
    db.commit()

    return {"message": "message deleted!"}


@router.get('/all', response_model=List[schemas.Message])
async def get_messages(db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves all messages"""
    messages = db.query(models.Message).filter(
        or_(models.Message.user_to_id == user_id, models.Message.user_from_id == user_id)).all()

    return messages