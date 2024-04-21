from sqlalchemy.orm import Session
from fastapi import HTTPException, status


from app.repositories.user_repository import find_user_by_username
from app.utils.auth_util import verify_password, create_access_token


def auth(user: dict, db: Session):
    db_user = find_user_by_username(user.username, db)

    if not db_user:
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN, detail="Invalid Credentials")

    if not verify_password(user.password, db_user.password):
        raise HTTPException(
            status_code=status.HTTP_403_FORBIDDEN, detail="Invalid Credentials")

    jwt_token = create_access_token({"user_uuid": str(db_user.user_uuid)})
    print('Token: ', jwt_token)
    return {"token": jwt_token, "token_type": "bearer"}
