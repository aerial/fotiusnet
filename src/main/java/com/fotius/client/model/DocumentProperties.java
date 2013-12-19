package com.fotius.client.model;

import com.fotius.shared.model.Document;
import com.fotius.shared.model.Message;
import com.fotius.shared.model.User;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DocumentProperties extends PropertyAccess<Document> {
    ModelKeyProvider<Document> documentId();
    ValueProvider<Document, String> name();
    ValueProvider<Document, Integer> size();
}

