from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.models import models
from app.db.database import engine
from app.routes import (user, auth, tweet, comment, comment_reply)


app = FastAPI()


origins = [
    "http://localhost",
    "http://localhost:4200"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

models.Base.metadata.create_all(bind=engine)


app.include_router(user.router, prefix="/users", tags=["User"])
app.include_router(auth.router, prefix="/auths", tags=["Authentication"])
app.include_router(tweet.router, prefix="/tweets", tags=["Tweet"])
app.include_router(comment.router, prefix="/comment", tags=["Comments"])
app.include_router(comment_reply.router,
                   prefix="/comment_reply", tags=["Comment Reply"])
