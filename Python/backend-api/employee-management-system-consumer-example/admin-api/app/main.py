from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.models import models
from app.db.database import engine
from app.routes import task, employee, leave

app = FastAPI()

origins = [
    "http://localhost:8080",
    "http://localhost:4200"
]


app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_methods=['*'],
    allow_headers=['*']
)


models.Base.metadata.create_all(bind=engine)


app.include_router(employee.router, prefix="/employees", tags=["Employee"])
app.include_router(task.router, prefix="/tasks", tags=["Task"])
app.include_router(leave.router, prefix="/leaves", tags=["Leave"])