package com.merp.hrservice.service;

import com.merp.hrservice.dto.CreateDepartmentRequest;
import com.merp.hrservice.dto.DepartmentResponse;
import com.merp.hrservice.dto.DepartmentUpdateRequest;
import com.merp.hrservice.entity.Department;
import com.merp.hrservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    public DepartmentResponse createDepartment (CreateDepartmentRequest request)
    {
      Department dep =  new Department();

      dep.setName(request.getName());
      dep.setCity(request.getCity());
      Department saved = departmentRepository.save(dep);
      System.out.println(" department saved"+ saved.getName() +saved.getId());
      return new DepartmentResponse(
              saved.getId(),
              saved.getName(),
              saved.getCity()
      );

    }
    public DepartmentResponse updateDepartment(long id, DepartmentUpdateRequest request)
    {
        Department dep = departmentRepository.findById(id).orElseThrow(()-> new RuntimeException("Department not found"));
        dep.setName(request.getName());
        dep.setCity(request.getCity());
        Department updated = departmentRepository.save(dep);
        return new DepartmentResponse(
                updated.getId(),
                updated.getName(),
                updated.getCity()
        );
    }


}
