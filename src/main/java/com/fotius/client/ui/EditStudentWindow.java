package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.model.StudentGroupProperties;
import com.fotius.client.model.StudentRoleProperties;
import com.fotius.client.model.TeacherRoleProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Student;
import com.fotius.shared.model.StudentGroup;
import com.fotius.shared.model.StudentRole;
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

public class EditStudentWindow extends Window {

    private Student currentStudent;
    private TextField nameTxt, loginTxt;
    private PasswordField passwordTxt;
    private TextButton saveBtn, cancelBtn;
    private ComboBox<StudentRole> roleCombo;
    private ComboBox<StudentGroup> groupCombo;
    private FramedPanel mainPanel;

    private static EditStudentWindow instance;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    public static EditStudentWindow getInstance() {
        if (instance == null) {
            instance = new EditStudentWindow();
        }
        return instance;
    }

    public EditStudentWindow() {
        getHeader().setIcon(Resources.IMAGES.add16());
        setSize("335", "300");
        setModal(true);
        setHeadingText("Student information");
        add(getMainPanel());

        currentStudent = new Student();
    }

    public void fillStudentData(Student student) {
        currentStudent = student;
        getNameTxt().setValue(student.getName());
        getLoginTxt().setValue(student.getLogin());
        getPasswordTxt().setValue(student.getPassword());
        getRoleCombo().setValue(student.getRole());
        getGroupCombo().setValue(student.getStudentGroup());
    }

    private FramedPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new FramedPanel();
            mainPanel.setHeadingText("Enter the student information");
            mainPanel.setBodyStyle("padding: 5px");
            VerticalLayoutContainer p = new VerticalLayoutContainer();
//            p.setBorders(true);
            mainPanel.add(p);
            p.add(new FieldLabel(getNameTxt(), "Name"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getLoginTxt(), "Login"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getPasswordTxt(), "Password"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getRoleCombo(), "Role"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
            p.add(new FieldLabel(getGroupCombo(), "Group"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
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

    private ComboBox<StudentRole> getRoleCombo() {
        if (roleCombo == null) {
            StudentRoleProperties props = GWT.create(StudentRoleProperties.class);

            RpcProxy<PagingLoadConfig, PagingLoadResult<StudentRole>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<StudentRole>>() {
                @Override
                public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<StudentRole>> callback) {
                    fotiusService.getStudentRoles(loadConfig, callback);
                }
            };

            ListStore<StudentRole> store = new ListStore<StudentRole>(new ModelKeyProvider<StudentRole>() {
                @Override
                public String getKey(StudentRole studentRole) {
                    return "" + studentRole.getRoleId();
                }
            });
            final PagingLoader<PagingLoadConfig, PagingLoadResult<StudentRole>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<StudentRole>>(
                    proxy);
            loader.setRemoteSort(true);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, StudentRole, PagingLoadResult<StudentRole>>(store));
            loader.load();

            roleCombo = new ComboBox<StudentRole>(store, props.name());
            roleCombo.setLoader(loader);
            Info.display(String.valueOf(store.size()), "");
        }
        return roleCombo;
    }

    private ComboBox<StudentGroup> getGroupCombo() {
        if (groupCombo == null) {
            StudentGroupProperties props = GWT.create(StudentGroupProperties.class);

            RpcProxy<PagingLoadConfig, PagingLoadResult<StudentGroup>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<StudentGroup>>() {
                @Override
                public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<StudentGroup>> callback) {
                    fotiusService.getStudentGroups(loadConfig, callback);
                }
            };

            ListStore<StudentGroup> store = new ListStore<StudentGroup>(new ModelKeyProvider<StudentGroup>() {
                @Override
                public String getKey(StudentGroup studentGroup) {
                    return "" + studentGroup.getGroupId();
                }
            });
            final PagingLoader<PagingLoadConfig, PagingLoadResult<StudentGroup>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<StudentGroup>>(
                    proxy);
            loader.setRemoteSort(true);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, StudentGroup, PagingLoadResult<StudentGroup>>(store));
            loader.load();

            groupCombo = new ComboBox<StudentGroup>(store, props.nameLabel());
            groupCombo.setLoader(loader);
            Info.display(String.valueOf(store.size()), "");
        }
        return groupCombo;
    }

    private TextButton getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = new TextButton("Save");
            saveBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    if (validate()) {
//                        currentStudent = new Student();
                        currentStudent.setName(getNameTxt().getValue());
                        currentStudent.setLogin(getLoginTxt().getValue());
                        currentStudent.setPassword(getPasswordTxt().getValue());
                        currentStudent.setRole(getRoleCombo().getValue());
                        currentStudent.setStudentGroup(getGroupCombo().getValue());
                        fotiusService.saveStudent(currentStudent, new AsyncCallback<Student>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Info.display("Internal server error", "Can't save student");
                            }

                            @Override
                            public void onSuccess(Student result) {
                                Info.display("Student saved with ID", String.valueOf(result.getStudentId()));
                                EditStudentWindow.getInstance().hide();
                                StudentsWindow.getInstance().refresh();
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
                    EditStudentWindow.getInstance().hide();
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
        return valid;
    }
}
