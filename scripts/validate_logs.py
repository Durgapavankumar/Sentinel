import requests
import time
import uuid
import random

API_URL = "http://localhost:8081/api/events"
TOTAL_REQUESTS = 100

def generate_payload():
    return {
        "userId": str(uuid.uuid4()),
        "action": random.choice(["LOGIN", "VIEW", "PURCHASE", "LOGOUT"]),
        "metadata": "Test payload"
    }

def run_load_test():
    print(f"Starting load test with {TOTAL_REQUESTS} requests...")
    start_time = time.time()
    
    success_count = 0
    for i in range(TOTAL_REQUESTS):
        try:
            response = requests.post(API_URL, json=generate_payload())
            if response.status_code == 202:
                success_count += 1
        except Exception as e:
            print(f"Request failed: {e}")
            
    end_time = time.time()
    duration = end_time - start_time
    
    print(f"Test completed in {duration:.2f} seconds")
    print(f"Successful requests: {success_count}/{TOTAL_REQUESTS}")
    print(f"TPS: {success_count / duration:.2f}")

if __name__ == "__main__":
    run_load_test()
