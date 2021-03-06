package com.fotius.shared.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student_roles")
public class StudentRole implements UserRole, Serializable {
    @Id
    @Column(name = "roleid")
    private Long roleId;

    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long id) {
        this.roleId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
