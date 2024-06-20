import json

import requests
import logging
import time
from multiprocessing import Process, Queue
from threading import Thread, Event

# 设置日志及日志级别
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# 根据实际情况修改成自己的用户名和密码
TEST_USERNAME = 'liangyf_test'
TEST_PASSWORD = '123qwe'

# 管理员：用户名为`admin`，密码为`adm123456

# 请根据实际情况修改BASE_URL
BASE_URL = 'http://localhost:8080'
QPS = 20  # Set the desired QPS here
TEST_DURATION = 10  # seconds

def login():
    url = BASE_URL + '/api/user/login'
    req_body = {'username': TEST_USERNAME, 'password': TEST_PASSWORD}
    res = requests.post(url, json=req_body)
    return res.cookies['satoken']

# def get_contributes_by_username(satoken):
#     url = BASE_URL + '/api/contribute/listByName/' + TEST_USERNAME
#     headers = {'satoken': satoken}
#     cookies = {'satoken': satoken}
#     res = requests.get(url, headers=headers, cookies=cookies)
#     return res.status_code, res.json()

def contribute(satoken):
    url = BASE_URL + '/api/contribute'
    headers = {'satoken': satoken}
    cookies = {'satoken': satoken}

    body = {
        "conferenceName": "test3",
        "abstractContent": "This is an abstract content for the conference paper.",
        "username": "liangyf_test",
        "realName": "liangyufeng",
        "writers": [
            {
                "name": "liangyufeng",
                "institutionName": "Fudan University",
                "area": "Computer Science",
                "email": "123@163.com"
            }
        ],
        "title": "Test",
        "essayId": "123",
        "topics": ["test"]
    }

    res = requests.post(url, headers=headers, cookies=cookies, json=body)
    return res.status_code, res.json()

def test(satoken):
    print(f'Start time: {time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}')
    print('Set QPS: ', QPS)
    print('Running test...')
    time_1 = time.localtime()
    stop_event = Event()
    success_count = 0
    failure_count = 0

    def worker():
        nonlocal success_count, failure_count
        while not stop_event.is_set():
            start_time = time.time()
            try:
                # status_code, response = get_contributes_by_username(satoken)
                status_code, response = contribute(satoken)
                if status_code == 200:
                    success_count += 1
                    # logger.info(f'Status Code: {status_code}, Response: {response}')
                else:
                    failure_count += 1
                    # logger.error(f'Failed Status Code: {status_code}, Response: {response}')
            except Exception as e:
                failure_count += 1
                logger.error(f'Request failed: {e}')
            elapsed_time = time.time() - start_time
            sleep_time = max(0, (1 / QPS) - elapsed_time)
            time.sleep(sleep_time)

    thread = Thread(target=worker)
    thread.start()

    time.sleep(TEST_DURATION)
    stop_event.set()
    thread.join()

    time_2 = time.localtime()
    print(f'End time:   {time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())}')
    print(f'Running time: {time.mktime(time_2) - time.mktime(time_1)}s')

    print(f'Total successful requests: {success_count}')
    print(f'Total failed requests: {failure_count}')
    print(f'True QPS: {success_count / (time.mktime(time_2) - time.mktime(time_1))}')


def main():
    satoken = login()
    test(satoken)

if __name__ == '__main__':
    main()
