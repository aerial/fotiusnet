package com.fotius.client.model;

import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.TeacherRole;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TeacherProperties extends PropertyAccess<Teacher> {
    ModelKeyProvider<Teacher> teacherId();
    ValueProvider<Teacher, String> name();
    ValueProvider<Teacher, String> login();
    ValueProvider<Teacher, String> password();
    @Editor.Path("role.name")
    ValueProvider<Teacher, String> role();
}
