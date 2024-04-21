import { Component, OnInit } from '@angular/core';
import { Employee } from './employee';
import { EmployeeService } from './employee.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public employees: Employee[];
  public editEmployee: Employee;
  public deleteEmployee: Employee;
  public selectedFile: any;

  constructor(private employeeService: EmployeeService){}

  ngOnInit() {
    this.getEmployees();
  }

  public onFileChanged(event) {
    let me = this;
    let file =  event.target.files[0];
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function() {
      me.selectedFile = reader.result;
    };
    reader.onerror = function (error) {
      console.log('Error', error);
    };



  }

  public getEmployees(): void {
    this.employeeService.getEmployees().subscribe(
      (response: Employee[]) => {
        console.log(response);
        this.employees = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onAddEmployee(addForm: NgForm): void {

    let employee: Employee = addForm.value;
    employee.image = this.selectedFile;
    
    this.employeeService.addEmployee(employee).subscribe(
      (response: Employee) => {
        console.log(response);
        this.getEmployees();
        addForm.reset();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
        addForm.reset();
      }
    );
  }

  public onUpdateEmloyee(employee: Employee): void {
  
    if( this.selectedFile === undefined){
      employee.image = this.editEmployee.image;
    }else{
      employee.image = this.selectedFile;
    }
    
    this.employeeService.updateEmployee(employee).subscribe(
      (response: Employee) => {
        console.log(response);
        this.getEmployees();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onDeleteEmloyee(employeeId: number): void {
    console.log("id: "+ employeeId);
    this.employeeService.deleteEmployee(employeeId).subscribe(
      (response: void) => {
        console.log(response);
        this.getEmployees();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public searchEmployees(key: string): void {
    console.log(key);
    const results: Employee[] = [];
    for (const employee of this.employees) {
      if (employee.name.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || employee.email.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || employee.phone.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || employee.job_title.toLowerCase().indexOf(key.toLowerCase()) !== -1) {
        results.push(employee);
      }
    }
    this.employees = results;
    if (results.length === 0 || !key) {
      this.getEmployees();
    }
  }


  public onOpenModal(employee: Employee, mode: string): void {

    if (mode === 'edit') {
      console.log('Edit');
      this.editEmployee = employee;
    }
    if (mode === 'delete') {
      console.log('Delete');
      this.deleteEmployee = employee;
    }
  
  }
}
