from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import models
from database import engine
from routers import signin, signup, post, comment, comment_reply, friend_system, user, message

models.Base.metadata.create_all(bind=engine)

app = FastAPI()


origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
app.include_router(signin.router)
app.include_router(signup.router)
app.include_router(post.router)
app.include_router(comment.router)
app.include_router(comment_reply.router)
app.include_router(friend_system.router)
app.include_router(user.router)
app.include_router(message.router)


@app.get('/')
def welcome_message():
    return {'message': 'welcome to my api'}