package com.fotius.client.model;

import com.fotius.shared.model.Student;
import com.fotius.shared.model.StudentGroup;
import com.fotius.shared.model.StudentRole;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface StudentGroupProperties extends PropertyAccess<StudentGroup> {
    ModelKeyProvider<StudentGroup> groupId();
    ValueProvider<StudentGroup, String> name();

    @Editor.Path("name")
    LabelProvider<StudentGroup> nameLabel();
}
