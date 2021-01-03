package be.ucll.java.ent.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Abstract class grouping all named queries
 */
@NamedQueries({
        @NamedQuery(name = "Student.getAll", query = "SELECT e FROM StudentEntity e ORDER BY e.id"),
        @NamedQuery(name = "Student.countAll", query = "SELECT count(e) FROM StudentEntity e"),
        @NamedQuery(name = "Leermodule.getAll", query = "SELECT e FROM LeermoduleEntity e ORDER BY e.id"),
        @NamedQuery(name = "Leermodule.countAll", query = "SELECT count(e) FROM LeermoduleEntity e")
})
@MappedSuperclass
public abstract class StubsEntity {
}
