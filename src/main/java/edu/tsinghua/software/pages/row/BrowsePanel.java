package edu.tsinghua.software.pages.row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import edu.tsinghua.software.cassandra.unit.SColumn;


public class BrowsePanel extends Panel {

	transient private Map<String, Key> list;   //data list
	public BrowsePanel(String id, Map<String, Key> l,final String thKey, final String thValue) {
		super(id);
		setOutputMarkupId(true);
		list =  l;
		ArrayList<String> rowIdList = null;
		if(l!= null)
		{
			rowIdList = new ArrayList<String>(list.keySet());
		}
		else
		{
			rowIdList = new ArrayList<String>();
		}
		
		System.out.println("**********"+rowIdList);
		final DataView dataView = new DataView("dataBrowse", new ListDataProvider(rowIdList)) {
			private boolean alternateLine = true;  //change table css
            public void populateItem(final Item item) {
            	
            	if (alternateLine) { 
            		item.add(new SimpleAttributeModifier("class", "line1"));  // change the color ! 
                } else { 
                	item.add(new SimpleAttributeModifier("class", "line2")); 
                } 
                alternateLine = !alternateLine; 
            	 
                String rowID = (String) item.getModelObject();
                final Key key = list.get(rowID);  
                item.add(new Label("id", key.getName()));
                Label showAllColumnsLabel = new Label("showAllColumns", "test");
                showAllColumnsLabel.setOutputMarkupPlaceholderTag(true);
                showAllColumnsLabel.setVisible(false);
                item.add(showAllColumnsLabel);
                
                if(!key.isSuperColumn())
                {
                	String rowContent ="";
                	Map<String,Cell> cellSet = key.getCells();
      				Iterator cellIt = cellSet.keySet().iterator();
      				int i=0;  //only show first 3 columns 
      				while(cellIt.hasNext() && i<3)
      				{
      					String cellName = (String) cellIt.next();
    					Cell cell = cellSet.get(cellName);
    					String columnContent = cell.getValue().toString();  //if one row is too long , just show first 50 characters
    					if(columnContent.length()>60)
    					{
    						columnContent = columnContent.substring(0,60)+"...";
    					}
      					rowContent +="<u>"+cell.getName()+"</u>: "+columnContent;
      					rowContent+="\n";
      					i++;
      				}
      				
      				
      				if(rowContent.length()>60 || i>=3)   //if column number is bigger than 3, add a link <show all columns>
      				{
      					String linkTag = showAllColumnsLabel.getId();
      					AjaxLink showAllColumnsLink = new AjaxLink(linkTag)
      					{
							@Override
							public void onClick(AjaxRequestTarget target) {
								ColumnValuePanel showAllColumnPanel = new ColumnValuePanel(BrowsePanel.this.getId(), key.getCells(),list,thKey,thValue); 
								showAllColumnPanel.setOutputMarkupId(true);
								BrowsePanel.this.replaceWith(showAllColumnPanel);   
								target.add(showAllColumnPanel);
							}
      					};
      					showAllColumnsLabel.replaceWith(showAllColumnsLink);
      					
      				}
                	item.add(new MultiLineLabel("content",rowContent).setEscapeModelStrings(false));
                }
                else    
                {
                	String rowContent ="";
                	Map<String, SColumn> superColumnSet =  key.getSColumns();    //keyID and super columns
    				Iterator superColumnIt = superColumnSet.keySet().iterator();
    				while(superColumnIt.hasNext())
    				{
    					String superColumnName = (String) superColumnIt.next();  //get super columnName
    					SColumn superColumn = superColumnSet.get(superColumnName);  
    					final Map<String,Cell> cellSet = superColumn.getCells();  //get cells           
          				Iterator cellIt = cellSet.keySet().iterator();
          				rowContent+="<u>"+superColumnName+"</u>{ ";
          				while(cellIt.hasNext())
          				{
          					String cellName = (String) cellIt.next();
        					Cell cell = cellSet.get(cellName);
          					rowContent +="'"+cell.getName()+":"+cell.getValue()+"', ";
          				}
						rowContent += " }\n";						
						
    				}
                	item.add(new MultiLineLabel("content",rowContent).setEscapeModelStrings(false));
                }
            }
        };

        dataView.setItemsPerPage(10);
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
        add(new Label("thKey",thKey));
        add(new Label("thValue",thValue));
	}

}
