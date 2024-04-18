import requests
import logging
import time
from multiprocessing import Process, Queue
from threading import Thread

# 设置日志及日志级别
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# 根据实际情况修改成自己的用户名和密码
TEST_USERNAME = 'user1'
TEST_PASSWORD = 'a123456'

# 请根据实际情况修改BASE_URL
BASE_URL = 'http://localhost:8080'

def login():
    url = BASE_URL + '/api/user/login'
    req_body = {'username': TEST_USERNAME, 'password': TEST_PASSWORD}
    res = requests.post(url, json=req_body)
    return res.cookies['satoken']

def get_all_conferences(satoken):
    url = BASE_URL + '/api/conference/getAllConferences'
    headers = {'satoken': satoken}
    cookies = {'satoken': satoken}
    res = requests.get(url, headers=headers, cookies=cookies)
    return res.status_code, res.json()

def load_test(satoken):
    process_cnt = 10
    loop_cnt = 500
    
    q = Queue()
    
    def worker(idx):
        success_request_cnt = 0
        failed_request_cnt = 0
        for _ in range(loop_cnt):
            status_code, res = get_all_conferences(satoken)
            if status_code == 200:
                success_request_cnt += 1
            else:
                failed_request_cnt += 1
        q.put((idx, success_request_cnt, failed_request_cnt))
    
    threads = [Thread(target=worker, args=(idx,)) for idx in range(process_cnt)]
    
    test_start = time.time()
    for t in threads:
        t.start()
    
    total_success_cnt = 0
    for _ in range(process_cnt):
        idx, success_request_cnt, failed_request_cnt = q.get()
        logger.info(f'Thread {idx} finished. Success request count: {success_request_cnt}, Failed request count: {failed_request_cnt}')
        total_success_cnt += success_request_cnt
    
    test_end = time.time()
    
    logger.info(f'Total success request count: {total_success_cnt}')
    logger.info(f'Total time: {test_end - test_start} seconds')
    qps = total_success_cnt / (test_end - test_start)
    
    if qps < 5:
        logger.warning(f'QPS is too low: {qps}')
    else:
        logger.info(f'QPS: {qps} is acceptable')


def main():
    satoken = login()
    load_test(satoken)

if __name__ == '__main__':
    main()