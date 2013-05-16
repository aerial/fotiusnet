package com.fotius.client;

import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.client.ui.*;
import com.fotius.shared.model.Teacher;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktop.client.widget.*;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;

public class FotiusNet implements EntryPoint {

    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    private Desktop desktop;

    public void onModuleLoad() {
        setBackground(RootPanel.get());
        new LoginWindow() {
            @Override
            public void onLoginSuccess(Teacher user) {
                Info.display("Login succeed", "Login: " + user.getLogin() + " Password: " + user.getPassword() );
                initDesktop();
            }
        }.show();
    }

    private void setBackground(HasWidgets hasWidgets) {
        if (hasWidgets instanceof UIObject) {
            ((UIObject) hasWidgets).addStyleName("x-desktop");
        }
    }

    private void initDesktop() {
        desktop = new Desktop();
        desktop.addWindow(TeachersWindow.getInstance());
        desktop.addWindow(StudentsWindow.getInstance());
        desktop.addWindow(GroupsWindow.getInstance());
        desktop.setDesktopLayoutType(DesktopLayoutType.CASCADE);
        RootPanel.get().add(desktop.asWidget());
        Shortcut teachersShortcut = new Shortcut();
        teachersShortcut.setText("Teachers");
        teachersShortcut.setIcon(Resources.IMAGES.user32());
        teachersShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                TeachersWindow.getInstance().show();
                Info.display("Teachers window", "displayed");
            }
        });
        desktop.addShortcut(teachersShortcut);

        Shortcut studentsShortcut = new Shortcut();
        studentsShortcut.setText("Students");
        studentsShortcut.setIcon(Resources.IMAGES.user32());
        studentsShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                StudentsWindow.getInstance().show();
                Info.display("Students window", "displayed");
            }
        });
        desktop.addShortcut(studentsShortcut);

        Shortcut groupsShortcut = new Shortcut();
        groupsShortcut.setText("Groups");
        groupsShortcut.setIcon(Resources.IMAGES.users32());
        groupsShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                GroupsWindow.getInstance().show();
                Info.display("Groups window", "displayed");
            }
        });
        desktop.addShortcut(groupsShortcut);

     //   StartMenu menu = desktop.getStartMenu();
    //    menu.add(new StartMainMenuItem("Teachers"));
    //    TaskBar taskBar = desktop.getTaskBar();
    }
}
//
//
//      TaskBar taskBar = desktop.getTaskBar();
//
//      StartMenu menu = taskBar.getStartMenu();
//      menu.setHeading("FotiusNET");
//      menu.setIconStyle("user");
//


//      StartMainMenuItem menuItem = ;
//      menuItem.setData("window", teachersWindow);
//      menuItem.setIcon(IconHelper.createStyle("accordion"));
//      menuItem.addSelectionListener(menuListener);
//      menu.add(menuItem);
//
//      menuItem = new StartMainMenuItem("Students");
//      menuItem.setIcon(IconHelper.createStyle("accordion"));
//      menuItem.addSelectionListener(menuListener);
//      menuItem.setData("window", studentsWindow);
//      menu.add(menuItem);
//
//      // tools
//      MenuItem tool = new MenuItem("Settings");
//      tool.setIcon(IconHelper.createStyle("settings"));
//      tool.addSelectionListener(new SelectionListener<MenuEvent>() {
//          @Override
//          public void componentSelected(MenuEvent ce) {
//
//              Info.display("Event", "The 'Settings' tool was clicked");
//
//          }
//      });
//      menu.addTool(tool);
//
//      menu.addToolSeperator();
//
//      tool = new MenuItem("Logout");
//      tool.setIcon(IconHelper.createStyle("logout"));
//      tool.addSelectionListener(new SelectionListener<MenuEvent>() {
//          @Override
//          public void componentSelected(MenuEvent ce) {
//              Info.display("Event", "The 'Logout' tool was clicked");
//              com.google.gwt.user.client.Window.Location.reload();
//              //desktop.getDesktop().hide();
//              //loginWindow.show();
//
//          }
//      });
//      menu.addTool(tool);


//  private Window createAccordionWindow() {
//    final Window w = new Window();
//    w.setMinimizable(true);
//    w.setMaximizable(true);
//    w.setIcon(IconHelper.createStyle("accordion"));
//    w.setHeading("Accordion Window");
//    w.setWidth(200);
//    w.setHeight(350);
//
//    ToolBar toolBar = new ToolBar();
//    Button item = new Button();
//    item.setIcon(IconHelper.createStyle("icon-connect"));
//    toolBar.add(item);
//
//    toolBar.add(new SeparatorToolItem());
//    w.setTopComponent(toolBar);
//
//    item = new Button();
//    item.setIcon(IconHelper.createStyle("icon-user-add"));
//    toolBar.add(item);
//
//    item = new Button();
//    item.setIcon(IconHelper.createStyle("icon-user-delete"));
//    toolBar.add(item);
//
//    w.setLayout(new AccordionLayout());
//
//    ContentPanel cp = new ContentPanel();
//    cp.setAnimCollapse(false);
//    cp.setHeading("Online Users");
////    cp.setScrollMode(Scroll.AUTO);
//    cp.getHeader().addTool(new ToolButton("x-tool-refresh"));
//
//    w.add(cp);
//
//    TreeStore<ModelData> store = new TreeStore<ModelData>();
//    TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
//    tree.setIconProvider(new ModelIconProvider<ModelData>() {
//
//      public AbstractImagePrototype getIcon(ModelData model) {
//        if (model.get("icon") != null) {
//          return IconHelper.createStyle((String) model.get("icon"));
//        } else {
//          return null;
//        }
//      }
//
//    });
//    tree.setDisplayProperty("name");
//
//    ModelData m = newItem("Family", null);
//    store.add(m, false);
//    tree.setExpanded(m, true);
//
//    store.add(m, newItem("Darrell", "user"), false);
//    store.add(m, newItem("Maro", "user-girl"), false);
//    store.add(m, newItem("Lia", "user-kid"), false);
//    store.add(m, newItem("Alec", "user-kid"), false);
//    store.add(m, newItem("Andrew", "user-kid"), false);
//
//    m = newItem("Friends", null);
//    store.add(m, false);
//    tree.setExpanded(m, true);
//    store.add(m, newItem("Bob", "user"), false);
//    store.add(m, newItem("Mary", "user-girl"), false);
//    store.add(m, newItem("Sally", "user-girl"), false);
//    store.add(m, newItem("Jack", "user"), false);
//
//    cp.add(tree);
//
//    cp = new ContentPanel();
//    cp.setAnimCollapse(false);
//    cp.setHeading("Settings");
//    cp.setBodyStyleName("pad-text");
//    cp.addText(TestData.DUMMY_TEXT_SHORT);
//    w.add(cp);
//
//    cp = new ContentPanel();
//    cp.setAnimCollapse(false);
//    cp.setHeading("Stuff");
//    cp.setBodyStyleName("pad-text");
//    cp.addText(TestData.DUMMY_TEXT_SHORT);
//    w.add(cp);
//
//    cp = new ContentPanel();
//    cp.setAnimCollapse(false);
//    cp.setHeading("More Stuff");
//    cp.setBodyStyleName("pad-text");
//    cp.addText(TestData.DUMMY_TEXT_SHORT);
//    w.add(cp);
//    return w;
//  }
//
//  private Window createGridWindow() {
//    Window w = new Window();
//    w.setIcon(IconHelper.createStyle("icon-grid"));
//    w.setMinimizable(true);
//    w.setMaximizable(true);
//    w.setHeading("Grid Window");
//    w.setSize(500, 400);
//    w.setLayout(new FitLayout());
//
//    GroupingStore<Stock> store = new GroupingStore<Stock>();
//    store.add(TestData.getCompanies());
//    store.groupBy("industry");
//
//    ColumnConfig company = new ColumnConfig("name", "Company", 60);
//    ColumnConfig price = new ColumnConfig("open", "Price", 20);
//    price.setNumberFormat(NumberFormat.getCurrencyFormat());
//    ColumnConfig change = new ColumnConfig("change", "Change", 20);
//    ColumnConfig industry = new ColumnConfig("industry", "Industry", 20);
//    ColumnConfig last = new ColumnConfig("date", "Last Updated", 20);
//    last.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));
//
//    List<ColumnConfig> config = new ArrayList<ColumnConfig>();
//    config.add(company);
//    config.add(price);
//    config.add(change);
//    config.add(industry);
//    config.add(last);
//
//    final ColumnModel cm = new ColumnModel(config);
//
//    GroupingView view = new GroupingView();
//    view.setForceFit(true);
//    view.setGroupRenderer(new GridGroupRenderer() {
//      public String render(GroupColumnData data) {
//        String f = cm.getColumnById(data.field).getHeader();
//        String l = data.models.size() == 1 ? "Item" : "Items";
//        return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
//      }
//    });
//
//    Grid<Stock> grid = new Grid<Stock>(store, cm);
//    grid.setView(view);
//    grid.setBorders(true);
//
//    w.add(grid);
//    return w;
//  }
//
//  private Window createTabWindow() {
//    Window w = new Window();
//    w.setMinimizable(true);
//    w.setMaximizable(true);
//    w.setSize(740, 480);
//    w.setIcon(IconHelper.createStyle("tabs"));
//    w.setHeading("Tab Window");
//
//    w.setLayout(new FitLayout());
//
//    TabPanel panel = new TabPanel();
//
//    for (int i = 0; i < 4; i++) {
//      TabItem item = new TabItem("Tab Item " + (i + 1));
//      item.addText("Something useful would be here");
//      panel.add(item);
//    }
//
//    w.add(panel);
//    return w;
//  }
//
//  private Window createBogusWindow(int index) {
//    Window w = new Window();
//    w.setIcon(IconHelper.createStyle("bogus"));
//    w.setMinimizable(true);
//    w.setMaximizable(true);
//    w.setHeading("Bogus Window " + ++index);
//    w.setSize(400, 300);
//    return w;
//  }
//
//  private ModelData newItem(String text, String iconStyle) {
//    ModelData m = new BaseModelData();
//    m.set("name", text);
//    m.set("icon", iconStyle);
//    return m;
//  }


