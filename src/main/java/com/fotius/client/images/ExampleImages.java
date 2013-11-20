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

  @Source("edit24.gif")
  ImageResource edit24();

  @Source("delete24.gif")
  ImageResource delete24();

  @Source("user_delete.png")
  ImageResource user_delete();

  @Source("add.gif")
  ImageResource add();

  @Source("lock.gif")
  ImageResource lock();

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

  @Source("gnome-session.png")
  ImageResource hp();

  @Source("student.png")
  ImageResource student();

  @Source("stock_people.png")
  ImageResource group();

  @Source("user_small.png")
  ImageResource hp_small();

  @Source("group_small.png")
  ImageResource group_small();

  @Source("f_messages_64.png")
  ImageResource messages_big();

  @Source("f_messages_24.png")
  ImageResource messages_medium();

  @Source("f_messages_16.png")
  ImageResource messages_small();
}
