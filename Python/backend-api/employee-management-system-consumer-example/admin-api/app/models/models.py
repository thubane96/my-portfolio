import uuid
from sqlalchemy import Column, String, Integer, Float, DateTime, func, Boolean, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.postgresql import UUID

from app.db.database import Base


class Employee(Base):
    __tablename__ = "employees"

    id = Column(UUID(as_uuid=True),primary_key=True, default=uuid.uuid4)
    name = Column(String, nullable=False)
    position = Column(String, nullable=False)
    is_active = Column(Boolean, nullable=False)
    salary = Column(Float, nullable=False)
    age = Column(Integer, nullable=False)
    image = Column(String, nullable=True)
    email = Column(String, nullable=False)
    password = Column(String, nullable=True)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)

    tasks = relationship("Task", back_populates="employee")


class Task(Base):
    __tablename__ = "tasks"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    title = Column(String, nullable=False)
    description = Column(String, nullable=False)
    status = Column(String, nullable=False)
    employee_id = Column(UUID(as_uuid=True), ForeignKey("employees.id"), nullable=False)
    created_at = Column(DateTime, server_default=func.now())
    updated_at = Column(DateTime, server_default=func.now(),
                        onupdate=func.now())
    deleted_at = Column(DateTime, nullable=True)
    
    employee = relationship("Employee", back_populates="tasks")

