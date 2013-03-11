package edu.tsinghua.software.pages.template;
/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.util.Collection;

/**
 * Menu Bar panel
 * */
@SuppressWarnings("serial")
public class MenuDropdownItem extends Panel {

	public MenuDropdownItem(String id, MenuItemEnum currentMenuItem,
			Collection<BookmarkablePageLink<?>> linksInMenuItem, boolean shouldBeActive) {
		super(id);

		WebMarkupContainer itemContainer = new WebMarkupContainer("itemContainer");
		if (shouldBeActive) {
			itemContainer.add(new AttributeAppender("class", " active "));
		}
		itemContainer.add(new Label("label", currentMenuItem.getLabel()));

		RepeatingView repeatingView = new RepeatingView("itemLinks");

		for (BookmarkablePageLink<?> link : linksInMenuItem) {
			MenuLinkItem menuLinkItem = new MenuLinkItem(repeatingView.newChildId(), link, false);
			repeatingView.add(menuLinkItem);
		}

		itemContainer.add(repeatingView);
		add(itemContainer);
	}
}
