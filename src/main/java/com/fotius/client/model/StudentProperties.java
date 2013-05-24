package com.fotius.client.model;

import com.fotius.shared.model.Student;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface StudentProperties extends PropertyAccess<Student> {
    ModelKeyProvider<Student> studentId();
    ValueProvider<Student, String> name();
    ValueProvider<Student, String> login();
    ValueProvider<Student, String> password();
    @Editor.Path("role.name")
    ValueProvider<Student, String> role();
    @Editor.Path("studentGroup.name")
    ValueProvider<Student, String> studentGroup();
}
