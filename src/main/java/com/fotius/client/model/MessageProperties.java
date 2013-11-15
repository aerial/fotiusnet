package com.fotius.client.model;

import com.fotius.shared.model.Message;
import com.fotius.shared.model.StudentGroup;
import com.fotius.shared.model.Teacher;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import java.util.Date;

public interface MessageProperties extends PropertyAccess<Message> {
    ModelKeyProvider<Message> messageId();
    ValueProvider<Message, String> text();
    ValueProvider<Message, Date> date();
    @Editor.Path("sender.name")
    ValueProvider<Message, String> senderName();
}
