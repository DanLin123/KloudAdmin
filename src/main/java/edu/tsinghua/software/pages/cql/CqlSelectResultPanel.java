package edu.tsinghua.software.pages.cql;

import java.util.ArrayList;
import java.util.Map;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;


public class CqlSelectResultPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient private Map<String, String> contentList;   //data list
	public CqlSelectResultPanel(String id,Map<String, String> content) {
		super(id);
		setOutputMarkupId(true);
		contentList =  content;
		ArrayList<String>  contentArrayList= new ArrayList<String>();
		//map tp arraylist
		for(Map.Entry<String, String> entry : contentList.entrySet())
		{
			contentArrayList.add(entry.getKey()+": "+entry.getValue());
		}
		
		
		final DataView dataView = new DataView("selectBrowse", new ListDataProvider(contentArrayList)) {
			private boolean alternateLine = true;  //change table css
			
            public void populateItem(final Item item) {
            	
            	if (alternateLine) { 
            		item.add(new SimpleAttributeModifier("class", "line1"));  // change the color ! 
                } else { 
                	item.add(new SimpleAttributeModifier("class", "line2")); 
                } 
                alternateLine = !alternateLine; 
                
                //content for one row
                String rowContent = item.getModelObject().toString();
                item.add(new MultiLineLabel("content",rowContent).setEscapeModelStrings(false));
      		
            }
        };

        dataView.setItemsPerPage(10);
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
	}

}
