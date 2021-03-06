package com.fotius.client.ui;

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
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.util.List;

public abstract class BaseGridWindow<T, P extends PropertyAccess<T>> extends Window {

    private PagingLoader loader;
    private Grid<T> grid;
    private ToolBar toolBar;
    private GridView view;
    private ListStore store;
    private ContentPanel mainPanel;
    private ColumnModel<T> columnModel;
    private RpcProxy<PagingLoadConfig, PagingLoadResult<T>> proxy;

    public BaseGridWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setMaximizable(true);
        setMinimizable(true);
        setPixelSize(600, 500);
    }

    @Override
    public void show() {
        if (mainPanel == null) {
            mainPanel = new ContentPanel();
            mainPanel.setHeaderVisible(false);
            mainPanel.setWidget(getGridPanel());
            add(mainPanel);
        }
        super.show();
    }

    public abstract void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<T>> callback);

    public abstract List<ColumnConfig<T, ?>> getColumnConfigs();

    public abstract P getProperties();

    public abstract ModelKeyProvider<T> getModelKey();

    public abstract void editEntity();

    public abstract GridView getView();

    private VerticalLayoutContainer getGridPanel() {
        final PagingToolBar toolBar = new PagingToolBar(50);
        toolBar.getElement().getStyle().setProperty("borderBottom", "none");
        toolBar.bind(getLoader());
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(getGrid(), new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        con.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        return con;
    }

    public RpcProxy<PagingLoadConfig, PagingLoadResult<T>> getProxy() {
        if (proxy == null) {
            proxy = new RpcProxy<PagingLoadConfig,
                    PagingLoadResult<T>>() {
                @Override
                public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<T>> callback) {
                    loadData(loadConfig, callback);
                }
            };

        }
        return proxy;
    }

    public PagingLoader getLoader() {
        if (loader == null) {
            loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<T>>(
                    getProxy());
            loader.setRemoteSort(false);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(getStore()));


        }
        return loader;
    }

    public Grid<T> getGrid() {
        if (grid == null) {
            grid = new Grid<T>(getStore(), getColumnModel()){
                @Override
                protected void onAfterFirstAttach() {
                    super.onAfterFirstAttach();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            getLoader().load();
                        }
                    });
                }
            };
            grid.setLoader(getLoader());
            grid.setLoadMask(true);
            grid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
                @Override
                public void onRowDoubleClick(RowDoubleClickEvent event) {
                    editEntity();
                }
            });
            getView().setForceFit(true);
            grid.setView(getView());
        }
        return grid;
    }

    public ListStore<T> getStore() {
        if (store == null) {
            store = new ListStore<T>(getModelKey());
        }
        return store;
    }

    public ColumnModel<T> getColumnModel() {
        if (columnModel == null) {
            columnModel = new ColumnModel<T>(getColumnConfigs());
        }
        return columnModel;
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
        getLoader().load();
    }

    public T getSelectedEntity() {
        return getGrid().getSelectionModel().getSelectedItem();
    }
}
