package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.model.TeacherRoleProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Student;
import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.TeacherRole;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.*;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

public class EditTeacherWindow extends Window {

    private Teacher currentTeacher;
    private TextField nameTxt, loginTxt;
    private PasswordField passwordTxt;
    private TextButton saveBtn, cancelBtn;
    private ComboBox<TeacherRole> roleCombo;
    private FramedPanel mainPanel;

    private static EditTeacherWindow instance;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    public static EditTeacherWindow getInstance() {
        if (instance == null) {
            instance = new EditTeacherWindow();

        }
        return instance;
    }

    public EditTeacherWindow() {
        getHeader().setIcon(Resources.IMAGES.add16());
        setSize("335", "300");
        setModal(true);
        setHeadingText("Teacher information");
        add(getMainPanel());
        currentTeacher = new Teacher();
    }

    public void fillTeacherData(Teacher teacher) {
        currentTeacher = teacher;
        getNameTxt().setValue(teacher.getName());
        getLoginTxt().setValue(teacher.getLogin());
        getPasswordTxt().setValue(teacher.getPassword());
        getRoleCombo().setValue(teacher.getRole());
    }

    private FramedPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new FramedPanel();
            mainPanel.setHeadingText("Enter the teacher information");
            mainPanel.setBodyStyle("padding: 5px");
            VerticalLayoutContainer p = new VerticalLayoutContainer();
            mainPanel.add(p);
            p.add(new FieldLabel(getNameTxt(), "Name"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getLoginTxt(), "Login"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getPasswordTxt(), "Password"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getRoleCombo(), "Role"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            mainPanel.addButton(getSaveBtn());
            mainPanel.addButton(getCancelBtn());
        }
        return mainPanel;

    }

    private TextField getNameTxt() {
        if (nameTxt == null) {
            nameTxt = new TextField();
            nameTxt.setAllowBlank(false);
        }
        return nameTxt;
    }

    private TextField getLoginTxt() {
        if (loginTxt == null) {
            loginTxt = new TextField();
            loginTxt.setAllowBlank(false);
        }
        return loginTxt;
    }

    private PasswordField getPasswordTxt() {
        if (passwordTxt == null) {
            passwordTxt = new PasswordField();
            passwordTxt.setAllowBlank(false);
        }
        return passwordTxt;
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

    private TextButton getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = new TextButton("Save");
            saveBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    if (validate()) {
                        currentTeacher.setName(getNameTxt().getValue());
                        currentTeacher.setLogin(getLoginTxt().getValue());
                        currentTeacher.setPassword(getPasswordTxt().getValue());
                        currentTeacher.setRole(roleCombo.getValue());
                        fotiusService.saveTeacher(currentTeacher, new AsyncCallback<Teacher>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Info.display("Internal server error", "Can't save teacher");
                            }

                            @Override
                            public void onSuccess(Teacher result) {
                                Info.display("Teacher saved", String.valueOf(result.getTeacherId()));
                                EditTeacherWindow.getInstance().hide();
                                TeachersWindow.getInstance().refresh();
                            }
                        });
                    }
                }
            });
        }
        return saveBtn;
    }

    private TextButton getCancelBtn() {
        if (cancelBtn == null) {
            cancelBtn = new TextButton("Cancel");
            cancelBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditTeacherWindow.getInstance().hide();
                }
            });
        }
        return cancelBtn;
    }

    private boolean validate() {
        boolean valid = true;
        valid &= getNameTxt().isValid();
        valid &= getLoginTxt().isValid();
        valid &= getPasswordTxt().isValid();
        valid &= getRoleCombo().isValid();
        return valid;
    }
}
