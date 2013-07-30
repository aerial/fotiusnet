package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.shared.model.Message;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ViewMessageWindow extends Window {

    private static ViewMessageWindow instance = null;
    private static final String WIDTH = "500px";
    private static final String HEIGHT = "400px";
    private TextField fromTxt;
    private TextArea messageTxt;

    public static ViewMessageWindow getInstance() {
        if (instance == null) {
            instance = new ViewMessageWindow("test", Resources.IMAGES.group_small());
        }
        return instance;
    }

    public ViewMessageWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setSize(WIDTH, HEIGHT);
        setMaximizable(true);
        setMinimizable(true);
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(true);
        mainPanel.setWidget(getMessagePanel());
        add(mainPanel);
    }

    private VerticalLayoutContainer getMessagePanel() {
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getFromTxt(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(getMessageTxt(), new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        return con;
    }


    private TextField getFromTxt() {
        if (fromTxt == null) {
            fromTxt = new TextField();
            fromTxt.setEnabled(false);
        }
        return fromTxt;
    }

    private TextArea getMessageTxt() {
        if (messageTxt == null) {
            messageTxt = new TextArea();
            messageTxt.setEnabled(false);
        }
        return messageTxt;
    }

    public void fillForm(Message msg) {
        getFromTxt().setText(msg.getSender().getName());
        getMessageTxt().setText(msg.getText());
    }



}
