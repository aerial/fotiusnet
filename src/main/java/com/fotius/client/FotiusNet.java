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
    }
}