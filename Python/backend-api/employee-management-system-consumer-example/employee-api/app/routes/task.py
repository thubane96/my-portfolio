from fastapi import APIRouter
from uuid import UUID

from app.services import task as task_service

router = APIRouter()

@router.get("/{task_id}")
def get_task(task_id: UUID):
    return task_service.find(task_id)


@router.get("/all/{employee_id}")
def get_all_tasks(employee_id: UUID):
    return task_service.all(employee_id)


@router.put("/{task_id}")
def update_task_status(task_id: UUID, status: str):
    return task_service.update_status(task_id, status)