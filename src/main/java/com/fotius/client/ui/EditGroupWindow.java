package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.model.StudentRoleProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Student;
import com.fotius.shared.model.StudentGroup;
import com.fotius.shared.model.StudentRole;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
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

public class EditGroupWindow extends Window {
    private StudentGroup currentGroup;
    private TextField nameTxt;
    private TextButton saveBtn, cancelBtn;
    private FramedPanel mainPanel;

    private static EditGroupWindow instance;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    public static EditGroupWindow getInstance() {
        if (instance == null) {
            instance = new EditGroupWindow();
        }
        return instance;
    }

    public EditGroupWindow() {
        getHeader().setIcon(Resources.IMAGES.add());
        setSize("335", "200");
        setModal(true);
        setHeadingText("Group  information");
        add(getMainPanel());

        currentGroup = new StudentGroup();
    }

    public void fillGroupData(StudentGroup group) {
        currentGroup = group;
        getNameTxt().setValue(group.getName());
    }

    private FramedPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new FramedPanel();
            mainPanel.setHeadingText("Enter the group information");
            mainPanel.setBodyStyle("padding: 5px");
            VerticalLayoutContainer p = new VerticalLayoutContainer();
//            p.setBorders(true);
            mainPanel.add(p);
            p.add(new FieldLabel(getNameTxt(), "Name"), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
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

    private TextButton getSaveBtn() {
        if (saveBtn == null) {
            saveBtn = new TextButton("Save");
            saveBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    if (validate()) {
                        currentGroup.setName(getNameTxt().getValue());
                        fotiusService.saveStudentGroup(currentGroup, new AsyncCallback<StudentGroup>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Info.display("Internal server error", "Can't save group");
                            }

                            @Override
                            public void onSuccess(StudentGroup result) {
                                Info.display("Group saved with ID", String.valueOf(result.getGroupId()));
                                EditGroupWindow.getInstance().hide();
                                GroupsWindow.getInstance().refresh();
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
                    EditGroupWindow.getInstance().hide();
                }
            });
        }
        return cancelBtn;

    }

    private boolean validate() {
        boolean valid = true;
        valid &= getNameTxt().isValid();
        return valid;
    }
}
