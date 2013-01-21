package com.fotius.client.model;

import com.fotius.shared.model.TeacherRole;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TeacherRoleProperties extends PropertyAccess<TeacherRole> {
    ModelKeyProvider<TeacherRole> roleId();
    LabelProvider<TeacherRole> name();
}
