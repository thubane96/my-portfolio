from fastapi import Depends, HTTPException, status, APIRouter
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from database import get_db
from util.oauth import generate_token
from util.password import verify_password
import schemas, models

router = APIRouter(prefix="/login", tags=["signin"])


@router.post("/", status_code=status.HTTP_202_ACCEPTED)
async def signin(credentials: OAuth2PasswordRequestForm = Depends(), db: Session = Depends(get_db)):

    """
    Signs in a user and creates a unique token. Required fields are:

    - **Username**: required, unique email
    - **Password**: required, password

    """
    user = (
        db.query(models.User).filter(models.User.email == credentials.username).first()
    )

    if not user:
        raise HTTPException(
            status_code=status.HTTP_406_NOT_ACCEPTABLE, detail="Invalid Credentials"
        )

    if not verify_password(credentials.password, user.password):
        raise HTTPException(
            status_code=status.HTTP_406_NOT_ACCEPTABLE, detail="Invalid Credentials"
        )

    token = generate_token(data={"user_id": user.id})

    return {"token": token, "token_type": "bearer"}
