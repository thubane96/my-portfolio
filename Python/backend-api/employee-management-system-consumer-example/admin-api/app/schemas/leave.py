from uuid import UUID
from pydantic import BaseModel
from typing import Optional
from datetime import datetime


class CoreLeave(BaseModel):
    id: UUID
    employee_id: UUID
    start_date: datetime
    end_date: datetime
    leave_type: str
    status: str
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True
