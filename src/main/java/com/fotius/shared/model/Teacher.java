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

    public TeacherRole getRole() {
        return teacherRole;
    }

    public void setRole(TeacherRole role) {
        this.teacherRole = role;
    }
}
