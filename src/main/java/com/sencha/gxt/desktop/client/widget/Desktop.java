package com.sencha.gxt.desktop.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;

/**
 * A desktop represents a desktop like application which contains a task bar,
 * start menu, and shortcuts.
 * <p/>
 * Rather than adding content directly to the root panel, content should be
 * wrapped in windows. Windows can be opened via shortcuts and the start menu.
 * 
 * @see Shortcut
 */
public class Desktop implements IsWidget {

  private VBoxLayoutContainer desktop;
  private List<Shortcut> shortcuts;
  private VerticalLayoutContainer desktopContainer;
  private Viewport desktopViewport;

  /**
   * Creates a new Desktop window.
   */
  public Desktop() {
  }

  /**
   * Adds a shortcut to the desktop.
   * 
   * @param shortcut the shortcut to add
   */
  public void addShortcut(Shortcut shortcut) {
    getShortcuts().add(shortcut);
    getDesktop().add(shortcut, new BoxLayoutData(new Margins(5)));
  }

  @Override
  public Widget asWidget() {
    return getDesktopViewport();
  }

  /**
   * Returns the container of the "desktop", which is the area that contains the
   * shortcuts (i.e. minus the task bar).
   * 
   * @return the desktop layout container
   */
  public VBoxLayoutContainer getDesktop() {
    if (desktop == null) {
      desktop = new VBoxLayoutContainer();
      desktop.addStyleName("x-desktop");
      desktop.setPadding(new Padding(5));
      desktop.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCHMAX);
    }
    return desktop;
  }

  /**
   * Returns a list of the desktop's shortcuts.
   * 
   * @return the shortcuts
   */
  public List<Shortcut> getShortcuts() {
    if (shortcuts == null) {
      shortcuts = new ArrayList<Shortcut>();
    }
    return shortcuts;

  }

  /**
   * Removes a shortcut from the desktop.
   * 
   * @param shortcut the shortcut to remove
   */
  public void removeShortcut(Shortcut shortcut) {
    getShortcuts().remove(shortcut);
    getDesktop().remove(shortcut);
  }

  private VerticalLayoutContainer getDesktopContainer() {
    if (desktopContainer == null) {
      desktopContainer = new VerticalLayoutContainer() {
        @Override
        public void onResize() {
          super.onResize();
        }

        @Override
        protected void doLayout() {
          super.doLayout();
        }
      };
      desktopContainer.add(getDesktop(), new VerticalLayoutData(-1, 1));
    }
    return desktopContainer;
  }


  private Viewport getDesktopViewport() {
    if (desktopViewport == null) {
      desktopViewport = new Viewport();
      desktopViewport.add(getDesktopContainer());
    }
    return desktopViewport;
  }
}
