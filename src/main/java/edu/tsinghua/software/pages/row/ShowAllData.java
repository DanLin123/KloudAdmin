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


import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;

import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.unit.Cell;
import edu.tsinghua.software.cassandra.unit.Key;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.columnFamily.ColumnFamilyPage;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
/**
 * ShowAllData pages
 * @author 林丹
 * */
public class ShowAllData extends BasePage {
	String keyspace = new String();
	String columnFamily = new String();
	String clusterName = new String();
	transient ClusterManager clusterManager ;
	transient DataManager client =  new DataManager(getConnection());
	
	
	/**
	 * ShowAllData Construct
	 * @param pageParameters
	 * */
	public ShowAllData(PageParameters pageParameters) {
		super();
        try {
			clusterManager = new ClusterManager(getConnection());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
        
		//get and show data
		clusterName = pageParameters.get("clusterParam").toString();
		keyspace = pageParameters.get("keyspaceParam").toString();
		columnFamily = pageParameters.get("columnFamilyParam").toString();
		
		  //add navigation
		add(new BookmarkablePageLink<Void>("clusterNavigation",
				ClusterView.class).add(new Label("clusterName", clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNavigation",
				KeyspacePage.class, pageParameters).add(new Label(
				"keyspaceName", keyspace)));
		add(new BookmarkablePageLink<Void>("columnFamilyNavigation",
				ColumnFamilyPage.class, pageParameters).add(new Label(
				"columnFamilyName", columnFamily)));
		final FeedbackPanel feedBack = new FeedbackPanel("feedback");
		feedBack.setOutputMarkupId(true);
		add(feedBack);
		
		//show Number of Keys (estimate)
		int number = clusterManager.getEstimateRowNumber(keyspace, columnFamily);
		Label numberOfKeysLabel = new Label("numberofKeysLabel","Number of Keys (estimate): " +number);
		add(numberOfKeysLabel);
		
		try {

			final Map<String, Key> l = client.listKeyAndValues(keyspace,columnFamily, "", "", 50);
			Panel browsePanel = new BrowsePanel("browseDataPanel",l,"Key","Columns"){};
			add(browsePanel);
			
			//search field
			final TextField keyField = new TextField<String>("key", new Model(""));
		
			Form keyForm = new Form<String>("keyForm") {};
			AjaxButton searchButton = new AjaxButton("searchButton")
			{		
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					Panel toReplacePanel = (Panel) ShowAllData.this.get("browseDataPanel");     //get current panel, which will be replaced based on key 
				

					if(keyField.getModelObject()==null)
					{
						target.add(feedBack);
						info("关键字不能为空");
					}
					else
					{
						target.add(feedBack);
						String keyValue = keyField.getModelObject().toString();
						try {
							Map<String, Key> searchResult = client.getKey(keyspace, columnFamily, "", keyValue);
							Panel resultPanel = null;
							if(searchResult.size()!=0)
							{
								resultPanel = new ColumnValuePanel("browseDataPanel", searchResult.get(keyValue).getCells(),l,"Key", "Columns"); 
							}
							else
							{
								Map<String,Cell> noData = new HashMap<String,Cell>();
								noData.put("No Data Found", new Cell());
								resultPanel = new ColumnValuePanel("browseDataPanel",noData, l,"Key","Columns");
							}
							resultPanel.setOutputMarkupId(true);
							toReplacePanel.replaceWith(resultPanel);   
							target.add(resultPanel);
							toReplacePanel = resultPanel;
							
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}
					
				}
				
			};
			keyForm.add(searchButton);
			keyForm.add(keyField);
			add(keyForm);	
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
		
	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
