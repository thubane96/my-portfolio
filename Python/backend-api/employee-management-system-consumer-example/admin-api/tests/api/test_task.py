def test_create_task(create_task):
    response, task = create_task
    created_task = response.json()

    assert response.status_code == 201
    assert created_task["title"] == task.title
    assert created_task["description"] == task.description
    assert created_task["status"] == task.status
    assert created_task["employee_id"] == task.employee_id


def test_get_tasks(client):
    response = client.get("/tasks")

    assert response.status_code == 200
    assert len(response.json()) > 0



def test_get_task(client, create_task):
    response, task = create_task
    created_task = response.json()

    task_id = created_task["id"]
    get_response = client.get(f"/tasks/{task_id}")
    fetched_task = get_response.json()

    assert response.status_code == 201
    assert get_response.status_code == 200
    assert fetched_task["title"] == task.title
    assert fetched_task["description"] == task.description
    assert fetched_task["status"] == task.status
    assert fetched_task["employee_id"] == task.employee_id


def test_update_task(client, create_task):
    response, task = create_task
    created_task = response.json()
    task.title = "Wash Windows"

    task_id = created_task["id"]
    update_response = client.put(f"/tasks/{task_id}", json={"title": task.title})
    updated_task = update_response.json()

    assert response.status_code == 201
    assert update_response.status_code == 200
    assert updated_task["title"] == task.title
    assert updated_task["description"] == task.description
    assert updated_task["status"] == task.status
    assert updated_task["employee_id"] == task.employee_id


def test_delete_task(client, create_task):
    response, _ = create_task
    created_task = response.json()

    task_id = created_task["id"]
    delete_response = client.delete(f"/tasks/{task_id}")
    deleted_task = delete_response.json()

    assert response.status_code == 201
    assert delete_response.status_code == 200
    assert deleted_task["updated_at"] is not None
