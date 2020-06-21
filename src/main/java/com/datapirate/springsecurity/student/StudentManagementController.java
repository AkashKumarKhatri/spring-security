package com.datapirate.springsecurity.student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Akash on 17-Jun-20
 */

@RestController
@RequestMapping("management/api/v1/student")
public class StudentManagementController {

    private static final List<Student> STUDENTS = List.of(
            new Student(1, "Akash"),
            new Student(2, "Kumar"),
            new Student(3, "Khatri"),
            new Student(4, "Vijay"),
            new Student(5, "Maheshwari")
    );

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE', 'ROLE_STUDENT')")
    public List<Student> getAllStudents() {
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println("StudentManagementController.registerNewStudent");
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize(value = "hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println("StudentManagementController.deleteStudent");
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize(value = "hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println("StudentManagementController.updateStudent");
        System.out.println(String.format("%s %s", studentId, student));
    }

}
