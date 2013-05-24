package com.fotius.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ExampleImages extends ClientBundle {

  public ExampleImages INSTANCE = GWT.create(ExampleImages.class);

  @Source("add16.gif")
  ImageResource add16();

  @Source("add24.gif")
  ImageResource add24();

  @Source("add32.gif")
  ImageResource add32();

    @Source("arrow_up_green.gif")
    ImageResource arrow_up();

    @Source("arrow_down_green.gif")
    ImageResource arrow_down();
  @Source("edit24.gif")
  ImageResource edit24();

  @Source("delete24.gif")
  ImageResource delete24();

  @Source("table.png")
  ImageResource table();

  @Source("application_side_list.png")
  ImageResource side_list();
  
  @Source("list.gif")
  ImageResource list();

  @Source("application_form.png")
  ImageResource form();

  @Source("connect.png")
  ImageResource connect();

  @Source("user_add.png")
  ImageResource user_add();

  @Source("user_delete.png")
  ImageResource user_delete();

  @Source("accordion.gif")
  ImageResource accordion();

  @Source("add.gif")
  ImageResource add();

  @Source("delete.gif")
  ImageResource delete();

  @Source("calendar.gif")
  ImageResource calendar();

  @Source("menu-show.gif")
  ImageResource menu_show();

  @Source("list-items.gif")
  ImageResource list_items();

  @Source("lock.gif")
  ImageResource lock();

  @Source("album.gif")
  ImageResource album();

  @Source("text.png")
  ImageResource text();

  @Source("user.png")
  ImageResource user();

  @Source("user32.gif")
  ImageResource user32();

  @Source("users32.gif")
  ImageResource users32();

  @Source("users_add24.gif")
  ImageResource users_add_24();

  @Source("users_edit24.gif")
  ImageResource users_edit_24();

  @Source("users_delete24.gif")
  ImageResource users_delete_24();

  @Source("css.png")
  ImageResource css();
  
  @Source("java.png")
  ImageResource java();

  @Source("html.png")
  ImageResource html();
  
  @Source("xml.png")
  ImageResource xml();
  
  @Source("folder.png")
  ImageResource folder();

  @Source("user2.png")
  ImageResource hp();

  @Source("group.png")
  ImageResource group();

  @Source("user_small.png")
  ImageResource hp_small();

  @Source("group_small.png")
  ImageResource group_small();


}
