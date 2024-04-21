import logging
import requests

from config.config import settings, redis_connection

key = 'task_status'
group = 'task_status_group'


try:
    redis_connection().xgroup_create(key, group)
except:
    logging.error('Group already exists!')

while True:
    try:
        tasks = redis_connection().xreadgroup(group, key, {key: '>'}, None)
        if tasks != []:
            for task in tasks:
                task = task[1][0][1]
                task_id = task['task_id']
                status = task['status']
                req = requests.put(f'{settings.admin_api}/tasks/{task_id}', json={'status': status})
                if req.status_code != 200:
                    logging.error(f'Error updating task {task_id} status')
                    continue
                logging.info(f'Task with id of {task_id} status updated to {status}')
        else:
            logging.info('No tasks to consume')
    except Exception as e:
        logging.error(f'Error consuming task: {e}')