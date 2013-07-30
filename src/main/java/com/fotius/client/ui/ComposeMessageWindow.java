package com.fotius.client.ui;


import com.fotius.client.Resources;
import com.fotius.client.model.StudentGroupProperties;
import com.fotius.client.model.UserProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.List;

public class ComposeMessageWindow extends Window {

    private static ComposeMessageWindow instance = null;
    private final UserProperties props = GWT.create(UserProperties.class);
    private static final String WIDTH = "500px";
    private static final String HEIGHT = "400px";
    private ComboBox<User> recipientCombo;
    private TextArea messageTxt;
    private static ListStore<User> users;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    public static ComposeMessageWindow getInstance() {
        if (instance == null) {
            instance = new ComposeMessageWindow("Compose message", Resources.IMAGES.group_small());
        }
        return instance;
    }


    public ComposeMessageWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setSize(WIDTH, HEIGHT);
        setMaximizable(true);
        setMinimizable(true);
        initUI();
    }

    private void initUI() {
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(true);
        mainPanel.setWidget(getMessagePanel());
        add(mainPanel);
    }

    private VerticalLayoutContainer getMessagePanel() {
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);

        con.add(getRecipientCombo(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(getMessageTxt(), new VerticalLayoutContainer.VerticalLayoutData(1, 1));


        return con;
    }

    private ComboBox<User> getRecipientCombo() {
        if (recipientCombo == null) {
            recipientCombo = new ComboBox<User>(users, props.name());
            recipientCombo.setTypeAhead(true);
            recipientCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
            fotiusService.getUsers(new AsyncCallback<List<User>>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.display("Users got", "");
                }

                @Override
                public void onSuccess(List<User> result) {
                    users.addAll(result);

                }
            });


        }
        return recipientCombo;
    }

    private TextArea getMessageTxt() {
        if (messageTxt == null) {
            messageTxt = new TextArea();

        }
        return messageTxt;
    }



}
