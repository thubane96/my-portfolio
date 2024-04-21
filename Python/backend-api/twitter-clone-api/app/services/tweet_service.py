from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from uuid import UUID

from app.models.models import Tweet, Reaction
from app.repositories import image_repository, tweet_repository, react_repository
from app.schemas.tweet import TweetCreateIn, TweetCreateOut, CoreTweet


def add_tweet(tweet: TweetCreateIn, user_uuid: UUID, db: Session) -> TweetCreateOut:
    if tweet.tweet_body is None and tweet.tweet_image is None:
        raise HTTPException(status_code=status.HTTP_406_NOT_ACCEPTABLE,
                            detail="Post missing body and image, at least one should be present")
    db_tweet = Tweet()

    if tweet.tweet_image is not None:
        image_uuid = image_repository.save_tweet_image(tweet.tweet_image, db)
        db_tweet.image_uuid = image_uuid

    if tweet.tweet_body is not None:
        db_tweet.tweet_body = tweet.tweet_body

    db_tweet.user_uuid = user_uuid
    return tweet_repository.save_tweet(db_tweet, db)


def get_tweet(tweet_uuid: UUID, db: Session) -> CoreTweet:
    db_tweet = tweet_repository.get_tweet(tweet_uuid, db)
    if db_tweet is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                            detail=f"Tweet with uuid of {tweet_uuid} was not found")
    return db_tweet


def react(tweet_uuid: UUID, user_uuid: UUID, db: Session) -> bool:
    _ = get_tweet(tweet_uuid, db)
    reaction = Reaction(user_uuid=user_uuid,
                        parent_uuid=tweet_uuid, is_tweet=True)
    return react_repository.react(reaction, db)


def delete_tweet(tweet_uuid: UUID, user_uuid: UUID, db: Session) -> CoreTweet:
    db_tweet = get_tweet(tweet_uuid, db)
    print("db_tweet.user_uuid ", db_tweet.user_uuid)
    print("user_uuid ", user_uuid)
    if str(db_tweet.user_uuid) != str(user_uuid):
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN,
                            detail=f"User with uuid of {user_uuid} not allowed to delete a tweet with uuid of {tweet_uuid}")

    return tweet_repository.delete_tweet(db_tweet, db)
