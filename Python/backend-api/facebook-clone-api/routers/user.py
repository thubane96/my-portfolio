from typing import List
from fastapi import Depends, HTTPException, status, APIRouter, Header
from sqlalchemy.orm import Session
from database import get_db
from util.oauth import get_current_user
from util.password import verify_password, hash_password
import models, schemas

router = APIRouter(
    prefix="/user",
    tags=["user"]
)


# (db: Session = Depends(get_db), user_id: int = Depends(get_current_user))
@router.get('/all', response_model=List[schemas.User])
async def get_users(db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """Retrieves all users"""

    users = db.query(models.User).all()

    return users


@router.put('/cpass')
async def update_user_password(password_data: schemas.UpdateUserPassword, db: Session = Depends(get_db)
                               , user_id: int = Depends(get_current_user)):
    """
    Updates a user's password, required fields are:

    - **Old/Previous Password**
    - **New Password**
    """

    user = db.query(models.User).filter(models.User.id == user_id)

    if not user.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {user_id} was not found!"
        )

    if not verify_password(password_data.old_password, user.first().password):
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="invalid credentials"
        )

    user.first().password = hash_password(password_data.new_password)
    db.commit()

    return {"message": "credentials updated"}


@router.put('/upuser')
async def update_user_details(user_data: schemas.UserUpdateDetails, db: Session = Depends(get_db)
                              , user_id: int = Depends(get_current_user)):
    """
        Updates user details, required fields are:

        - **First Name**: Optional
        - **Last Name**: Optional
    """
    user = db.query(models.User).filter(models.User.id == user_id)

    if not user.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {user_id} was not found!"
        )

    user.first().first_name = user_data.first_name
    user.first().last_name = user_data.last_name
    db.commit()

    return {"message": "user details updated"}


@router.put('/updpicture')
async def update_profile_picture(picture_data: schemas.UpdateUserPicture, db: Session = Depends(get_db)
                                 , user_id: int = Depends(get_current_user)):
    """
    Updates User profile picture, requred field is:

    - **Picture**: in bytes(base64)
    """
    user = db.query(models.User).filter(models.User.id == user_id)

    if not user.first():
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {user_id} was not found!"
        )

    user.first().picture = picture_data.picture
    db.commit()

    return {"message": "profile picture updated"}


@router.get('/{id}', response_model=schemas.User)
async def get_user(id: int, db: Session = Depends(get_db), user_id: int = Depends(get_current_user)):
    """
    Retrieves a user using a user id passed in the path parameter
    """
    user = db.query(models.User).filter(models.User.id == id).first()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"user with id of {id} was not found!"
        )

    return user



