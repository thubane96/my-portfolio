from sqlalchemy.orm import Session
from typing import List
from uuid import UUID
from datetime import datetime

from app.models.models import Leave
from app.schemas.leave import CoreLeave, LeaveCreateIn, LeaveCreateOut, LeaveUpdateIn, LeaveUpdateOut

def all(db: Session) -> List[CoreLeave]:
    return db.query(Leave).all()


def find(leave_id: UUID, db: Session) -> CoreLeave:
    return db.query(Leave).filter(Leave.id == leave_id).first()


def get_employee_leaves(employee_id: UUID, db: Session) -> List[CoreLeave]:
    return db.query(Leave).filter(Leave.employee_id == employee_id).all()


def create(leave_in: LeaveCreateIn, db: Session) -> LeaveCreateOut:
    leave = Leave(**leave_in.dict())
    db.add(leave)
    db.commit()
    db.refresh(leave)
    return leave


def update(leave: any, leave_in: LeaveUpdateIn, db: Session) -> LeaveUpdateOut:
    for key, value in leave_in.dict(exclude_unset=True).items():
        setattr(leave, key, value)
    db.commit()
    db.refresh(leave)
    return leave


def delete(leave: any, db: Session) -> LeaveUpdateOut:
    leave.deleted_at = datetime.now()
    db.commit()
    return leave