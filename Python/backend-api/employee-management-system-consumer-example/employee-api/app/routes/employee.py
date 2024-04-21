from fastapi import APIRouter
from uuid import UUID

from app.services.employee import find

router = APIRouter()

@router.get("/{employee_id}")
def get_employee(employee_id: UUID):
    return find(employee_id)