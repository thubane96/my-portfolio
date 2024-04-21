from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from uuid import UUID
from typing import List

from app.db.database import get_db
from app.schemas.employee import EmployeeCreateIn, EmpployeeCreateOut, EmployeeUpdateIn, EmployeeUpdateOut, CoreEmployee
from app.services import employee as employee_service

router = APIRouter()

@router.get("/", status_code=200, response_model=List[CoreEmployee])
def get_employees(db: Session = Depends(get_db)) -> List[CoreEmployee]:
    return employee_service.all(db)

@router.get("/{employee_id}", status_code=200, response_model=CoreEmployee)
def get_employee(employee_id: UUID, db: Session = Depends(get_db)) -> CoreEmployee:
    return employee_service.find(employee_id, db)


@router.post("/", status_code=201, response_model=EmpployeeCreateOut)
def create_employee(employee_in: EmployeeCreateIn, db: Session = Depends(get_db)) -> EmpployeeCreateOut:
    return employee_service.create(employee_in, db)

@router.put("/{employee_id}", status_code=200, response_model=EmployeeUpdateOut)
def update_employee(employee_id: UUID, employee_in: EmployeeUpdateIn, db: Session = Depends(get_db)) -> EmployeeUpdateOut:
    return employee_service.update(employee_id, employee_in, db)


@router.delete("/{employee_id}")
def delete_employee(employee_id: UUID, db: Session = Depends(get_db)) -> CoreEmployee:
    return employee_service.delete(employee_id, db)