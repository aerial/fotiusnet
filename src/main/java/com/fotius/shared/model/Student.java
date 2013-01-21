package com.fotius.shared.model;

import net.sf.gilead.pojo.gwt.LightEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "students")
@SequenceGenerator(name = "students_sequence", sequenceName = "students_sequence", allocationSize = 1)
public class Student  extends LightEntity implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "students_sequence")
    @Column(name = "student_id")
    private Long studentId;
    private String name, login, password;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "roleid")
    private StudentRole studentRole;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "groupid")
    private StudentGroup studentGroup;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long id) {
        this.studentId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentRole getRole() {
        return studentRole;
    }

    public void setRole(StudentRole role) {
        this.studentRole = role;
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }
}
