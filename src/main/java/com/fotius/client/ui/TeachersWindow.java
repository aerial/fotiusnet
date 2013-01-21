package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.model.TeacherProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.TeacherRole;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.List;

public class TeachersWindow extends Window {
    private static TeachersWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private Grid<Teacher> teachersGrid;
    private TextButton addTeacherBtn;
    private PagingLoader loader;
    private ToolBar toolBar;

    public static TeachersWindow getInstance() {
        if (instance == null) {
            instance = new TeachersWindow();
        }
        return instance;
    }

    public TeachersWindow() {
        getHeader().setIcon(Resources.IMAGES.table());
        setHeadingText("Teachers");
        setSize("500px", "400px");
        setMaximizable(true);
        setMinimizable(true);
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(false);

        mainPanel.setWidget(getGridPanel());
        add(mainPanel);
//        addButton(getAddTeacherBtn());
    }

    public VerticalLayoutContainer getGridPanel() {
        final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

        RpcProxy<PagingLoadConfig, PagingLoadResult<Teacher>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Teacher>>() {
            @Override
            public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Teacher>> callback) {
                fotiusService.getTeachers(loadConfig, callback);
            }
        };

        ListStore<Teacher> store = new ListStore<Teacher>(new ModelKeyProvider<Teacher>() {
            @Override
            public String getKey(Teacher teacher) {
                return "" + teacher.getTeacherId();
            }
        });
        loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Teacher>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Teacher, PagingLoadResult<Teacher>>(store));

        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);


        TeacherProperties props = GWT.create(TeacherProperties.class);
        ColumnConfig<Teacher, String> nameColumn = new ColumnConfig<Teacher, String>(props.name(), 150, "name");
        ColumnConfig<Teacher, String> loginColumn = new ColumnConfig<Teacher, String>(props.login(), 150, "login");
        ColumnConfig<Teacher, String> passwordConfig = new ColumnConfig<Teacher, String>(props.password(), 150, "pass");
        ColumnConfig<Teacher, TeacherRole> roleConfig = new ColumnConfig<Teacher, TeacherRole>(props.role(), 150, "role");
        roleConfig.setCell(new AbstractCell<TeacherRole>() {
            @Override
            public void render(Context context, TeacherRole value, SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendEscaped(value.getName());
                }
            }
        });
        List<ColumnConfig<Teacher, ?>> l = new ArrayList<ColumnConfig<Teacher, ?>>();
        l.add(nameColumn);
        l.add(loginColumn);
        l.add(passwordConfig);
        l.add(roleConfig);
        ColumnModel<Teacher> cm = new ColumnModel<Teacher>(l);

        teachersGrid = new Grid<Teacher>(store, cm){
            @Override
            protected void onAfterFirstAttach() {
                super.onAfterFirstAttach();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        loader.load();
                    }
                });
            }
        };;
        teachersGrid.setLoader(loader);
        teachersGrid.setLoadMask(true);
        teachersGrid.getView().setForceFit(true);
        teachersGrid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                EditTeacherWindow.getInstance().show();
                Teacher teacher = teachersGrid.getStore().get(event.getRowIndex());
                EditTeacherWindow editTeacherWindow = EditTeacherWindow.getInstance();
                editTeacherWindow.fillTeacherData(teacher);
            }
        });

        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(teachersGrid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        return con;
    }

    private TextButton getAddTeacherBtn() {
        addTeacherBtn = new TextButton("Add teacher");
        addTeacherBtn.setIcon(Resources.IMAGES.add16());
        addTeacherBtn.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                EditTeacherWindow.getInstance().show();
            }
        });
        return addTeacherBtn;
    }

    private ToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new ToolBar();
            TextButton addTeacherBtn = new TextButton("Add teacher", Resources.IMAGES.add24());
            addTeacherBtn.setScale(ButtonCell.ButtonScale.LARGE);
            addTeacherBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            addTeacherBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditTeacherWindow.getInstance().show();
                }
            });
            toolBar.add(addTeacherBtn);
            TextButton editTeacherBtn = new TextButton("Edit teacher", Resources.IMAGES.edit24());
            editTeacherBtn.setScale(ButtonCell.ButtonScale.LARGE);
            editTeacherBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            editTeacherBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditTeacherWindow teacherWindow = EditTeacherWindow.getInstance();
                    Teacher teacher = teachersGrid.getSelectionModel().getSelectedItem();
                    if (teacher != null) {
                        teacherWindow.fillTeacherData(teacher);
                    }
                    teacherWindow.show();
                }
            });
            toolBar.add(editTeacherBtn);
            TextButton removeTeacherBtn = new TextButton("Remove teacher", Resources.IMAGES.delete24());
            removeTeacherBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeTeacherBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            removeTeacherBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    fotiusService.removeTeacher(teachersGrid.getSelectionModel().getSelectedItem(), new AsyncCallback<Void>() {
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
            toolBar.add(removeTeacherBtn);
            removeTeacherBtn = new TextButton("Download list", Resources.IMAGES.arrow_down());
            removeTeacherBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeTeacherBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            toolBar.add(removeTeacherBtn);
            removeTeacherBtn = new TextButton("Upload list", Resources.IMAGES.arrow_up());
            removeTeacherBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeTeacherBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            toolBar.add(removeTeacherBtn);
        }
        return toolBar;
    }


    public void refresh() {
        loader.load();
    }
}
