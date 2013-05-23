package com.fotius.client.ui;

import com.fotius.client.model.StudentProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGridWindow<T, P extends PropertyAccess<T>> extends Window {

    private static final String WIDTH = "500px";
    private static final String HEIGHT = "400px";
    private PagingLoader loader;
    private Grid<T> grid;
    private ToolBar toolBar;

    public BaseGridWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setSize(WIDTH, HEIGHT);
        setMaximizable(true);
        setMinimizable(true);
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(false);
        mainPanel.setWidget(getGridPanel());
        add(mainPanel);
    }

    public abstract void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<T>> callback);

    public abstract List<ColumnConfig<T, ?>> getColumnConfigs();

    public abstract P getProperties();

    public abstract ModelKeyProvider<T> getModelKey();

    public VerticalLayoutContainer getGridPanel() {
        RpcProxy<PagingLoadConfig, PagingLoadResult<T>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<T>>() {
            @Override
            public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<T>> callback) {
                loadData(loadConfig, callback);
            }
        };

        StudentProperties props = GWT.create(StudentProperties.class);
        ListStore<T> store = new ListStore<T>(getModelKey());
        loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<T>>(
                proxy);
        loader.setRemoteSort(false);
        loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(store));

        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(loader);

        ColumnModel<T> cm = new ColumnModel<T>(getColumnConfigs());

        grid = new Grid<T>(store, cm){
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
        grid.setLoader(loader);
        grid.setLoadMask(true);
        grid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                Info.display("Edit window show", "yeah");
            }
        });
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        return con;
    }

    private ToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new ToolBar();
            if (getToolbarButtons() != null && !getToolbarButtons().isEmpty()) {
                for (TextButton btn : getToolbarButtons()) {
                    toolBar.add(btn);
                }
            }
        }
        return toolBar;
    }

    public abstract List<TextButton> getToolbarButtons();

    public void refresh() {
        loader.load();
    }

    public T getSelectedEntity() {
        return grid.getSelectionModel().getSelectedItem();
    }
}
