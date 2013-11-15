package com.fotius.client.ui;

import com.fotius.client.FotiusnetConstants;
import com.fotius.client.Resources;
import com.fotius.client.model.TeacherProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Teacher;
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
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeachersWindow extends BaseGridWindow<Teacher, TeacherProperties> {

    public static TeachersWindow instance = null;
    private static FotiusnetConstants constants = GWT.create(FotiusnetConstants.class);
    private static final TeacherProperties props = GWT.create(TeacherProperties.class);
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private TextButton addTeacherBtn, editTeacherBtn, removeTeacherBtn;
    private GridView view;


    public static TeachersWindow getInstance() {
        if (instance == null) {
            instance = new TeachersWindow(constants.teachersWindowCaption(), Resources.IMAGES.hp_small());
        }
        return instance;
    }


    public TeachersWindow(String title, ImageResource icon) {
        super(title, icon);
    }

    @Override
    public void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Teacher>> callback) {
        fotiusService.getTeachers(loadConfig, callback);
    }

    @Override
    public List<ColumnConfig<Teacher, ?>> getColumnConfigs() {
        ColumnConfig<Teacher, String> nameColumn = new ColumnConfig<Teacher, String>(getProperties().name(), 150, constants.teacherName());
        getView().setAutoExpandColumn(nameColumn);
        ColumnConfig<Teacher, String> loginColumn = new ColumnConfig<Teacher, String>(getProperties().login(), 150, constants.teacherLogin());
        ColumnConfig<Teacher, String> passwordConfig = new ColumnConfig<Teacher, String>(getProperties().password(), 150, constants.teacherPassword());
        ColumnConfig<Teacher, String> roleConfig = new ColumnConfig<Teacher, String>(getProperties().role(), 150, constants.teacherRole());
        List<ColumnConfig<Teacher, ?>> l = new ArrayList<ColumnConfig<Teacher, ?>>();
        l.add(nameColumn);
        l.add(loginColumn);
        l.add(passwordConfig);
        l.add(roleConfig);
        return l;
    }

    @Override
    public TeacherProperties getProperties() {
        return props;
    }

    @Override
    public ModelKeyProvider<Teacher> getModelKey() {
        return props.userId();
    }

    @Override
    public GridView getView() {
        if (view == null) {
            view = new GridView();
        }
        return view;
    }

    @Override
    public void editEntity() {
        EditTeacherWindow teacherWindow = EditTeacherWindow.getInstance();
        Teacher teacher = getSelectedEntity();
        if (teacher != null) {
            teacherWindow.fillTeacherData(teacher);
            teacherWindow.show();
        }
    }

    @Override
    public List<TextButton> getToolbarButtons() {
        return Arrays.asList(getAddTeacherBtn(), getEditTeacherBtn(), getRemoveTeacherBtn());
    }

    private TextButton getAddTeacherBtn() {
        if (addTeacherBtn == null) {
            addTeacherBtn = UIHelper.createToolbarBtn(constants.addTeacher(), Resources.IMAGES.add24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditTeacherWindow.getInstance().fillTeacherData(new Teacher());
                    EditTeacherWindow.getInstance().show();
                }
            });
        }
        return addTeacherBtn;
    }

    private TextButton getEditTeacherBtn() {
        if (editTeacherBtn == null) {
            editTeacherBtn = UIHelper.createToolbarBtn(constants.editTeacher(), Resources.IMAGES.edit24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    editEntity();
                }
            });
        }
        return editTeacherBtn;
    }

    private TextButton getRemoveTeacherBtn() {
        if (removeTeacherBtn == null) {
            removeTeacherBtn = UIHelper.createToolbarBtn(constants.removeTeacher(), Resources.IMAGES.delete24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    fotiusService.removeTeacher(getSelectedEntity(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new MessageBox("Error", "Unable to remove teacher").show();
                        }
                        @Override
                        public void onSuccess(Void result) {
                            refresh();
                        }
                    });
                }
            });
        }
        return removeTeacherBtn;
    }
}
