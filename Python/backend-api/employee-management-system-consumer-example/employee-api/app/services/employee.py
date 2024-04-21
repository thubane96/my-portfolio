import requests
from fastapi import HTTPException
from uuid import UUID
from app.config.config import settings

def find(employee_id: UUID):
    req = requests.get(f'{settings.admin_api}/employees/{employee_id}')

    if req.status_code != 200:
        raise HTTPException(status_code=req.status_code, detail=f"Employee with id of {employee_id} was not found")
    return req.json()