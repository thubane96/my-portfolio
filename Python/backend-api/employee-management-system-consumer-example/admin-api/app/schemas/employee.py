from uuid import UUID
from pydantic import BaseModel
from typing import Optional
from datetime import datetime


class CoreEmployee(BaseModel):
    id: UUID
    name: str
    position: str
    is_active: bool
    salary: float
    age: int
    image: Optional[str]
    email: Optional[str]
    password: Optional[str]
    created_at: datetime
    updated_at: datetime
    deleted_at: Optional[datetime]

    class Config:
        orm_mode = True



class EmployeeCreateIn(BaseModel):
    name: str
    position: str
    is_active: bool
    salary: float
    age: int
    image: Optional[str]
    email: str
    password: Optional[str]



class EmpployeeCreateOut(CoreEmployee):
    pass


class EmployeeUpdateIn(BaseModel):
    name: Optional[str]
    position: Optional[str]
    is_active: Optional[bool]
    salary: Optional[float]
    age: Optional[int]
    image: Optional[str]
    email: Optional[str]
    password: Optional[str]


class EmployeeUpdateOut(CoreEmployee):
    pass
