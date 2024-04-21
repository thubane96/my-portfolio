from sqlalchemy.orm import Session
from uuid import UUID

from app.models.models import User


def all(db: Session):
    return db.query(User).all()


def create(user, db: Session):
    user = User(**user.dict())
    db.add(user)
    db.commit()
    db.refresh(user)
    return user


def find_user_by_uuid(user_uuid: UUID, db: Session):
    return db.query(User).filter(User.user_uuid == user_uuid).first()


def find_user_by_username(username: str, db: Session):
    return db.query(User).filter(User.username == username).first()
