from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from uuid import UUID

from app.schemas.comment_reply import CommentReplyCreateIn, CommentReplyCreateOut
from app.models.models import CommentReply, Reaction
from app.repositories import image_repository, comment_reply_repository, react_repository


def save_comment_reply(comment_reply: CommentReplyCreateIn, user_uuid: UUID, db: Session) -> CommentReplyCreateOut:
    if comment_reply.comment_reply_body is None and comment_reply.comment_reply_image is None:
        raise HTTPException(status_code=status.HTTP_406_NOT_ACCEPTABLE,
                            detail="Comment reply missing body and image, at least one should be present")

    db_comment_reply = CommentReply()
    if comment_reply.comment_reply_body:
        db_comment_reply.comment_reply_body = comment_reply.comment_reply_body

    if comment_reply.comment_reply_image:
        image_uuid = image_repository.save_comment_reply_image(
            comment_reply.comment_reply_image, db)
        db_comment_reply.image_uuid = image_uuid
    db_comment_reply.user_uuid = user_uuid

    return comment_reply_repository.save_comment_reply(db_comment_reply, db)


def get_comment_reply(comment_reply_uuid: UUID, db: Session) -> CommentReplyCreateOut:
    db_comment_reply = comment_reply_repository.get_comment_reply(
        comment_reply_uuid, db)

    if db_comment_reply is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                            detail=f"Comment reply with uuid of {comment_reply_uuid} was not found")

    return db_comment_reply


def react(comment_reply_uuid: UUID, user_uuid: UUID, db: Session) -> bool:
    _ = get_comment_reply(comment_reply_uuid, db)
    reaction = Reaction(user_uuid=user_uuid,
                        parent_uuid=comment_reply_uuid, is_comment_reply=True)
    return react_repository.react(reaction, db)


def delete_comment_reply(comment_reply_uuid: UUID, user_uuid: UUID, db: Session) -> CommentReplyCreateOut:
    db_comment_reply = get_comment_reply(comment_reply_uuid, db)

    if db_comment_reply.user_uuid != user_uuid:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN,
                            detail=f"User with uuid of {user_uuid} not allowed to delete a comment reply with uuid of {comment_reply_uuid}")

    return comment_reply_repository.delete_comment_reply(db_comment_reply, db)
