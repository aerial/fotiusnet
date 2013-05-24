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
import com.google.gwt.resources.client.ImageResource;
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
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupsWindow extends BaseGridWindow<StudentGroup, StudentGroupProperties> {

    private static GroupsWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private TextButton addGroupBtn, editGroupBtn, removeGroupBtn;
    private final StudentGroupProperties props = GWT.create(StudentGroupProperties.class);


    public static GroupsWindow getInstance() {
        if (instance == null) {
            instance = new GroupsWindow("Groups", Resources.IMAGES.group_small());
        }
        return instance;
    }

    public GroupsWindow(String text, ImageResource icon) {
        super(text, icon);
    }

    @Override
    public void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<StudentGroup>> callback) {
        fotiusService.getStudentGroups(loadConfig, callback);
    }

    @Override
    public List<ColumnConfig<StudentGroup, ?>> getColumnConfigs() {
        ColumnConfig<StudentGroup, String> nameColumn = new ColumnConfig<StudentGroup, String>(props.name(), 150, "name");
        List<ColumnConfig<StudentGroup, ?>> l = new ArrayList<ColumnConfig<StudentGroup, ?>>();
        l.add(nameColumn);
        return l;
    }

    @Override
    public StudentGroupProperties getProperties() {
        return props;
    }

    @Override
    public ModelKeyProvider<StudentGroup> getModelKey() {
        return getProperties().groupId();
    }

    private TextButton getAddGroupBtn() {
        if (addGroupBtn == null) {
            addGroupBtn = UIHelper.createToolbarBtn("Add group", Resources.IMAGES.users_add_24(), new SelectEvent.SelectHandler() {
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
            editGroupBtn = UIHelper.createToolbarBtn("Edit group", Resources.IMAGES.users_edit_24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    StudentGroup group = getSelectedEntity();
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
            removeGroupBtn = UIHelper.createToolbarBtn("Remove group", Resources.IMAGES.delete24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    StudentGroup group = getSelectedEntity();
                    if (group != null) {
                        fotiusService.removeGroup(group, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                new MessageBox("Error", "Unable to remove group").show();
                            }

                            @Override
                            public void onSuccess(Void aVoid) {
                                Info.display("Success", "Group removed");
                                refresh();
                            }
                        });
                    }
                }
            });
        }
        return removeGroupBtn;
    }

    @Override
    public List<TextButton> getToolbarButtons() {
        return Arrays.asList(getAddGroupBtn(), getEditGroupBtn(), getRemoveGroupBtn());
    }

}
