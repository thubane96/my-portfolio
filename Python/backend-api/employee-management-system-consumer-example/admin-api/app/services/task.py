from sqlalchemy.orm import Session
from fastapi import HTTPException
from typing import List
from uuid import UUID
from datetime import datetime

from app.models.models import Task
from app.schemas.task import CoreTask, TaskCreateIn, TaskCreateOut, TaskUpdateIn, TaskUpdateOut
from app.services import employee as employee_service
from app.repositories import task as task_repo


def all(db: Session) -> List[CoreTask]:
    return task_repo.all(db)


def all_by_employee(employee_id: UUID, db: Session) -> List[CoreTask]:
    _ = employee_service.find(employee_id, db)
    return task_repo.all_by_employee(employee_id, db)

def find(task_id: UUID, db: Session) -> CoreTask:
    task = task_repo.find(task_id, db)

    if not task:
        raise HTTPException(status_code=404, detail=f"Task with uuid of {task_id} was not found!")
    return task


def create(task_in: TaskCreateIn, db: Session) -> TaskCreateOut:
    _ = employee_service.find(task_in.employee_id, db)
    
    return task_repo.create(task_in, db)


def update(task_id: UUID, task_in: TaskUpdateIn, db: Session) -> TaskUpdateOut:
    db_task = find(task_id, db)
    return task_repo.update(db_task, task_in, db)


def delete_task(task_id: UUID, db: Session) -> CoreTask:
    task = find(task_id, db)
    return task_repo.delete(task, db)