package edu.tsinghua.software.pages.row;

import java.util.ArrayList;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import edu.tsinghua.software.cassandra.unit.Cell;
import edu.tsinghua.software.cassandra.unit.Key;

public class ColumnValuePanel extends Panel {
	Map<String,Cell> cellSet;
	transient final Map<String, Key> list;
	public ColumnValuePanel(String id,Map<String,Cell> cellSet1, Map<String, Key> list1,final String thKey, final String thValue) {
		super(id);
		setOutputMarkupId(true);
		this.cellSet = cellSet1;
		this.list = list1;
	    ArrayList<String> ColumnNames = new ArrayList<String>(cellSet.keySet());
	    
		final DataView dataView = new DataView("ColumnValue", new ListDataProvider(ColumnNames)) {
			private boolean alternateLine = true;  //change table css
			
            public void populateItem(final Item item) {
            	
            	if (alternateLine) { 
            		item.add(new SimpleAttributeModifier("class", "line1"));  // change the color ! 
                } else { 
                	item.add(new SimpleAttributeModifier("class", "line2")); 
                } 
                alternateLine = !alternateLine; 
            	 
                String columnName = (String) item.getModelObject();
                String columnValue = cellSet.get(columnName).getValue();
                item.add(new Label("name", columnName)).setEscapeModelStrings(false);  
                item.add(new MultiLineLabel("value",columnValue));
            }
        };
        
        dataView.setItemsPerPage(10);
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
        //back link
        AjaxLink backLink =  new AjaxLink("back"){
        	Panel toReplacePanel = (Panel) ColumnValuePanel.this;
			@Override
			public void onClick(AjaxRequestTarget target) {
				BrowsePanel browsePanel = new BrowsePanel(ColumnValuePanel.this.getId(),list,thKey, thValue); 
				browsePanel.setOutputMarkupId(true);
				toReplacePanel.replaceWith(browsePanel);   
				target.add(browsePanel);
				toReplacePanel = browsePanel;
		
			}
        	
        };
        add(backLink);
	}

}
