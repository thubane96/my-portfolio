import pytest
from fastapi.testclient import TestClient
from pydantic_factories import ModelFactory


from app.main import app
from app.schemas.employee import EmployeeCreateIn
from app.schemas.task import TaskCreateIn


class EmployeeFactory(ModelFactory):
    __model__ = EmployeeCreateIn

    is_active = True

class TaskFactory(ModelFactory):
    __model__ = TaskCreateIn



@pytest.fixture(scope="session")
def client():
    with TestClient(app) as client:
        yield client



@pytest.fixture(scope="session")
def create_employee(client):
    employee = EmployeeFactory.build()

    response = client.post("/employees", json=employee.dict())


    return response, employee


@pytest.fixture(scope="session")
def create_task(client, create_employee):
    response, _ = create_employee
    task = TaskFactory.build()
    task.employee_id = response.json()["id"]

    response = client.post("/tasks", json=task.dict())

    return response, task