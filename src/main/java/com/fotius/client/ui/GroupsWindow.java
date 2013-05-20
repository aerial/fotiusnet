package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.model.StudentGroupProperties;
import com.fotius.client.model.StudentProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Student;
import com.fotius.shared.model.StudentGroup;
import com.fotius.shared.model.StudentRole;
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

public class GroupsWindow extends Window {
    private static GroupsWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private Grid<StudentGroup> groupsGrid;
    private TextButton addGroupBtn, editGroupBtn, removeGroupBtn;
    private PagingToolBar toolbar;
    private PagingLoader loader;
    private ToolBar toolBar;

    public static GroupsWindow getInstance() {
        if (instance == null) {
            instance = new GroupsWindow();
        }
        return instance;
    }

    public GroupsWindow() {
        getHeader().setIcon(Resources.IMAGES.table());
        setHeadingText("Groups");
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
        loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<StudentGroup>>(
                proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, StudentGroup, PagingLoadResult<StudentGroup>>(store));

        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);

        StudentGroupProperties props = GWT.create(StudentGroupProperties.class);
        ColumnConfig<StudentGroup, String> nameColumn = new ColumnConfig<StudentGroup, String>(props.name(), 150, "name");
        List<ColumnConfig<StudentGroup, ?>> l = new ArrayList<ColumnConfig<StudentGroup, ?>>();
        l.add(nameColumn);
        ColumnModel<StudentGroup> cm = new ColumnModel<StudentGroup>(l);

        groupsGrid = new Grid<StudentGroup>(store, cm){
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
        groupsGrid.setLoader(loader);
        groupsGrid.setLoadMask(true);
        groupsGrid.getView().setForceFit(true);
        groupsGrid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                EditGroupWindow.getInstance().show();
                StudentGroup group = groupsGrid.getStore().get(event.getRowIndex());
                EditGroupWindow editGroupWindow = EditGroupWindow.getInstance();
                editGroupWindow.fillGroupData(group);
            }
        });

        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(groupsGrid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        return con;
    }

    private TextButton getAddGroupBtn() {
        if (addGroupBtn == null) {
            addGroupBtn = new TextButton("Add group", Resources.IMAGES.users_add_24());
            addGroupBtn.setScale(ButtonCell.ButtonScale.LARGE);
            addGroupBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            addGroupBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    EditGroupWindow.getInstance().show();
                }
            });
        }
        return addGroupBtn;
    }

    private TextButton getEditGroupBtn() {
        if (editGroupBtn == null) {
            editGroupBtn = new TextButton("Edit group", Resources.IMAGES.users_edit_24());
            editGroupBtn.setScale(ButtonCell.ButtonScale.LARGE);
            editGroupBtn.setIconAlign(ButtonCell.IconAlign.TOP);
            editGroupBtn.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    StudentGroup group = groupsGrid.getSelectionModel().getSelectedItem();
                    if (group != null) {
                        EditGroupWindow.getInstance().fillGroupData(group);
                        EditGroupWindow.getInstance().show();
                    }
                }
            });
        }
        return editGroupBtn;
    }

    private TextButton getRemoveGroupBtn() {
        if (removeGroupBtn == null) {
            removeGroupBtn = new TextButton("Remove group", Resources.IMAGES.users_delete_24());
            removeGroupBtn.setScale(ButtonCell.ButtonScale.LARGE);
            removeGroupBtn.setIconAlign(ButtonCell.IconAlign.TOP);
//            removeGroupBtn.addSelectHandler(new SelectEvent.SelectHandler() {
//                @Override
//                public void onSelect(SelectEvent selectEvent) {
//                    StudentGroup group = groupsGrid.getSelectionModel().getSelectedItem();
//                    if (group != null) {
//                        fotiusService.removeGroup(group, new AsyncCallback<Void>() {
//                            @Override
//                            public void onFailure(Throwable throwable) {
//                                //To change body of implemented methods use File | Settings | File Templates.
//                            }
//
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                refresh();                            }
//
//                        });
//                    }
//                }
//            });
        }
        return removeGroupBtn;
    }

    private ToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new ToolBar();
            toolBar.add(getAddGroupBtn());
            toolBar.add(getEditGroupBtn());
            toolbar.add(getRemoveGroupBtn());
        }
        return toolBar;
    }

    public void refresh() {
        loader.load();
    }

}
