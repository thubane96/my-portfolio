from sqlalchemy.orm import Session

from app.models.models import UserImage, TweetImage, CommentImage, CommentReplyImage, MessageImage


def save_user_image(image_path, db: Session):
    db_image = UserImage()
    db_image.image_path = image_path
    db.add(db_image)
    db.commit()
    db.refresh(db_image)
    return db_image.user_image_uuid


def save_tweet_image(image_path, db: Session):
    db_image = TweetImage()
    db_image.image_path = image_path
    db.add(db_image)
    db.commit()
    db.refresh(db_image)
    return db_image.tweet_image_uuid


def save_comment_image(image_path, db: Session):
    db_image = CommentImage()
    db_image.image_path = image_path
    db.add(db_image)
    db.commit()
    db.refresh(db_image)
    return db_image.comment_image_uuid


def save_comment_reply_image(image_path, db: Session):
    db_image = CommentReplyImage()
    db_image.image_path = image_path
    db.add(db_image)
    db.commit()
    db.refresh(db_image)
    return db_image.comment_reply_image_uuid


def save_message_image(image_path, db: Session):
    db_image = MessageImage()
    db_image.image_path = image_path
    db.add(db_image)
    db.commit()
    db.refresh(db_image)
    return db_image.message_image_uuid
