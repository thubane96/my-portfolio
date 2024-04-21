from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from typing import List
from uuid import UUID

from app.schemas.user import UserCreateIn, UserCreateOut, CoreUser
from app.services import user_service
from app.repositories import user_repository
from app.db.database import get_db
from app.utils.auth_util import verify_access_token


router = APIRouter()


@router.post('/signup', response_model=UserCreateOut)
def create(user: UserCreateIn, db: Session = Depends(get_db)) -> UserCreateOut:
    return user_service.create(user, db)


@router.get('/', response_model=List[CoreUser])
def all(db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> List[CoreUser]:
    return user_repository.all(db)


@router.get('/{user_uuid}', response_model=CoreUser)
def get_user(user_uuid: UUID, db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> CoreUser:
    return user_service.get_user(user_uuid, db)
