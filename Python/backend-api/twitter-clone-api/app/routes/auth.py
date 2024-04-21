from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session


from app.db.database import get_db
from app.services.auth_service import auth
from app.schemas.user import UserSignin


router = APIRouter()


@router.post('/signin')
def login(user_credentials: UserSignin, db: Session = Depends(get_db)):
    return auth(user_credentials, db)
