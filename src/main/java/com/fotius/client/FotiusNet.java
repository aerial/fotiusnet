package com.fotius.client;

import com.fotius.client.ui.*;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.sencha.gxt.desktop.client.widget.*;
import com.sencha.gxt.desktop.client.widget.Desktop;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FotiusNet implements EntryPoint {

    private Desktop desktop;
    private FotiusnetConstants constants = GWT.create(FotiusnetConstants.class);
    private static Logger logger = Logger.getLogger("");


    public void onModuleLoad() {
        new LoginWindow() {
            @Override
            public void onLoginSuccess(User user) {
                logger.log(Level.INFO, "Login succeed" +  " Login: " + user.getLogin() + " Password: " + user.getPassword() );
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
        studentsShortcut.setIcon(Resources.IMAGES.student());
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
        msgsShortCut.setText("Messages");
        msgsShortCut.setIcon(Resources.IMAGES.messages_big());
        msgsShortCut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                MessagesWindow.getInstance().show();
                Info.display("Msgs window", "displayed");
            }
        });
        desktop.addShortcut(msgsShortCut);

        Shortcut documentsShortcut = new Shortcut();
        documentsShortcut.setText("Documents");
        documentsShortcut.setIcon(Resources.IMAGES.upload_big());
        documentsShortcut.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                DocumentsWindow.getInstance().show();
                Info.display("Docs window", "displayed");
            }
        });
        desktop.addShortcut(documentsShortcut);
    }
}