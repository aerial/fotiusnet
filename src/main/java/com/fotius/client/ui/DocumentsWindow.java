package com.fotius.client.ui;

import com.fotius.client.FotiusnetConstants;
import com.fotius.client.Resources;
import com.fotius.client.model.DocumentProperties;
import com.fotius.client.service.FotiusService;
import com.fotius.client.service.FotiusServiceAsync;
import com.fotius.shared.model.Document;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentsWindow extends BaseGridWindow<Document, DocumentProperties> {

    private static DocumentsWindow instance = null;
    private final FotiusServiceAsync fotiusService = GWT.create(FotiusService.class);
    private final DocumentProperties props = GWT.create(DocumentProperties.class);
    private static FotiusnetConstants constants = GWT.create(FotiusnetConstants.class);
    private GridView<Document> view;
    private TextButton uploadButton;

    public DocumentsWindow(String title, ImageResource icon) {
        super(title, icon);
    }


    public static DocumentsWindow getInstance() {
        if (instance == null) {
            instance = new DocumentsWindow("Documents", Resources.IMAGES.group_small());
        }
        return instance;
    }

    @Override
    public void loadData(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Document>> callback) {

    }

    @Override
    public List<ColumnConfig<Document, ?>> getColumnConfigs() {
        ColumnConfig<Document, String> fileNameColumn = new ColumnConfig<Document, String>(getProperties().name(), 150, "Filename");
        getView().setAutoExpandColumn(fileNameColumn);
        ColumnConfig<Document, Integer> sizeColumn = new ColumnConfig<Document, Integer>(getProperties().size(), 150, "Size");
        List<ColumnConfig<Document, ?>> l = new ArrayList<ColumnConfig<Document, ?>>();
        l.add(fileNameColumn);
        l.add(sizeColumn);
        return l;
    }

    @Override
    public DocumentProperties getProperties() {
        return props;
    }

    @Override
    public ModelKeyProvider<Document> getModelKey() {
        return props.documentId();
    }

    @Override
    public void editEntity() {
        DocumentUploadWindow.getInstance().show();
        Info.display("Edit document", "Edit document");
    }

    @Override
    public GridView getView() {
        if (view == null) {
            view = new GridView();
        }
        return view;
    }

    @Override
    public List<TextButton> getToolbarButtons() {
        return Arrays.asList(getUploadButton());
    }

    public TextButton getUploadButton() {
        if (uploadButton == null) {
            uploadButton = UIHelper.createToolbarBtn("Upload", Resources.IMAGES.upload(), new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent selectEvent) {
                    editEntity();
                }

            });
        }
        return uploadButton;

    }

}
