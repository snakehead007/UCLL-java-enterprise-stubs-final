package be.ucll.java.ent.rest;

import be.ucll.java.ent.domain.StudentDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentsList implements Serializable {

    private List<StudentDTO> students;

    public StudentsList() {
        students = new ArrayList<>();
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
    public List<StudentDTO> getStudents() {
        return students;
    }

}

