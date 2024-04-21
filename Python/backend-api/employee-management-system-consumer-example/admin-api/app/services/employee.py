from sqlalchemy.orm import Session
from fastapi import HTTPException
from typing import List
from uuid import UUID


from app.repositories import employee as employee_repo
from app.schemas.employee import CoreEmployee, EmployeeCreateIn, EmpployeeCreateOut, EmployeeUpdateIn, EmployeeUpdateOut
from app.utils.auth import hash_password


def all(db: Session) -> list[CoreEmployee]:
    return employee_repo.all(db)


def find(employee_id: UUID, db: Session) -> CoreEmployee:
    employee = employee_repo.find(employee_id, db)

    if not employee:
        raise HTTPException(status_code=404, detail=f"Employee with uuid of {employee_id} was not found!")
    return employee


def create(employee_in: EmployeeCreateIn, db: Session) -> EmpployeeCreateOut:
    db_employee = employee_repo.get_by_username(employee_in.email, db)

    if db_employee:
        raise HTTPException(status_code=400, detail=f"Employee with email of {employee_in.email} already exists!")
    if employee_in.password:
        employee_in.password = hash_password(employee_in.password)
    
    return employee_repo.create(employee_in, db)


def update(employee_id: UUID, employee_in: EmployeeUpdateIn, db: Session) -> EmployeeUpdateOut:
    db_employee = employee_repo.find(employee_id, db)

    if not db_employee:
        raise HTTPException(status_code=404, detail=f"Employee with uuid of {employee_id} was not found!")
    
    if employee_in.password:
        employee_in.password = hash_password(employee_in.password)
    
    return employee_repo.update(db_employee, employee_in, db)


def delete(employee_id: UUID, db: Session) -> CoreEmployee:
    employee = find(employee_id, db)
    return employee_repo.delete(employee, db)