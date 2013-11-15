package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.Session;
import com.fotius.client.model.TeacherRoleProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.TeacherRole;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LoginWindow extends Window {
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private static Logger log = Logger.getLogger("");

    protected TextField loginUserNameTxt;
    protected PasswordField loginPasswordTxt;

    protected TextField regUserNameTxt, regNameTxt;
    protected PasswordField regPasswordTxt;


    protected TextButton resetBtn, loginBtn;
    protected Status status;
    private ComboBox<TeacherRole> roleCombo;


    public LoginWindow() {
        getHeader().setIcon(Resources.IMAGES.lock());
        setHeadingText("Please just fucking log in");
        setModal(true);
        setBodyStyle("background: none");
        setWidth(300);
        setResizable(false);

        VerticalLayoutContainer p = new VerticalLayoutContainer();
        loginUserNameTxt = new TextField();
        loginUserNameTxt.setTitle("Username");
        loginUserNameTxt.setAllowBlank(false);
        loginUserNameTxt.setText("aerial");
        p.add(new FieldLabel(loginUserNameTxt, "Login"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        loginPasswordTxt = new PasswordField();
        loginPasswordTxt.setTitle("Password");
        loginPasswordTxt.setAllowBlank(false);
        loginPasswordTxt.setText("aerial");
        p.add(new FieldLabel(loginPasswordTxt, "Password"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        setFocusWidget(loginUserNameTxt);

        status = new Status();
        status.setBusy("checking...");
        status.hide();
        getButtonBar().add(status);
//        addButton(getLoginBtn());

        VerticalLayoutContainer p2 = new VerticalLayoutContainer();
        regNameTxt = new TextField();
        regNameTxt.setTitle("Name");
        regNameTxt.setAllowBlank(false);
        regNameTxt.setText("");
        p2.add(new FieldLabel(regNameTxt, "Name"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        regUserNameTxt = new TextField();
        regUserNameTxt.setTitle("Username");
        regUserNameTxt.setAllowBlank(false);
        regUserNameTxt.setText("");
        p2.add(new FieldLabel(regUserNameTxt, "Login"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        regPasswordTxt = new PasswordField();
        regPasswordTxt.setTitle("Password");
        regPasswordTxt.setAllowBlank(false);
        regPasswordTxt.setText("");
        p2.add(new FieldLabel(regPasswordTxt, "Password"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        p2.add(new FieldLabel(getRoleCombo(), "Role"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        setFocusWidget(regUserNameTxt);
        p.add(getLoginBtn());
        p2.add(getRegisterBtn());

        TabPanel tabs = new TabPanel();
        tabs.add(p, "Login");
        tabs.add(p2, "Register");
        add(tabs);
    }

    public abstract void onLoginSuccess(User user);

    private TextButton getLoginBtn() {
        loginBtn = new TextButton("Login");
        loginBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                log.info("Logging in ...");
                onSubmit(loginUserNameTxt.getText(), loginPasswordTxt.getText());
            }
        });
        return loginBtn;
    }


    private ComboBox<TeacherRole> getRoleCombo() {
        if (roleCombo == null) {
            TeacherRoleProperties props = GWT.create(TeacherRoleProperties.class);

            RpcProxy<PagingLoadConfig, PagingLoadResult<TeacherRole>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<TeacherRole>>() {
                @Override
                public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<TeacherRole>> callback) {
                    fotiusService.getTeacherRoles(loadConfig, callback);
                }
            };

            ListStore<TeacherRole> store = new ListStore<TeacherRole>(new ModelKeyProvider<TeacherRole>() {
                @Override
                public String getKey(TeacherRole teacherRole) {
                    return "" + teacherRole.getRoleId();
                }
            });
            final PagingLoader<PagingLoadConfig, PagingLoadResult<TeacherRole>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<TeacherRole>>(
                    proxy);
            loader.setRemoteSort(true);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, TeacherRole, PagingLoadResult<TeacherRole>>(store));
            loader.load();

            roleCombo = new ComboBox<TeacherRole>(store, props.name());
            roleCombo.setLoader(loader);
            roleCombo.setAllowBlank(false);
        }
        return roleCombo;
    }

    private TextButton getRegisterBtn() {
        loginBtn = new TextButton("Register");
        loginBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {

                log.info("Registering ...");

                Teacher teacher = new Teacher();
                teacher.setLogin(regUserNameTxt.getText());
                teacher.setPassword(regPasswordTxt.getText());
                teacher.setName(regNameTxt.getText());
                teacher.setTeacherRole(getRoleCombo().getCurrentValue());
                fotiusService.saveTeacher(teacher, new AsyncCallback<Teacher>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(Teacher user) {

                        onSubmit(user.getLogin(), user.getPassword());
                    }
                });



                LoginWindow.this.hide();

            }
        });
        return loginBtn;
    }

    protected void onSubmit(String login, String password) {
        status.show();
        getButtonBar().disable();
        fotiusService.login(login, password, new AsyncCallback<User>() {
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