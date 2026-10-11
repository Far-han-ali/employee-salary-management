import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { EmployeeService } from '../employee.service';
import { Employee, EmployeeRequest } from '../employee.model';

@Component({
  selector: 'app-employee-edit',
  imports: [FormsModule],
  templateUrl: './employee-edit.html',
  styleUrl: './employee-edit.css',
})
export class EmployeeEdit implements OnInit {

  employeeId!: number;

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
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.employeeId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.loadEmployee();
  }

  loadEmployee(): void {
    this.employeeService.getEmployeeById(this.employeeId).subscribe({
      next: (employee: Employee) => {

        this.employeeForm = {
          employeeCode: employee.employeeCode,
          firstName: employee.firstName,
          lastName: employee.lastName,
          email: employee.email,
          country: employee.country,
          department: employee.department,
          jobTitle: employee.jobTitle,
          employmentStatus: employee.employmentStatus
        };

        console.log('Employee loaded for editing:', employee);

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Failed to load employee for editing', error);
      }
    });
  }

  updateEmployee(): void {
    this.employeeService
      .updateEmployee(this.employeeId, this.employeeForm)
      .subscribe({
        next: (employee) => {
          console.log('Employee updated:', employee);

          this.router.navigate(['/employees']);
        },
        error: (error) => {
          console.error('Failed to update employee', error);
        }
      });
  }

  cancel(): void {
    this.router.navigate(['/employees']);
  }
}