from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.models import models
from app.db.database import engine
from app.routes import employee, task, leave

app = FastAPI()

origins = [
    "http://localhost:8000",
    "http://localhost:4200"
]


app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_methods=['*'],
    allow_headers=['*']
)

models.Base.metadata.create_all(bind=engine)

app.include_router(employee.router, prefix="/employee", tags=["employee"])
app.include_router(task.router, prefix="/task", tags=["task"])
app.include_router(leave.router, prefix="/leave", tags=["leave"])