from app.config.config import redis_connection
import logging

from app.services import task as task_service
from app.db.database import get_db

key = 'task_status'
group = 'task_status_group'

try:
    redis_connection.xgroup_create(key, group)
except:
    logging.error('Group already exists!')

while True:
    try:
        tasks = redis_connection().xreadgroup(group, key, {key: '>'}, None)
        db = get_db()

        if tasks != []:
            for task in tasks:
                task = task[1][0]
                task_id = task['task_id']
                status = task['status']
                task_service.update(task_id, {'status': status}, db)
                logging.info(f'Task {task_id} status updated to {status}')
                db.close()
        else:
            db.close()
            logging.info('No tasks to consume')
    except Exception as e:
        logging.error(f'Error consuming task: {e}')
        break