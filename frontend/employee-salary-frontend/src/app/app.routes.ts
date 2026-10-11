import { Routes } from '@angular/router';
import { Employees } from './features/employees/employees';
import { EmployeeDetails } from './features/employees/employee-details/employee-details';
import { EmployeeEdit } from './features/employees/employee-edit/employee-edit';

export const routes: Routes = [
  {
    path: 'employees',
    component: Employees
  },
  {
    path: 'employees/:id',
    component: EmployeeDetails
  },
  {
  path: 'employees/:id/edit',
  component: EmployeeEdit
}
];