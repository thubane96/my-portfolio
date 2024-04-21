from sqlalchemy.orm import Session
from uuid import UUID

from app.models.models import Reaction


def react(reaction, db: Session):
    db_reaction = db.query(Reaction).filter(
        Reaction.parent_uuid == reaction.parent_uuid, Reaction.user_uuid == reaction.user_uuid).first()

    if db_reaction:
        db.delete(db_reaction)
    if db_reaction is None:
        db.add(reaction)
    return True
