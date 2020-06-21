package com.datapirate.springsecurity.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Akash on 16-Jun-20
 */
@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private static final List<Student> STUDENTS = List.of(
            new Student(1, "Akash"),
            new Student(2, "Kumar"),
            new Student(3, "Khatri"),
            new Student(4, "Vijay"),
            new Student(5, "Maheshwari")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId) {
        return STUDENTS.stream()
                .filter(student -> studentId.equals(student.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student " + studentId + " does not exist"));
    }

}
