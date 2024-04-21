from sqlalchemy.orm import Session
from fastapi import HTTPException
from typing import List
from uuid import UUID
from datetime import datetime

from app.schemas.leave import CoreLeave, LeaveCreateIn, LeaveCreateOut, LeaveUpdateIn, LeaveUpdateOut
from app.repositories import leave as leave_repo

def all(db: Session) -> List[CoreLeave]:
    return leave_repo.all(db)

def find(leave_id: UUID, db: Session) -> CoreLeave:
    leave = leave_repo.find(leave_id, db)

    if not leave:
        raise HTTPException(status_code=404, detail=f"Leave with id of {leave_id} was not found")
    return leave

def get_employee_leaves(leave_id: UUID, db: Session) -> List[CoreLeave]:
    return leave_repo.get_employee_leaves(leave_id, db)

def create(leave_in: LeaveCreateIn, db: Session) -> LeaveCreateOut:
    if leave_in.start_date < datetime.now().astimezone() or leave_in.end_date < datetime.now().astimezone():
        raise HTTPException(status_code=400, detail="Leave start or end date must be in the future")
    return leave_repo.create(leave_in, db)

def update(leave_id: UUID, leave_in: LeaveUpdateIn, db: Session) -> LeaveUpdateOut:
    db_leave = leave_repo.find(leave_id, db)
    return leave_repo.update(db_leave, leave_in, db)

def delete(leave_id: UUID, db: Session) -> LeaveUpdateOut:
    leave = leave_repo.find(leave_id, db)
    return leave_repo.delete(leave, db)