import requests
import time

# Set the URL of the API endpoint you want to poll
api_url = "https://example.com/api/endpoint"

# Function to make a GET request and check the status
def poll_endpoint():
    start_time = time.time()
    response = requests.get(api_url)
    end_time = time.time()

    if response.status_code == 200:
        data = response.json()
        data.model.status
        status = data.get("status")
        if status == "completed":
            return data, end_time - start_time
    return None, end_time - start_time

# Poll the endpoint until a completed status is received
while True:
    result, elapsed_time = poll_endpoint()
    if result:
        print("Task is completed:", result)
        print("Time taken:", elapsed_time, "seconds")
        break
    else:
        print("Task is not completed yet. Waiting...")
        print("Time taken:", elapsed_time, "seconds")
        time.sleep(5)  # Poll every 5 seconds (adjust as needed)
