from pydantic import BaseSettings
from redis_om import get_redis_connection


class Settings(BaseSettings):
    employee_api: str
    redis_host: str
    redis_port: str
    redis_password: str

    class Config:
        env_file = ".env"


settings = Settings()

def redis_connection():
    return get_redis_connection(
        host=settings.redis_host,
        port=settings.redis_port,
        password=settings.redis_password,
        decode_responses=True,
    )
