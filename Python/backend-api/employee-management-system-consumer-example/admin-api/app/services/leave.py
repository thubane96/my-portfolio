from sqlalchemy.orm import Session
from typing import List
from fastapi import HTTPException
import requests
import time
from uuid import UUID

from app.schemas.leave import CoreLeave
from app.config.config import settings, redis_connection

  
def all():
    req = requests.get(f"{settings.employee_api}/leave/")

    if req.status_code != 200:
        raise HTTPException(status_code=req.status_code, detail=req.json()) 
    
    return req.json()


def find(leave_id: UUID):
    req = requests.get(f"{settings.employee_api}/leave/{leave_id}")

    if req.status_code != 200:
        raise HTTPException(status_code=req.status_code, detail=req.json())

    return req.json()

def update(leave_id: UUID, status: str):
    redis_connection().xadd('leave_status', {'leave_id': str(leave_id), 'status': status}, '*')
    time.sleep(1)
    return find(leave_id)