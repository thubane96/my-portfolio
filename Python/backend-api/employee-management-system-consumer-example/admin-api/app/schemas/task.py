from uuid import UUID
from pydantic import BaseModel
from typing import Optional
from datetime import datetime


class CoreTask(BaseModel):
    id: UUID
    title: str
    description: str
    status: str
    employee_id: UUID
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True

class TaskCreateIn(BaseModel):
    title: str
    description: str
    status: str
    employee_id: UUID


class TaskCreateOut(CoreTask):
    pass

    


class TaskUpdateIn(BaseModel):
    title: Optional[str]
    description: Optional[str]
    status: Optional[str]
    employee_id: Optional[UUID]


class TaskUpdateOut(CoreTask):
    pass