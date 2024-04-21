from fastapi import Depends, HTTPException, APIRouter, status
from sqlalchemy.orm import Session
import schemas, models
from database import get_db
from util import password

router = APIRouter(prefix="/signup", tags=["signup"])


@router.post("/", status_code=status.HTTP_201_CREATED, response_model=schemas.User)
async def signup(user: schemas.UserCreate, db: Session = Depends(get_db)):

    """
    Creates/Signs up a new user. Required fields are:

    - **First Name**: required
    - **Last Name**: required
    - **Gender**: required
    - **Picture**: Optional and should be in decoded bytes(base64)
    - **Email**: required, should be unique and will be verified that it's a proper email
    - **Password**: required, should be checked if it's a proper password
    """

    is_signedup = db.query(models.User).filter(models.User.email == user.email).first()

    if is_signedup != None:
        raise HTTPException(
            status_code=status.HTTP_406_NOT_ACCEPTABLE, detail="invalid credentials"
        )
    user.password = password.hash_password(user.password)

    new_user = models.User(**user.dict())
    db.add(new_user)
    db.commit()
    db.refresh(new_user)

    return new_user
