package com.acme.employeesalary.controller;

import com.acme.employeesalary.dto.EmployeeRequest;
import com.acme.employeesalary.dto.EmployeeResponse;
import com.acme.employeesalary.exception.GlobalExceptionHandler;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.service.EmployeeService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployeeSuccessfully() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeCode("EMP200");
        request.setFirstName("Ravi");
        request.setLastName("Kumar");
        request.setEmail("ravi.kumar@example.com");
        request.setCountry("IN");
        request.setDepartment("Engineering");
        request.setJobTitle("Java Developer");
        request.setEmploymentStatus("ACTIVE");

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId(200L);
        response.setEmployeeCode("EMP200");
        response.setFirstName("Ravi");
        response.setLastName("Kumar");
        response.setEmail("ravi.kumar@example.com");
        response.setCountry("IN");
        response.setDepartment("Engineering");
        response.setJobTitle("Java Developer");
        response.setEmploymentStatus("ACTIVE");

        when(employeeService.saveEmployee(any(EmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(200))
                .andExpect(jsonPath("$.employeeCode").value("EMP200"))
                .andExpect(jsonPath("$.firstName").value("Ravi"))
                .andExpect(jsonPath("$.email")
                        .value("ravi.kumar@example.com"));
    }

    @Test
    void shouldRejectInvalidEmployeeRequest() throws Exception {

        EmployeeRequest request = new EmployeeRequest();

        request.setEmployeeCode("");
        request.setFirstName("");
        request.setLastName("");
        request.setEmail("invalid-email");
        request.setCountry("I");
        request.setDepartment("");
        request.setJobTitle("");
        request.setEmploymentStatus("");

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation Failed"));
    }

    @Test
    void shouldGetEmployeeByIdSuccessfully() throws Exception {

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId(1L);
        response.setEmployeeCode("EMP001");
        response.setFirstName("Amit");
        response.setLastName("Verma");
        response.setEmail("amit.verma@example.com");
        response.setCountry("IN");
        response.setDepartment("Engineering");
        response.setJobTitle("Backend Developer");
        response.setEmploymentStatus("ACTIVE");

        when(employeeService.findEmployeeById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andExpect(jsonPath("$.firstName").value("Amit"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {

        when(employeeService.findEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "Employee not found with id: 999"
                ));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Employee not found with id: 999"));
    }

    @Test
    void shouldGetEmployeesWithPagination() throws Exception {

        EmployeeResponse response = new EmployeeResponse();

        response.setEmployeeId(1L);
        response.setEmployeeCode("EMP001");
        response.setFirstName("Amit");
        response.setLastName("Verma");
        response.setEmail("amit.verma@example.com");
        response.setCountry("IN");
        response.setDepartment("Engineering");
        response.setJobTitle("Backend Developer");
        response.setEmploymentStatus("ACTIVE");

        Page<EmployeeResponse> page =
                new PageImpl<>(List.of(response));

        when(employeeService.findAllEmployees(0, 10, null))
                .thenReturn(page);

        mockMvc.perform(
                        get("/api/employees")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].employeeId").value(1))
                .andExpect(jsonPath("$.content[0].employeeCode")
                        .value("EMP001"));
    }

    @Test
    void shouldRejectNegativePageNumber() throws Exception {

        mockMvc.perform(
                        get("/api/employees")
                                .param("page", "-1")
                                .param("size", "10")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void shouldRejectInvalidPageSize() throws Exception {

        mockMvc.perform(
                        get("/api/employees")
                                .param("page", "0")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }
}