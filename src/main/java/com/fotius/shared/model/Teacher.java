package com.fotius.shared.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User implements Serializable {

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "roleid")
    private TeacherRole teacherRole;

    public TeacherRole getTeacherRole() {
        return teacherRole;
    }

    public void setTeacherRole(TeacherRole role) {
        this.teacherRole = role;
    }
}
