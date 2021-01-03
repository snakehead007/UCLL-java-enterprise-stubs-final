package be.ucll.java.ent.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Inschrijving")
public class InschrijvingEntity extends StubsEntity implements Serializable {

    @EmbeddedId
    private InschrijvingId id;

    @Column(name = "betaald")
    private boolean betaald;

    // Meerdere "Inschrijving" zijn mogelijk voor 1 "Student"
    // Foreign Key
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("student_id")
    private StudentEntity student;

    // Meerdere "Inschrijving" zijn mogelijk voor 1 "Leermodule".
    // Foreign Key
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("leermodule_id")
    private LeermoduleEntity leermodule;

    /* ***** Constructors ***** */
    public InschrijvingEntity() {
        // Default constructor
    }

    public InschrijvingEntity(StudentEntity student, LeermoduleEntity leermodule, boolean betaald) {
        this.student = student;
        this.leermodule = leermodule;
        this.betaald = betaald;

        this.id = new InschrijvingId(student.getId(), leermodule.getId());
    }

    /* ***** Getters en Setters ***** */

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public LeermoduleEntity getLeermodule() {
        return leermodule;
    }

    public void setLeermodule(LeermoduleEntity leermodule) {
        this.leermodule = leermodule;
    }

    public boolean isBetaald() {
        return betaald;
    }

    public void setBetaald(boolean betaald) {
        this.betaald = betaald;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InschrijvingEntity that = (InschrijvingEntity) o;
        return betaald == that.betaald &&
                Objects.equals(student, that.student) &&
                Objects.equals(leermodule, that.leermodule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, leermodule, betaald);
    }

    @Override
    public String toString() {
        return "InschrijvingEntity{" +
                "id=" + id +
                ", betaald=" + betaald +
                ", student=" + student +
                ", leermodule=" + leermodule +
                '}';
    }
}
