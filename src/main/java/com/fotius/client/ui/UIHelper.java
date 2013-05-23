package com.fotius.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class UIHelper {

    public static TextButton createToolbarBtn(String text, ImageResource icon, SelectEvent.SelectHandler handler) {
        TextButton btn = new TextButton(text, icon);
        btn.setScale(ButtonCell.ButtonScale.LARGE);
        btn.setIconAlign(ButtonCell.IconAlign.TOP);
        btn.addSelectHandler(handler);
        return btn;
    }

}
