from pydantic import BaseModel
from uuid import UUID
from datetime import datetime
from typing import Optional


class CoreUserImage(BaseModel):
    user_image_uuid: UUID
    user_uuid: UUID
    image_path: str
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True
