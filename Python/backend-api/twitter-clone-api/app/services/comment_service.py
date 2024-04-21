from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from uuid import UUID

from app.schemas.comment import CommentCreateIn, CommentCreateOut, CoreComment
from app.models.models import Comment, Reaction
from app.repositories import image_repository, comment_repository, react_repository


def save_comment(comment: CommentCreateIn, user_uuid: UUID, db: Session) -> CommentCreateOut:
    if comment.comment_body is None and comment.comment_image is None:
        raise HTTPException(status_code=status.HTTP_406_NOT_ACCEPTABLE,
                            detail="Comment missing body and image, at least one should be present")

    db_comment = Comment()
    if comment.comment_body:
        db_comment.comment_body = comment.comment_body

    if comment.comment_image:
        image_uuid = image_repository.save_comment_image(
            comment.comment_image, db)
        db_comment.image_uuid = image_uuid

    db_comment.user_uuid = user_uuid
    return comment_repository.save_comment(comment, db)


def get_comment(comment_uuid: UUID, db: Session) -> CoreComment:
    db_comment = comment_repository.get_comment(comment_uuid, db)

    if db_comment is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND,
                            detail=f"Comment with uuid of {comment_uuid} was not found")

    return db_comment


def react(comment_uuid: UUID, user_uuid: UUID, db: Session) -> bool:
    _ = get_comment(comment_uuid, db)
    reaction = Reaction(user_uuid=user_uuid,
                        parent_uuid=comment_uuid, is_comment=True)
    return react_repository.react(reaction, db)


def delete_comment(comment_uuid: UUID, user_uuid: UUID, db: Session) -> CoreComment:
    db_comment = get_comment(comment_uuid, db)

    if db_comment.user_uuid != user_uuid:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN,
                            detail=f"User with uuid of {user_uuid} not allowed to delete a comment with uuid of {comment_uuid}")
    return comment_repository.delete_comment(db_comment, db)
