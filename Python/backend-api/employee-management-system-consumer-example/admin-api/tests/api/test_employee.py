def test_create_employee(create_employee):
    response, employee = create_employee
    created_employee = response.json()

    assert response.status_code == 201
    assert created_employee["name"] == employee.name
    assert created_employee["position"] == employee.position
    assert created_employee["is_active"] == employee.is_active
    assert created_employee["salary"] == employee.salary
    assert created_employee["age"] == employee.age


def test_get_employees(client):
    response = client.get(f"/employees")

    assert response.status_code == 200
    assert len(response.json()) > 0


def test_get_employee(client, create_employee):
    response , employee = create_employee
    created_employee = response.json()

    employee_id = created_employee["id"]
    get_response = client.get(f"/employees/{employee_id}")
    fetched_employee = get_response.json()

    assert response.status_code == 201
    assert get_response.status_code == 200
    assert fetched_employee["name"] == employee.name
    assert fetched_employee["position"] == employee.position
    assert fetched_employee["is_active"] == employee.is_active
    assert fetched_employee["salary"] == employee.salary
    assert fetched_employee["age"] == employee.age



def test_update_employee(client, create_employee):
    response, employee = create_employee
    created_employee = response.json()
    employee.name = "Jack"

    employee_id = created_employee["id"]
    update_response = client.put(f"/employees/{employee_id}", json={"name":employee.name})
    updated_employee = update_response.json()

    assert response.status_code == 201
    assert update_response.status_code == 200
    assert updated_employee["name"] == employee.name
    assert updated_employee["is_active"] == employee.is_active
    assert updated_employee["updated_at"] is not None


def test_delete_employee(client, create_employee):
    response, _ = create_employee
    created_employee = response.json()

    print(created_employee)
    employee_id = created_employee["id"]
    delete_response = client.delete(f"/employees/{employee_id}")
    deleted_employee = delete_response.json()

    assert response.status_code == 201
    assert delete_response.status_code == 200
    assert created_employee["id"] == deleted_employee["id"]
    assert created_employee["created_at"] == deleted_employee["created_at"]
    assert deleted_employee["updated_at"] is not None
    assert deleted_employee["is_active"] == False




