package com.fotius.client.model;

import com.fotius.shared.model.StudentRole;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface StudentRoleProperties extends PropertyAccess<StudentRole> {
    ModelKeyProvider<StudentRole> roleId();
    LabelProvider<StudentRole> name();
}

