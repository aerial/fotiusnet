package com.fotius.client.ui;


import com.fotius.client.Resources;
import com.fotius.client.Session;
import com.fotius.client.model.StudentGroupProperties;
import com.fotius.client.model.UserProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Message;
import com.fotius.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.*;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;


import java.util.List;

public class ComposeMessageWindow extends Window {

    private static ComposeMessageWindow instance = null;
    private final UserProperties props = GWT.create(UserProperties.class);
    private static final String WIDTH = "500px";
    private static final String HEIGHT = "400px";
    private ComboBox<User> recipientCombo;
    private TextArea messageTxt;
    private ToolBar toolBar;
    private TextButton sendMessageBtn;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);

    public static ComposeMessageWindow getInstance() {
        if (instance == null) {
            instance = new ComposeMessageWindow("Compose message", Resources.IMAGES.group_small());
        }
        return instance;
    }


    public ComposeMessageWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setSize(WIDTH, HEIGHT);
        setMaximizable(true);
        setMinimizable(true);
        initUI();
    }

    private void initUI() {
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(true);
        mainPanel.setWidget(getMessagePanel());
        add(mainPanel);
    }

    private VerticalLayoutContainer getMessagePanel() {
        VerticalLayoutContainer con = new VerticalLayoutContainer();
        con.setBorders(true);
        con.add(getToolBar(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(getRecipientCombo(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
        con.add(getMessageTxt(), new VerticalLayoutContainer.VerticalLayoutData(1, 1));
        return con;
    }

    private ComboBox<User> getRecipientCombo() {
        if (recipientCombo == null) {

            final RpcProxy<ListLoadConfig, ListLoadResult<User>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<User>>() {
                @Override
                public void load(final ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<User>> callback) {

                    fotiusService.getUsers(new AsyncCallback<List<User>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            //To change body of implemented methods use File | Settings | File Templates.
                        }

                        @Override
                        public void onSuccess(List<User> result) {
                            callback.onSuccess(new ListLoadResultBean<User>(result));
                        }
                    });


                }
            };

            ListStore<User> users = new ListStore<User>(props.userId());
            ListLoader loader = new ListLoader(proxy);
            loader.setRemoteSort(false);
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, User, ListLoadResult<User>>(users));
            recipientCombo = new ComboBox<User>(users, props.name());
            recipientCombo.setLoader(loader);
            recipientCombo.setTypeAhead(true);
            recipientCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
            recipientCombo.getLoader().load();

        }
        return recipientCombo;
    }

    private ToolBar getToolBar() {
        if (toolBar == null) {
            toolBar = new ToolBar();
            toolBar.add(getSendMessageBtn());

        }
        return toolBar;
    }

    private TextButton getSendMessageBtn() {
        if (sendMessageBtn == null) {
            sendMessageBtn = UIHelper.createToolbarBtn("send message", Resources.IMAGES.add24(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    Message toSend = new Message();
                    toSend.setSender(Session.currentUser);
                    toSend.setRecipient(recipientCombo.getCurrentValue());
                    toSend.setText(messageTxt.getText());
                    fotiusService.sendMessage(toSend, new AsyncCallback<Message>() {
                        @Override
                        public void onFailure(Throwable caught) {
                        }

                        @Override
                        public void onSuccess(Message result) {
                            Info.display("message sent", "rly");
                        }
                    });


                }
            });
        }
        return sendMessageBtn;
    }

    private TextArea getMessageTxt() {
        if (messageTxt == null) {
            messageTxt = new TextArea();

        }
        return messageTxt;
    }



}
