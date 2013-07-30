package com.fotius.client.model;

import com.fotius.shared.model.User;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface UserProperties extends PropertyAccess<User> {
    ModelKeyProvider<User> userId();
    LabelProvider<User> name();
}

