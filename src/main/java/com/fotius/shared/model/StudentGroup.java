package com.fotius.shared.model;

import com.google.gwt.user.client.rpc.GwtTransient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "student_groups")
@SequenceGenerator(name = "student_groups_sequence", sequenceName = "student_groups_sequence", allocationSize = 1)
public class StudentGroup implements Serializable  {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "student_groups_sequence")
    @Column(name = "groupid")
    private Long groupId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER)

    @JoinColumn(name = "groupid")
    @GwtTransient
    private List<Student> students;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
