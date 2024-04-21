package com.ams.campusconnect.controller;

import android.content.Context;

import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.EmployeeRepository;

public class EmployeeController {
    private EmployeeRepository employeeRepository;
    private Context context;

    public EmployeeController(Context context, School school){
        this.context = context;
        this.employeeRepository = new EmployeeRepository(school.getSchoolID());
    }

    public void addEmployee(School school, EmployeeModel employeeModel){
        employeeRepository.addEmployee(school, employeeModel);
    }

    public void getEmployee(School school, String employeeID, final EmployeeRepository.OnDataFetchListener listener){
        employeeRepository.getEmployee(school, employeeID, listener);
    }

    public void getAllEmployees(School school, final EmployeeRepository.OnDataFetchListener listener){
        employeeRepository.getAllEmployees(school, listener);
    }

    public void updateEmployee(School school, EmployeeModel employeeModel){
        employeeRepository.updateEmployee(school, employeeModel);
    }

    public void deleteEmployee(School school, EmployeeModel employeeModel){
        employeeRepository.deleteEmployee(school, employeeModel);
    }
}
