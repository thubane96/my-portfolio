from sqlalchemy.orm import Session
from typing import List
from uuid import UUID
from datetime import datetime

from app.models.models import Task
from app.schemas.task import TaskCreateIn, TaskCreateOut, TaskUpdateIn, TaskUpdateOut, CoreTask

def all(db: Session) -> List[CoreTask]:
    return db.query(Task).all()

def all_by_employee(employee_id: UUID, db: Session) -> List[CoreTask]:
    return db.query(Task).filter(Task.employee_id == employee_id).all()

def find(task_id: UUID, db: Session) -> CoreTask:
    return db.query(Task).filter(Task.id == task_id).first()

def create(task: TaskCreateIn, db: Session) -> TaskCreateOut:
    new_task = Task(**task.dict())
    db.add(new_task)
    db.commit()
    db.refresh(new_task)
    return new_task

def update(db_task, task_in: TaskUpdateIn, db: Session) -> TaskUpdateOut:
    for key, value in task_in.dict(exclude_unset=True).items():
        setattr(db_task, key, value)
    db.commit()
    return db_task


def delete(db_task: Task, db: Session) -> CoreTask:
    db_task.deleted_at = datetime.now()
    db.commit()
    return db_task