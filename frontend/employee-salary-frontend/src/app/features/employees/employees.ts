import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from './employee.service';
import { Employee, EmployeeRequest } from './employee.model';

@Component({
  selector: 'app-employees',
  imports: [FormsModule],
  templateUrl: './employees.html',
  styleUrl: './employees.css',
})
export class Employees implements OnInit {

  employees: Employee[] = [];

  showAddForm = false;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  keyword = '';

  employeeForm: EmployeeRequest = {
    employeeCode: '',
    firstName: '',
    lastName: '',
    email: '',
    country: '',
    department: '',
    jobTitle: '',
    employmentStatus: 'ACTIVE'
  };

  constructor(
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('Employees component loaded');
    this.loadEmployees();
  }

 loadEmployees(): void {
  this.employeeService
    .getEmployees(this.currentPage, this.pageSize, this.keyword)
    .subscribe({
      next: (response) => {
        this.employees = response.content;
        this.totalPages = response.totalPages;

        console.log('Employees loaded:', this.employees);

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Failed to load employees', error);
      }
    });
}

  createEmployee(): void {
    this.employeeService.createEmployee(this.employeeForm).subscribe({
      next: (employee) => {
        console.log('Employee created:', employee);

        this.showAddForm = false;

        this.employeeForm = {
          employeeCode: '',
          firstName: '',
          lastName: '',
          email: '',
          country: '',
          department: '',
          jobTitle: '',
          employmentStatus: 'ACTIVE'
        };

        this.loadEmployees();
      },
      error: (error) => {
        console.error('Failed to create employee', error);
      }
    });
  }

 
  viewEmployee(employee: Employee): void {
  this.router.navigate(['/employees', employee.employeeId]);
}

editEmployee(employee: Employee): void {
  this.router.navigate(['/employees', employee.employeeId, 'edit']);
}
deleteEmployee(employee: Employee): void {
  const confirmed = confirm(
    `Are you sure you want to delete ${employee.firstName} ${employee.lastName}?`
  );

  if (!confirmed) {
    return;
  }

  this.employeeService.deleteEmployee(employee.employeeId).subscribe({
    next: () => {
      console.log('Employee deleted:', employee.employeeId);

      this.loadEmployees();
    },
    error: (error) => {
      console.error('Failed to delete employee', error);
    }
  });
}
searchEmployees(): void {
  this.currentPage = 0;
  this.loadEmployees();
}

nextPage(): void {
  if (this.currentPage < this.totalPages - 1) {
    this.currentPage++;
    this.loadEmployees();
  }
}

previousPage(): void {
  if (this.currentPage > 0) {
    this.currentPage--;
    this.loadEmployees();
  }
}

}