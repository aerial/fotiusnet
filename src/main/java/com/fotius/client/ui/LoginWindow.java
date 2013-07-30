package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.Session;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public abstract class LoginWindow extends Window {
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    protected TextField userNameTxt;
    protected PasswordField passwordTxt;
    protected TextButton resetBtn, loginBtn;
    protected Status status;

    public LoginWindow() {
        getHeader().setIcon(Resources.IMAGES.lock());
        setHeadingText("Please just fucking log in");
        setModal(true);
        setBodyStyle("background: none");
        setWidth(300);
        setResizable(false);
        FieldSet fieldSet = new FieldSet();
        add(fieldSet);
        VerticalLayoutContainer p = new VerticalLayoutContainer();
        fieldSet.add(p);
        userNameTxt = new TextField();
        userNameTxt.setTitle("Username");
        userNameTxt.setAllowBlank(false);
        userNameTxt.setText("aerial");
        p.add(new FieldLabel(userNameTxt, "Login here"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        passwordTxt = new PasswordField();
        passwordTxt.setTitle("Password");
        passwordTxt.setAllowBlank(false);
        passwordTxt.setText("aerial");
        p.add(new FieldLabel(passwordTxt, "Password here"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        setFocusWidget(userNameTxt);

        status = new Status();
        status.setBusy("checking...");
        status.hide();
        getButtonBar().add(status);
        addButton(getLoginBtn());
    }

    public abstract void onLoginSuccess(User user);

    private TextButton getLoginBtn() {
        loginBtn = new TextButton("Login");
        loginBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                onSubmit();
            }
        });
        return loginBtn;
    }
    protected void onSubmit() {
        status.show();
        getButtonBar().disable();
        fotiusService.login(userNameTxt.getText(), passwordTxt.getText(), new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    Session.authorize(result);
                    LoginWindow.this.hide();
                    onLoginSuccess(result);
                } else {
                    new AlertMessageBox("Access denied", "Incorrect login data").show();
                }
                status.hide();
                getButtonBar().enable();
            }
        });
    }
}