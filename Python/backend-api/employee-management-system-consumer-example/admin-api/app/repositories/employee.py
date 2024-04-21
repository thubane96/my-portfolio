from sqlalchemy.orm import Session
from typing import List
from uuid import UUID
from datetime import datetime

from app.models.models import Employee
from app.schemas.employee import EmployeeCreateIn, EmpployeeCreateOut, EmployeeUpdateIn, EmployeeUpdateOut, CoreEmployee

def all(db: Session) -> List[CoreEmployee]:
    return db.query(Employee).all()

def find(employee_id: UUID, db: Session) -> CoreEmployee:
    return db.query(Employee).filter(Employee.id == employee_id).first()

def create(employee: EmployeeCreateIn, db: Session) -> EmpployeeCreateOut:
    new_employee = Employee(**employee.dict())
    db.add(new_employee)
    db.commit()
    db.refresh(new_employee)
    return new_employee

def update(db_employee: Employee, employee_in: EmployeeUpdateIn, db: Session) -> EmployeeUpdateOut:
    for key, value in employee_in.dict(exclude_unset=True).items():
        setattr(db_employee, key, value)
    db.commit()
    return db_employee

def delete(db_employee: Employee, db: Session) -> CoreEmployee:
    db_employee.is_active = False
    db_employee.deleted_at = datetime.now()
    db.commit()
    return db_employee

def get_by_username(email: str, db: Session) -> CoreEmployee:
    return db.query(Employee).filter(Employee.email == email).first()