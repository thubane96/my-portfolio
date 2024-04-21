from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from typing import List
from uuid import UUID

from app.db.database import get_db
from app.schemas.leave import CoreLeave, LeaveCreateIn, LeaveCreateOut, LeaveUpdateIn, LeaveUpdateOut
from app.services import leave as leave_service

router = APIRouter()


@router.get("/")
def all(db: Session = Depends(get_db)) -> List[CoreLeave]:
    return leave_service.all(db)


@router.get("/{leave_id}")
def find(leave_id: UUID, db: Session = Depends(get_db)) -> CoreLeave:
    return leave_service.find(leave_id, db)


@router.get("/employee/{employee_id}")
def get_employee_leaves(employee_id: UUID, db: Session = Depends(get_db)) -> List[CoreLeave]:
    return leave_service.get_employee_leaves(employee_id, db)


@router.post("/")
def create(leave_in: LeaveCreateIn, db: Session = Depends(get_db)) -> LeaveCreateOut:
    return leave_service.create(leave_in, db)


@router.put("/{leave_id}")
def update(leave_id: UUID, leave_in: LeaveUpdateIn, db: Session = Depends(get_db)) -> LeaveUpdateOut:
    return leave_service.update(leave_id, leave_in, db)


@router.delete("/{leave_id}")
def delete(leave_id: UUID, db: Session = Depends(get_db)) -> LeaveUpdateOut:
    return leave_service.delete(leave_id, db)