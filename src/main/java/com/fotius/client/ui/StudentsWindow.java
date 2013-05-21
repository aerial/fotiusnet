package com.fotius.client.ui;


import com.fotius.client.Resources;
import com.fotius.client.model.StudentProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
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
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.List;

public class StudentsWindow extends Window {
    private static StudentsWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private Grid<Student> studentsGrid;
    private TextButton addStudentBtn, editStudentBtn, removeStudentBtn;
    private PagingLoader loader;
    private ToolBar toolBar;

    public static StudentsWindow getInstance() {
        if (instance == null) {
            instance = new StudentsWindow();
        }
        return instance;
    }

    public StudentsWindow() {
        getHeader().setIcon(Resources.IMAGES.table());
        setHeadingText("Students");
        setSize("500px", "400px");
        setMaximizable(true);
        setMinimizable(true);
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(false);
        mainPanel.setWidget(getGridPanel());
        add(mainPanel);
    }

    public VerticalLayoutContainer getGridPanel() {
        final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

        RpcProxy<PagingLoadConfig, PagingLoadResult<Student>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<Student>>() {
            @Override
            public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Student>> callback) {
                fotiusService.getStudents(loadConfig, callback);
            }
        };

        StudentProperties props = GWT.create(StudentProperties.class);
        ListStore<Student> store = new ListStore<Student>(props.studentId());
        loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Student>>(
                proxy);
        loader.setRemoteSort(false);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, Student, PagingLoadResult<Student>>(store));

        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);

        ColumnConfig<Student, String> nameColumn = new ColumnConfig<Student, String>(props.name(), 150, "name");
        ColumnConfig<Student, String> loginColumn = new ColumnConfig<Student, String>(props.login(), 150, "login");
        ColumnConfig<Student, String> passwordConfig = new ColumnConfig<Student, String>(props.password(), 150, "pass");
        ColumnConfig<Student, String> groupConfig = new ColumnConfig<Student, String>(props.studentGroup(), 150, "group");
        ColumnConfig<Student, String> roleConfig = new ColumnConfig<Student, String>(props.role(), 150, "role");
        List<ColumnConfig<Student, ?>> l = new ArrayList<ColumnConfig<Student, ?>>();
        l.add(nameColumn);
        l.add(loginColumn);
        l.add(passwordConfig);
        l.add(groupConfig);
        l.add(roleConfig);
        ColumnModel<Student> cm = new ColumnModel<Student>(l);
        final GroupingView<Student> view = new GroupingView<Student>();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.groupBy(groupConfig);

        studentsGrid = new Grid<Student>(store, cm){
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
        };
        studentsGrid.setLoader(loader);
        studentsGrid.setLoadMask(true);
        studentsGrid.setView(view);
        studentsGrid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                EditStudentWindow.getInstance().show();
                Student student = studentsGrid.getStore().get(event.getRowIndex());
                EditStudentWindow editStudentWindow = EditStudentWindow.getInstance();
                editStudentWindow.fillStudentData(student);

            }
        });
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(studentsGrid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        return con;
    }

    private TextButton getAddStudentButton() {
        if (addStudentBtn == null) {
            addStudentBtn = new TextButton("Add", Resources.IMAGES.add24());
            addStudentBtn.setScale(ButtonCell.ButtonScale.LARGE);
            addStudentBtn.setIconAlign(ButtonCell.IconAlign.LEFT);
            addStudentBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditStudentWindow.getInstance().show();
                }
            });
        }
        return addStudentBtn;
    }

    private TextButton getEditStudentBtn() {
        if (editStudentBtn == null) {
            editStudentBtn = new TextButton("Edit", Resources.IMAGES.edit24());
            editStudentBtn.setScale(ButtonCell.ButtonScale.LARGE);
            editStudentBtn.setIconAlign(ButtonCell.IconAlign.LEFT);
            editStudentBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditStudentWindow studentWindow = EditStudentWindow.getInstance();
                    Student student = studentsGrid.getSelectionModel().getSelectedItem();
                    if (student != null) {
                        studentWindow.fillStudentData(student);
                    }
                    studentWindow.show();
                }
            });

        }
        return editStudentBtn;

    }

    private TextButton getRemoveStudentBtn() {
        if (removeStudentBtn == null) {
            removeStudentBtn = new TextButton("Remove", Resources.IMAGES.delete24());
            removeStudentBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeStudentBtn.setIconAlign(ButtonCell.IconAlign.LEFT);
            removeStudentBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    fotiusService.removeStudent(studentsGrid.getSelectionModel().getSelectedItem(), new AsyncCallback<Void>() {
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

    private ToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new ToolBar();
            toolBar.add(getAddStudentButton());
            toolBar.add(getEditStudentBtn());
            toolBar.add(getRemoveStudentBtn());
            toolBar.add(new FillToolItem());
            removeStudentBtn = new TextButton("Download list", Resources.IMAGES.arrow_down());
            removeStudentBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeStudentBtn.setIconAlign(ButtonCell.IconAlign.LEFT);
            //TODO add handler
            toolBar.add(removeStudentBtn);
            removeStudentBtn = new TextButton("Upload list", Resources.IMAGES.arrow_up());
            removeStudentBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeStudentBtn.setIconAlign(ButtonCell.IconAlign.LEFT);
            //TODO add handler
            toolBar.add(removeStudentBtn);
        }
        return toolBar;
    }

    public void refresh() {
        loader.load();
    }
}
