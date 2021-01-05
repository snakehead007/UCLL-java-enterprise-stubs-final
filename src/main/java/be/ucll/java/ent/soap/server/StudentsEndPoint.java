package be.ucll.java.ent.soap.server;

import be.ucll.java.ent.controller.StudentController;
import be.ucll.java.ent.domain.StudentDTO;
import be.ucll.java.ent.soap.model.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
public class StudentsEndPoint {
    private final Logger logger = LoggerFactory.getLogger(StudentsEndPoint.class);

    @Autowired
    private StudentController studentenMngr;

    @PayloadRoot(namespace = "http://ucll.be/java/ent/students", localPart = "GetStudentsRequest")
    @ResponsePayload
    public GetStudentsResponse getStudents(@RequestPayload GetStudentsRequest request) {
        GetStudentsResponse response = new GetStudentsResponse();
        try {
            List<StudentDTO> students = new ArrayList<>();
            // 1. Execute the search
            if (request.getId() != null && request.getId() > 0L) {
                StudentDTO stud = studentenMngr.getStudentById(request.getId());
                students.add(stud);
            } else {
                students = studentenMngr.getStudentsByName(request.getName());
            }

            // 2. Process the results found
            if (students != null && students.size() > 0) {
                CTypeStudents studs = new CTypeStudents();
                for (StudentDTO stud : students) {
                    CTypeStudent s = new CTypeStudent();
                    s.setNaam(stud.getNaam());
                    s.setVoornaam(stud.getVoornaam());
                    //s.setGeboortedatum(stud.getGeboortedatum());

                    GregorianCalendar c = new GregorianCalendar();
                    c.setTime(stud.getGeboortedatum());
                    XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

                    s.setGeboortedatum(date2);
                    studs.getStudent().add(s);
                }
                response.setStudents(studs);
                response.setCode(0);
                response.setType(STypeProcessOutcome.INFO);
            } else {
                response.setCode(1);
                response.setType(STypeProcessOutcome.WARNING);
                response.setErrormessage("Geen student(en) gevonden voor naam of id");
            }
        } catch (CannotCreateTransactionException cctex){
            response.setCode(1);
            response.setType(STypeProcessOutcome.ERROR);
            response.setErrormessage("Bepaalde randsystemen zijn onbereikbaar. Probeer later opnieuw");
            logger.error("Database offline or unreachable", cctex);
        } catch (Exception ex) {
            response.setCode(1);
            response.setType(STypeProcessOutcome.ERROR);
            response.setErrormessage("Algemene fout. Er is geen verdere detailinfo voorhanden.");
            logger.error("Unexpected error while processing soap request", ex);
        }
        return response;
    }
}
