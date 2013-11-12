package com.fotius.client.ui;

import com.fotius.client.FotiusnetConstants;
import com.fotius.client.Resources;
import com.fotius.client.model.StudentProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentsWindow extends BaseGridWindow<Student, StudentProperties> {

    private static StudentsWindow instance = null;
    private static FotiusnetConstants constants = GWT.create(FotiusnetConstants.class);
    private final StudentProperties props = GWT.create(StudentProperties.class);
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private TextButton addStudentBtn, editStudentBtn, removeStudentBtn;

    public static StudentsWindow getInstance() {
        if (instance == null) {
            instance = new StudentsWindow(constants.studentsWindowCaption(), Resources.IMAGES.hp_small());
        }
        return instance;
    }

    public StudentsWindow(String title, ImageResource icon) {
        super(title, icon);
    }

    @Override
    public void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Student>> callback) {
        fotiusService.getStudents(loadConfig, callback);
    }

    @Override
    public List<ColumnConfig<Student, ?>> getColumnConfigs() {
        ColumnConfig<Student, String> nameColumn = new ColumnConfig<Student, String>(getProperties().name(), 150, constants.studentName());
        ColumnConfig<Student, String> loginColumn = new ColumnConfig<Student, String>(getProperties().login(), 150, constants.studentLogin());
        ColumnConfig<Student, String> passwordConfig = new ColumnConfig<Student, String>(getProperties().password(), 150, constants.studentPassword());
        ColumnConfig<Student, String> groupConfig = new ColumnConfig<Student, String>(getProperties().studentGroup(), 150, constants.studentGroup());
        ColumnConfig<Student, String> roleConfig = new ColumnConfig<Student, String>(getProperties().role(), 150, constants.studentRole());
        List<ColumnConfig<Student, ?>> l = new ArrayList<ColumnConfig<Student, ?>>();
        l.add(nameColumn);
        l.add(loginColumn);
        l.add(passwordConfig);
        l.add(groupConfig);
        l.add(roleConfig);
        return l;
    }

    @Override
    public StudentProperties getProperties() {
        return props;
    }

    @Override
    public ModelKeyProvider<Student> getModelKey() {
        return getProperties().userId();
    }

    @Override
    public void editEntity() {
        EditStudentWindow studentWindow = EditStudentWindow.getInstance();
        Student student = getSelectedEntity();
        if (student != null) {
            studentWindow.fillStudentData(student);
            studentWindow.show();
        }
    }

    private TextButton getAddStudentButton() {
        if (addStudentBtn == null) {
            addStudentBtn = UIHelper.createToolbarBtn(constants.addStudent(), Resources.IMAGES.add24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditStudentWindow.getInstance().show();
                    EditStudentWindow.getInstance().fillStudentData(new Student());
                }
            });
        }
        return addStudentBtn;
    }

    private TextButton getEditStudentBtn() {
        if (editStudentBtn == null) {
            editStudentBtn = UIHelper.createToolbarBtn(constants.editStudent(), Resources.IMAGES.edit24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    editEntity();
                }
            });
        }
        return editStudentBtn;

    }

    private TextButton getRemoveStudentBtn() {
        if (removeStudentBtn == null) {
            removeStudentBtn = UIHelper.createToolbarBtn(constants.removeStudent(), Resources.IMAGES.delete24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    fotiusService.removeStudent(getSelectedEntity(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new MessageBox("Error", "Unable to remove student").show();
                        }

                        @Override
                        public void onSuccess(Void result) {
                            refresh();
                        }
                    });
                }
            });
        }
        return removeStudentBtn;
    }

    @Override
    public List<TextButton> getToolbarButtons() {
        return Arrays.asList(getAddStudentButton(), getEditStudentBtn(), getRemoveStudentBtn());
    }
}
