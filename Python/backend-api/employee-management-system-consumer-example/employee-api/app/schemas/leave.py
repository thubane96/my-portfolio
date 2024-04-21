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



class LeaveCreateIn(BaseModel):
    employee_id: UUID
    start_date: datetime
    end_date: datetime
    leave_type: str
    status: str


class LeaveCreateOut(CoreLeave):
    pass


class LeaveUpdateIn(BaseModel):
    employee_id: Optional[UUID]
    start_date: Optional[datetime]
    end_date: Optional[datetime]
    leave_type: Optional[str]
    status: Optional[str]


class LeaveUpdateOut(CoreLeave):
    pass

