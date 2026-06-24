package ec.edu.ups.icc.fundamentos01.controllers;

import ec.edu.ups.icc.fundamentos01.students.controllers.models.Student;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();

    public StudentController() {
        students.add(new Student(1L, "JUAN", 20));
        students.add(new Student(2L, "MARIA", 22));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }
}