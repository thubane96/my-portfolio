from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from uuid import UUID
from typing import List

from app.db.database import get_db
from app.repositories import tweet_repository
from app.services import tweet_service
from app.utils.auth_util import verify_access_token
from app.schemas.tweet import TweetCreateIn, TweetCreateOut, CoreTweet

router = APIRouter()


@router.post('/', response_model=TweetCreateOut)
def add_tweet(tweet: TweetCreateIn, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> TweetCreateOut:
    return tweet_service.add_tweet(tweet, user_uuid, db)


@router.post('/react/{tweet_uuid}')
def react(tweet_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> bool:
    return tweet_service.react(tweet_uuid, user_uuid, db)


@router.get('/')
def get_tweets(db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)):
    return tweet_repository.get_tweets(db)


@router.get('/{tweet_uuid}', response_model=CoreTweet)
def get_tweet(tweet_uuid: UUID, db: Session = Depends(get_db), _: UUID = Depends(verify_access_token)) -> CoreTweet:
    return tweet_service.get_tweet(tweet_uuid, db)


@router.delete('/{tweet_uuid}', response_model=CoreTweet)
def delete_tweet(tweet_uuid: UUID, db: Session = Depends(get_db), user_uuid: UUID = Depends(verify_access_token)) -> CoreTweet:
    tweet_service.delete_tweet(tweet_uuid, user_uuid, db)
