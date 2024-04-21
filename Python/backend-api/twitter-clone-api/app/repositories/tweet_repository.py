from sqlalchemy.orm import Session, joinedload
from uuid import UUID
from datetime import datetime

from app.models.models import Tweet


def save_tweet(tweet: Tweet, db: Session):
    db.add(tweet)
    db.commit()
    db.refresh(tweet)
    return tweet


def get_tweet(tweet_uuid: UUID, db: Session):
    return db.query(Tweet).options(joinedload(Tweet.image)).filter(Tweet.tweet_uuid == tweet_uuid).first()


def get_tweets(db: Session):
    return db.query(Tweet).options(joinedload(Tweet.image)).all()


def delete_tweet(db_tweet, db: Session):
    db_tweet.deleted_at = datetime.now()
    db_tweet.image.deleted_at = datetime.now()
    db.commit()
    return db_tweet
