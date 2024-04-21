import logging
import requests

from config.config import settings, redis_connection

key = 'leave_status'
group = 'leave_status_group'

try:
    redis_connection().xgroup_create(key, group)
except:
    logging.error('Group already exists!')

while True:
    try:
        leaves = redis_connection().xreadgroup(group, key, {key: '>'}, None)
        if leaves != []:
            for leave in leaves:
                leave = leave[1][0][1]
                leave_id = leave['leave_id']
                status = leave['status']
                req = requests.put(f'{settings.employee_api}/leave/{leave_id}', json={'status': status})
                if req.status_code != 200:
                    logging.error(f'Error updating leave {leave_id} status')
                    continue
                logging.info(f'Leave with id of {leave_id} status updated to {status}')
        else:
            logging.info('No leaves to consume')
    except Exception as e:
        logging.error(f'Error consuming leave: {e}')