package com.acme.employeesalary.controller;

import com.acme.employeesalary.dto.SalaryRequest;
import com.acme.employeesalary.dto.SalaryResponse;
import com.acme.employeesalary.exception.GlobalExceptionHandler;
import com.acme.employeesalary.exception.ResourceNotFoundException;
import com.acme.employeesalary.service.SalaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalaryController.class)
@Import(GlobalExceptionHandler.class)
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SalaryService salaryService;

    @Test
    void shouldCreateSalarySuccessfully() throws Exception {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("75000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        SalaryResponse response = new SalaryResponse();

        response.setSalaryId(1L);
        response.setEmployeeId(1L);
        response.setAmount(new BigDecimal("75000.00"));
        response.setCurrency("INR");
        response.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        when(salaryService.saveSalary(any(SalaryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/salaries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salaryId").value(1))
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.amount").value(75000.00))
                .andExpect(jsonPath("$.currency").value("INR"));
    }

    @Test
    void shouldGetSalaryByIdSuccessfully() throws Exception {

        SalaryResponse response = new SalaryResponse();

        response.setSalaryId(1L);
        response.setEmployeeId(1L);
        response.setAmount(new BigDecimal("75000.00"));
        response.setCurrency("INR");
        response.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        when(salaryService.findSalaryById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/salaries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salaryId").value(1))
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.currency").value("INR"));
    }

    @Test
    void shouldReturnNotFoundWhenSalaryDoesNotExist() throws Exception {

        when(salaryService.findSalaryById(999L))
                .thenThrow(new ResourceNotFoundException(
                        "Salary not found with id: 999"
                ));

        mockMvc.perform(get("/api/salaries/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Salary not found with id: 999"));
    }

    @Test
    void shouldGetAllSalariesSuccessfully() throws Exception {

        SalaryResponse response = new SalaryResponse();

        response.setSalaryId(1L);
        response.setEmployeeId(1L);
        response.setAmount(new BigDecimal("75000.00"));
        response.setCurrency("INR");
        response.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        when(salaryService.findAllSalaries())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/salaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salaryId").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[0].currency").value("INR"));
    }

    @Test
    void shouldGetSalariesByEmployeeIdSuccessfully() throws Exception {

        SalaryResponse response = new SalaryResponse();

        response.setSalaryId(1L);
        response.setEmployeeId(1L);
        response.setAmount(new BigDecimal("75000.00"));
        response.setCurrency("INR");
        response.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        when(salaryService.findSalariesByEmployeeId(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/salaries/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salaryId").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(1));
    }

    @Test
    void shouldUpdateSalarySuccessfully() throws Exception {

        SalaryRequest request = new SalaryRequest();

        request.setEmployeeId(1L);
        request.setAmount(new BigDecimal("80000.00"));
        request.setCurrency("INR");
        request.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        SalaryResponse response = new SalaryResponse();

        response.setSalaryId(1L);
        response.setEmployeeId(1L);
        response.setAmount(new BigDecimal("80000.00"));
        response.setCurrency("INR");
        response.setEffectiveFrom(LocalDate.of(2026, 1, 1));

        when(salaryService.updateSalary(
                org.mockito.ArgumentMatchers.eq(1L),
                any(SalaryRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/salaries/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salaryId").value(1))
                .andExpect(jsonPath("$.amount").value(80000.00));
    }

    @Test
    void shouldDeleteSalarySuccessfully() throws Exception {

        doNothing().when(salaryService).deleteSalary(1L);

        mockMvc.perform(delete("/api/salaries/1"))
                .andExpect(status().isOk());
    }
}


