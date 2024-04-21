from fastapi import HTTPException, status, Depends
from fastapi.security import OAuth2PasswordBearer
from passlib.context import CryptContext
from jose import JWTError, jwt
from datetime import datetime, timedelta
from uuid import UUID

from app.config.config import settings

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl='signin')


def hash_password(password: str) -> str:
    return pwd_context.hash(password)


def verify_password(plain_password, hashed_password) -> bool:
    return pwd_context.verify(plain_password, hashed_password)


def create_access_token(data: dict) -> str:
    to_encode = data.copy()
    expiring_time = datetime.utcnow(
    ) + timedelta(minutes=settings.access_token_expire_minutes)
    to_encode.update({"exp": expiring_time})
    jwt_token = jwt.encode(
        to_encode, settings.secret_key, algorithm=settings.algorithm)
    return jwt_token


def verify_access_token(token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(status_code=status.HTTP_401_UNAUTHORIZED,
                                          detail="Could not validate credentials", headers={"WWW-Authenticate": "Bearer"})
    try:
        payload = jwt.decode(token, settings.secret_key,
                             algorithms=[settings.algorithm])
        user_uuid: UUID = payload.get("user_uuid")
        if user_uuid is None:
            raise credentials_exception
        return user_uuid
    except JWTError:
        credentials_exception