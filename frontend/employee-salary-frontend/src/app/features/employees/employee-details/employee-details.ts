import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';
import { EmployeeService } from '../employee.service';
import { Employee } from '../employee.model';

@Component({
  selector: 'app-employee-details',
  imports: [],
  templateUrl: './employee-details.html',
  styleUrl: './employee-details.css',
})
export class EmployeeDetails implements OnInit {

  employee: Employee | null = null;

  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const employeeId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.employeeService.getEmployeeById(employeeId).subscribe({
      next: (employee) => {
  this.employee = employee;

  console.log('Employee details loaded:', employee);

  this.cdr.detectChanges();
},
      error: (error) => {
        console.error('Failed to load employee details', error);
      }
    });
  }
}