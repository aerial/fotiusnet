package com.fotius.client.ui;

import com.fotius.client.Resources;
import com.fotius.client.Session;
import com.fotius.client.model.MessageProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Message;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MessagesWindow extends BaseGridWindow<Message, MessageProperties> {

    private static MessagesWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private final MessageProperties props = GWT.create(MessageProperties.class);
    private TextButton sendMessageBtn;
    private GridView view;
    private List<ColumnConfig<Message, ?>> columnConfigs;
    private RowExpander<Message> expander;


    public MessagesWindow(String title, ImageResource icon) {
        super(title, icon);
    }


    public static MessagesWindow getInstance() {
        if (instance == null) {
            instance = new MessagesWindow("Messages", Resources.IMAGES.group_small());
        }
        return instance;
    }

    @Override
    public void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Message>> callback) {
        fotiusService.getInbox(Session.currentUser, loadConfig, callback);
    }

    @Override
    public List<ColumnConfig<Message, ?>> getColumnConfigs() {
        if (columnConfigs == null) {
            ColumnConfig<Message, String> nameColumn = new ColumnConfig<Message, String>(getProperties().senderName(), 150, "sender");
            ColumnConfig<Message, String> textColumn = new ColumnConfig<Message, String>(getProperties().subject(), 150, "subject");
            getView().setAutoExpandColumn(textColumn);
            ColumnConfig<Message, Date> dateColumn = new ColumnConfig<Message, Date>(getProperties().date(), 150, "date");
            columnConfigs = new ArrayList<ColumnConfig<Message, ?>>();
            columnConfigs.add(getExpander());
            columnConfigs.add(nameColumn);
            columnConfigs.add(textColumn);
            columnConfigs.add(dateColumn);

        }

        return columnConfigs;
    }

    public RowExpander<Message> getExpander() {
        if (expander == null) {
            IdentityValueProvider<Message> identity = new IdentityValueProvider<Message>();
            expander = new RowExpander<Message>(identity, new AbstractCell<Message>() {
                @Override
                public void render(Context context, Message value, SafeHtmlBuilder sb) {
                    sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Subject:</b> " + value.getSubject() + "</p>");
                    sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Text:</b> " + value.getText());
                }
            });

        }
        return expander;
    }

    @Override
    public MessageProperties getProperties() {
        return props;
    }

    @Override
    public ModelKeyProvider<Message> getModelKey() {
        return props.messageId();
    }

    @Override
    public void editEntity() {
        ViewMessageWindow.getInstance().fillForm(getSelectedEntity());
        ViewMessageWindow.getInstance().show();
    }

    @Override
    public GridView getView() {
        if (view == null) {
            view = new GridView();
        }
        return view;
    }

    @Override
    public Grid getGrid() {
        Grid grid = super.getGrid();
        getExpander().initPlugin(grid);
        return grid;
    }


    @Override
    public List<TextButton> getToolbarButtons() {
        return Arrays.asList(getSendMessageBtn());
    }

    private TextButton getSendMessageBtn() {
        if (sendMessageBtn == null) {
            sendMessageBtn = UIHelper.createToolbarBtn("Compose message", Resources.IMAGES.messages_medium(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    ComposeMessageWindow.getInstance().show();
                }
            });
        }
        return sendMessageBtn;
    }
}
