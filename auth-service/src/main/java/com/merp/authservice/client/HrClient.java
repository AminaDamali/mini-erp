package com.merp.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.merp.authservice.dto.CreateEmployeeInternalRequest;
import com.merp.authservice.dto.EmployeeResponse;

@FeignClient(name = "hr-service")
public interface HrClient {
    
    @PostMapping("/api/employees/internal")
    EmployeeResponse createEmployee(@RequestBody CreateEmployeeInternalRequest request);
}
