import requests, time
from fastapi import HTTPException
from uuid import UUID
import json

from app.config.config import settings, redis_connection


def find(task_id: UUID):
    req = requests.get(f'{settings.admin_api}/tasks/{task_id}')

    if req.status_code != 200:
        raise HTTPException(status_code=req.status_code, detail=f"Task with id of {task_id} was not found")
    return req.json()


def all(employee_id: UUID):
    req = requests.get(f'{settings.admin_api}/tasks/employee/{employee_id}')
    if req.status_code != 200:
        raise HTTPException(status_code=req.status_code, detail=f"Tasks for employee with id of {employee_id} were not found")
    return req.json()


def update_status(task_id: UUID, status: str):

    redis_connection().xadd('task_status', {'task_id': str(task_id), 'status': status}, '*')
    time.sleep(3)
    return find(task_id)

