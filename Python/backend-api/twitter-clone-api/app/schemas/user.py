from pydantic import BaseModel
from uuid import UUID
from datetime import datetime
from typing import List, Optional

from app.schemas.user_image import CoreUserImage


class UserCreateIn(BaseModel):
    first_name: str
    last_name: str
    username: str
    password: str


class UserSignin(BaseModel):
    username: str
    password: str


class UserCreateOut(BaseModel):
    user_uuid: UUID
    first_name: str
    last_name: str
    username: str
    password: str
    bio: Optional[str]
    birth_date: Optional[str]
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True


class CoreUser(UserCreateOut):
    user_image: Optional[CoreUserImage]
