package com.fotius.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User implements Serializable {

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "roleid")
    private StudentRole studentRole;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "groupid")
    private StudentGroup studentGroup;

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
