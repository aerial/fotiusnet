package com.fotius.client.ui;

import com.fotius.client.Resources;


/**
 * Created by vvinnik on 11/22/13.
 */

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import java.util.logging.Logger;

public class DocumentUploadWindow extends Window {

    public static DocumentUploadWindow instance;
    private FramedPanel panel;
    private static Logger log = Logger.getLogger("");
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                IUploader.UploadedInfo info = uploader.getServerInfo();
                Info.display(info.name, info.ctype + " " + info.size);
                Info.display("Url", uploader.fileUrl());
                log.warning("Url " +  uploader.fileUrl());
                log.warning("INFO " +  uploader.getServerInfo().field);
            }
        }
    };

    public DocumentUploadWindow(String title, ImageResource icon) {
        getHeader().setIcon(icon);
        setHeadingText(title);
        setMaximizable(true);
        setMinimizable(true);
        setPixelSize(600, 500);
    }

    public static DocumentUploadWindow getInstance() {
        if (instance == null) {
            instance = new DocumentUploadWindow("Test", Resources.IMAGES.messages_small());
        }
        return instance;
    }

    @Override
    public void show() {
        if (panel == null) {
            panel = new FramedPanel();
            panel.setHeadingText("File Upload Example");
            panel.setButtonAlign(BoxLayoutPack.CENTER);
            panel.setWidth(350);
            panel.getElement().setMargins(10);

            final FormPanel form = new FormPanel();
            form.setAction("fotiusnet/fileupload");
            form.setEncoding(Encoding.MULTIPART);
            form.setMethod(Method.POST);
            panel.add(form);

            VerticalLayoutContainer p = new VerticalLayoutContainer();
            form.add(p);

            TextField firstName = new TextField();
            firstName.setAllowBlank(false);
            p.add(new FieldLabel(firstName, "Name"), new VerticalLayoutData(-18, -1));

            final FileUploadField file = new FileUploadField();
            file.addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    Info.display("File Changed", "You selected " + file.getValue());
                }
            });
            file.setName("uploadedfile");
            file.setAllowBlank(false);

            p.add(new FieldLabel(file, "File"), new VerticalLayoutData(-18, -1));

            TextButton btn = new TextButton("Reset");
            btn.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    form.reset();
                    // TODO needs to be part of form panel, ie a Field interface
                    file.reset();
                }
            });

            panel.addButton(btn);

            btn = new TextButton("Submit");
            btn.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    if (!form.isValid()) {
                        return;
                    }
                    form.submit();
                    MessageBox box = new MessageBox("File Upload Example", "Your file was uploaded.");
                    box.setIcon(MessageBox.ICONS.info());
                    box.show();
                }
            });
            panel.addButton(btn);
        }
        MultiUploader uploader = new MultiUploader();
        uploader.addOnFinishUploadHandler(onFinishUploaderHandler);
        add(uploader);
//        add(panel);
        super.show();
    }

}
