package com.fotius.client;

import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.client.ui.*;
import com.fotius.shared.model.Teacher;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktop.client.widget.*;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;

public class FotiusNet implements EntryPoint {

    private Desktop desktop;
    private FotiusnetConstants constants = GWT.create(FotiusnetConstants.class);

    public void onModuleLoad() {
        new LoginWindow() {
            @Override
            public void onLoginSuccess(User user) {
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
        setBackground(RootPanel.get());
        RootPanel.get().add(desktop.asWidget());
        Shortcut teachersShortcut = new Shortcut();
        teachersShortcut.setText(constants.teachers());
        teachersShortcut.setIcon(Resources.IMAGES.hp());
        teachersShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent selectEvent) {
                GWT.runAsync(new RunAsyncCallback() {
                    @Override
                    public void onFailure(Throwable reason) {

                    }

                    @Override
                    public void onSuccess() {
                        TeachersWindow.getInstance().show();
                        Info.display("Teachers window", "displayed");
                    }
                });

            }
        });
        desktop.addShortcut(teachersShortcut);

        Shortcut studentsShortcut = new Shortcut();
        studentsShortcut.setText(constants.students());
        studentsShortcut.setIcon(Resources.IMAGES.hp());
        studentsShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                StudentsWindow.getInstance().show();
                Info.display("Students window", "displayed");
            }
        });
        desktop.addShortcut(studentsShortcut);

        Shortcut groupsShortcut = new Shortcut();
        groupsShortcut.setText(constants.groups());
        groupsShortcut.setIcon(Resources.IMAGES.group());
        groupsShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                GroupsWindow.getInstance().show();
                Info.display("Groups window", "displayed");
            }
        });
        desktop.addShortcut(groupsShortcut);

        Shortcut msgsShortCut = new Shortcut();
        msgsShortCut.setText("msgs");
        msgsShortCut.setIcon(Resources.IMAGES.group());
        msgsShortCut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                MessagesWindow.getInstance().show();
                Info.display("Msgs window", "displayed");
            }
        });
        desktop.addShortcut(msgsShortCut);
    }
}