from sqlalchemy.orm import Session
from fastapi import HTTPException, status
from uuid import UUID
from typing import List


from app.repositories import user_repository
from app.utils.auth_util import hash_password
from app.schemas.user import UserCreateIn, UserCreateOut


def create(user: UserCreateIn, db: Session) -> UserCreateOut:

    existing_user = user_repository.find_user_by_username(user.username, db)
    if existing_user:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN, detail="User already exist")

    user.password = hash_password(user.password)
    return user_repository.create(user, db)


def get_user(user_uuid: UUID, db: Session) -> UserCreateOut:
    db_user = user_repository.find_user_by_uuid(user_uuid, db)

    if db_user is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                            detail=f"User with uuid of {user_uuid} was not found")

    return db_user
