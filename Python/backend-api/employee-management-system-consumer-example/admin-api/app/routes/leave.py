from fastapi import APIRouter
from typing import List
from uuid import UUID

from app.services import leave as leave_service

router = APIRouter()

@router.get("/")
def all():
    return leave_service.all()


@router.get("/{leave_id}")
def find(leave_id: UUID):
    return leave_service.find(leave_id)


@router.put("/{leave_id}")
def update(leave_id: UUID, status: str):
    return leave_service.update(leave_id, status)