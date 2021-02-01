package be.ucll.java.ent.rest;

import be.ucll.java.ent.controller.StudentController;
import be.ucll.java.ent.domain.StudentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class StudentRestController {
    private Logger logger = LoggerFactory.getLogger(StudentRestController.class);

    @Autowired
    private StudentController studentenMngr;

    // URL requires an ID => http://<server and port>/stubs/rest/v1/student?name=<a (part of) a student name>
    @GetMapping(value = "/v1/student")
    public ResponseEntity getStudent(@RequestParam(value = "name", defaultValue = "") String name) {

        try {
            logger.debug("REST service input param name " + name);

            List<StudentDTO> students = studentenMngr.getStudentsByName(name);
            if (students != null && students.size() > 0) {
                StudentsList sl = new StudentsList();
                sl.setStudents(students);

                return new ResponseEntity<StudentsList>(sl, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("No students found for name search term", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("An error has occurred", HttpStatus.BAD_REQUEST);
        }
    }

    // URL requires an ID => http://<server and port>/stubs/rest/v1/student/{a (part of) a student name}
    @GetMapping(value = "/v1/student/{name}")
    public ResponseEntity getStudent2(@PathVariable String name) {

        try {
            List<StudentDTO> students = studentenMngr.getStudentsByName(name);
            if (students != null && students.size() > 0) {
                StudentsList sl = new StudentsList();
                sl.setStudents(students);

                return new ResponseEntity<StudentsList>(sl, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("No students found for name search term", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("An error has occurred", HttpStatus.BAD_REQUEST);
        }
    }

}

