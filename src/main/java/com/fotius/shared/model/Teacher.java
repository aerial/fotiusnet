package com.fotius.shared.model;

import net.sf.gilead.pojo.gwt.LightEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "teachers")
@SequenceGenerator(name = "teachers_sequence", sequenceName = "teachers_sequence", allocationSize = 1)
public class Teacher extends LightEntity implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "teachers_sequence")
    @Column(name = "teacher_id")
    private Long teacherId;
    private String name, login, password;


    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "roleid")
    private TeacherRole teacherRole;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long id) {
        this.teacherId = id;
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


    public TeacherRole getRole() {
        return teacherRole;
    }

    public void setRole(TeacherRole role) {
        this.teacherRole = role;
    }
}
