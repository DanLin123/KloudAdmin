package edu.tsinghua.software.pages.row;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;


import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.unit.ColumnFamily;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.columnFamily.ColumnFamilyPage;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

/**InsertRow Page
 * @author 林丹
 * 
 */
public class InsertRow  extends BasePage{

	private static final long serialVersionUID = 1L;
	
	//for navigation
	String clusterName;
	String keyspaceName;
	String columnFamilyName; 	
	transient DataManager client =  new DataManager(getConnection());


	private class InputForm extends Form<Row>{
		private static final long serialVersionUID = 1L;
		/**
		 * InputForm Construct 
		 * @param name InputForm id
		 */
		@SuppressWarnings("serial")
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel<Row>(new Row()));
			//add textfield key
			add(new TextField<String>("key").setRequired(true));
			//add textField superFamily if type is super
			TextField<String> superFamily = new TextField<String>("scolumn");
			ColumnFamily columnFamily = null;
			try {
				columnFamily = client.getColumnFamilyBean(keyspaceName, columnFamilyName);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			System.out.print(columnFamily.getColumnType());
			if(columnFamily.getColumnType().equals("Standard"))
			{
				superFamily.setVisible(false);
			}
			add(superFamily);
			//add(new TextField<String>("column").setRequired(true));
			ArrayList<String> columns = null;
			try {
				columns = client.getColumns(keyspaceName, columnFamilyName);
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
			DropDownChoice column = new DropDownChoice("column",columns);
			column.setRequired(true);
			add(column);
		    add(new TextField<String>("value").setRequired(true));
	
	    	add(new Button("cancel"){

				@Override
	    		public void onSubmit()
	    		{
			
					// just set a new instance of the page
                    setResponsePage(InsertRow.class);
	    		}
	    	}.setDefaultFormProcessing(false));
	    	
		}
		  /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        public void onSubmit()
        {
        	if(getConnection().isConnected())
        	{
        		Row row = this.getModelObject();
        		//test for insert 
				Date date = null;
				try {
					
					date = client.insertColumn(keyspaceName, columnFamilyName, row.getKey(),row.getScolumn(), row.getColumn(), row.getValue());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(getSession().getLocale() == Locale.CHINA)
				{
					info("插入新记录成功"+date);
				}
				else if(getSession().getLocale() == Locale.US)
				{
					info("new row is add" +date);
				}
		
        	}
        }
	}
	
	/**
	 * InsertRow Construct
	 * @param pageParameters
	 * */
	public InsertRow(PageParameters pageParameters)
	{
		super();  
		//get params
		clusterName = pageParameters.get("clusterParam").toString();
		keyspaceName = pageParameters.get("keyspaceParam").toString();
		columnFamilyName = pageParameters.get("columnFamilyParam").toString();

	
		
		  //add navigation
		add(new BookmarkablePageLink<Void>("clusterNavigation",
				ClusterView.class).add(new Label("clusterName", clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNavigation",
				KeyspacePage.class, pageParameters).add(new Label(
				"keyspaceName", keyspaceName)));
		add(new BookmarkablePageLink<Void>("columnFamilyNavigation",
				ColumnFamilyPage.class, pageParameters).add(new Label(
				"columnFamilyName", columnFamilyName)));
		
		//add keyspace input form
		add(new InputForm("inputForm"));
		add(new FeedbackPanel("feedback"));
		
	
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
