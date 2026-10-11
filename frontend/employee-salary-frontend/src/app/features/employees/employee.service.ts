import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import {Employee, EmployeePage, EmployeeRequest } from './employee.model';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {

  private readonly apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) {
  }

  getEmployees(page: number = 0, size: number = 10, keyword: string = '') {
  return this.http.get<EmployeePage>(this.apiUrl, {
    params: {
      page,
      size,
      keyword
    }
  });
}

createEmployee(request: EmployeeRequest) {
  return this.http.post<Employee>(
    this.apiUrl,
    request
  );
}
getEmployeeById(employeeId: number) {
  return this.http.get<Employee>(
    `${this.apiUrl}/${employeeId}`
  );
}
updateEmployee(employeeId: number, request: EmployeeRequest) {
  return this.http.put<Employee>(
    `${this.apiUrl}/${employeeId}`,
    request
  );
}
deleteEmployee(employeeId: number) {
  return this.http.delete<void>(
    `${this.apiUrl}/${employeeId}`
  );
}

}