from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from uuid import UUID
from typing import List

from app.db.database import get_db
from app.schemas.task import TaskCreateIn, TaskCreateOut, TaskUpdateIn, TaskUpdateOut, CoreTask
from app.services import task as task_service

router = APIRouter()

@router.get("/", status_code=200, response_model=List[CoreTask])
def get_tasks(db: Session = Depends(get_db)) -> List[CoreTask]:
    return task_service.all(db)


@router.get("/{task_uuid}", status_code=200, response_model=CoreTask)
def get_task(task_uuid: UUID, db: Session = Depends(get_db)) -> CoreTask:
    return task_service.find(task_uuid, db)


@router.get("/employee/{employee_uuid}", status_code=200, response_model=List[CoreTask])
def get_tasks_by_employee(employee_uuid: UUID, db: Session = Depends(get_db)) -> List[CoreTask]:
    return task_service.all_by_employee(employee_uuid, db)

@router.post("/", status_code=201, response_model=TaskCreateOut)
def create_task(task_in: TaskCreateIn, db: Session = Depends(get_db)) -> TaskCreateOut:
    return task_service.create(task_in, db)


@router.put("/{task_uuid}", status_code=200, response_model=TaskUpdateOut)
def update_task(task_uuid: UUID, task_in: TaskUpdateIn, db: Session = Depends(get_db)) -> TaskUpdateOut:
    return task_service.update(task_uuid, task_in, db)


@router.delete("/{task_uuid}")
def delete_task(task_uuid: UUID, db: Session = Depends(get_db)) -> CoreTask:
    return task_service.delete_task(task_uuid, db)