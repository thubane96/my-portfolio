from pydantic import BaseSettings
from redis_om import get_redis_connection


class Settings(BaseSettings):
    database_hostname: str
    database_port: str
    database_password: str
    database_name: str
    database_username: str
    redis_host: str
    redis_port: str
    redis_password: str
    employee_api: str


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