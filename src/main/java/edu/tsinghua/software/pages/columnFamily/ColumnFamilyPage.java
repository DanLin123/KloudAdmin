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

package edu.tsinghua.software.pages.columnFamily;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.cassandra.db.ColumnFamilyStoreMBean;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.odlabs.wiquery.ui.tabs.Tabs;

import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.unit.ColumnFamily;
import edu.tsinghua.software.cassandra.unit.ColumnFamilyMetaData;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.row.InsertRow;
import edu.tsinghua.software.pages.row.ShowAllData;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
import edu.tsinghua.software.reuseComponent.ConfirmLink;
/* * columnFamily page, show columnFamily setting info, index info
 * @author 林丹
 */
public class ColumnFamilyPage<E> extends BasePage {


	private static final long serialVersionUID = 1L;
	/**
	 * ColumnFamilyPage Construct
	 * 
	 * @param pageParameters
	 *            clusterParam keyspaceParam columnFamilyParam
	 * */
	String cfName;
	String ksName;
	String clusterName;
	ColumnFamily columnFamily = new ColumnFamily();
	ArrayList cfDetailList;
	transient DataManager client =  new DataManager(getConnection());
	transient ClusterManager clusterManager = new ClusterManager(getConnection());


    public ColumnFamilyPage(final PageParameters pageParameters) throws UnsupportedEncodingException, NotFoundException, TException, InvalidRequestException {
    	super();
    	init(pageParameters);
 
        //add nevigation
        add(new BookmarkablePageLink<Void>("clusterNevigation",ClusterView.class).add(new Label("clusterName",clusterName)));
        PageParameters kfParam = new PageParameters();  //use this param when nevigate from column family page to keyspace page
        kfParam.add("clusterParam", clusterName);
        kfParam.add("keyspaceParam", ksName);
        add(new BookmarkablePageLink<Void>("keyspaceNevigation",KeyspacePage.class,kfParam).add(new Label("ksName",ksName)));
        add(new Label("cfName", cfName));
        
    	//for mutilanguge
		String deleteStr = "";
		String truncateStr = "";
		String dropStr = "";
		if(getSession().getLocale() == Locale.US)
		{
			deleteStr = "Delete";
			truncateStr = "are you sure to truncate columnFamily";
			dropStr = "are you sure to drop ";
		}
		else if (getSession().getLocale() == Locale.CHINA)
		{
			deleteStr = "删除";
			truncateStr = "确定清空表";
			dropStr = "确定要删除表";
		}
       
//      add button groups      
        add( new AjaxLink( "editColumnFamilyLink" ){

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(EditColumnFamily.class,pageParameters);
				
			}

          	} ); 
        add( new AjaxLink( "browseDataLink" ){

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ShowAllData.class,pageParameters);
			}

      	} ); 
        
        add( new AjaxLink( "createIndexLink" ){

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(SecondaryIndexPanel.class,pageParameters);
				
			}

      	} ); 
        add( new AjaxLink( "insertRowLink" ){

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(InsertRow.class,pageParameters);
				
			}

      	} ); 
        
        ConfirmLink deleteColunnFamilyButton = new ConfirmLink("dropColumnFamilyLink",dropStr +" "+ this.cfName) {
			@Override
			public void onClick() {
				try {
					client.dropColumnFamily(ksName, cfName);
					setResponsePage(KeyspacePage.class,pageParameters);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

		}; 
		
    	deleteColunnFamilyButton.add(new AttributeModifier("value", new Model(deleteStr+" '" + this.cfName+"'")));
    	add(deleteColunnFamilyButton);
         
    	// truncateColumnFamily
		add(new ConfirmLink("truncateColumnFamilyLink",truncateStr) {
			@Override
			public void onClick() {
				try {
				    client.truncateColumnFamily(ksName, cfName);
				} catch (InvalidRequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
     
		
		ColumnFamilyStoreMBean cfstore = clusterManager.getColumnFamilyStatics(ksName,cfName);	
		
		Tabs tabs = new Tabs("tabs");
	    tabs.add(new CfInfoPanel("cfInfo",columnFamily));
	    tabs.add(new CfStatics("cfStatics",cfstore, ksName, clusterManager));
	    add(tabs);
		// index infomation
		final List<ColumnFamilyMetaData> columnFamilyMetaList = columnFamily.getMetaDatas();
		final PageableListView indexView = new PageableListView("indexRows",columnFamilyMetaList, 20) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final ColumnFamilyMetaData meta = (ColumnFamilyMetaData)item.getModelObject();
				if(meta.getColumnName()!=null)	
				{
					item.add(new Label("columnName",meta.getColumnName()));
				}
				else
				{
					item.add(new Label("columnName",""));
				}
				
				if(meta.getValiDationClass()!=null)
				{
					item.add(new Label("validationClass",meta.getValiDationClass()));
				}
				else
				{
					item.add(new Label("validationClass",""));
				}
				
			
				if(meta.getIndexName()!=null)
				{
					item.add(new Label("indexName",meta.getIndexName()));
				}
				else
				{
					item.add(new Label("indexName",""));
				}
				//if ther is no validation class , show nothing
				if(meta.getIndexType()!=null)
				{
					item.add(new Label("indexType",meta.getIndexType().toString()));
				}
				else
				{
					item.add(new Label("indexType",""));
				}
				
			/*    item.add(new ConfirmLink("deleteIndex", "Are you sure to delete Index?"){
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
							columnFamilyMetaList.remove(meta);
							columnFamily.setMetaDatas(columnFamilyMetaList);
							try {
								client.updateColumnFamily(ksName, columnFamily);
							} catch (InvalidRequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (TException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SchemaDisagreementException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							setResponsePage(ColumnFamilyPage.class,pageParameters);
						}
						
			    });*/
							
			}
		};
		indexView.setOutputMarkupId(true);
		this.add(indexView);
			
    }
    
    void init(PageParameters pageParameters) throws UnsupportedEncodingException, NotFoundException, TException, InvalidRequestException
    {
    	
		clusterName = pageParameters.get("clusterParam").toString();
		ksName = pageParameters.get("keyspaceParam").toString();
		cfName = pageParameters.get("columnFamilyParam").toString();
		columnFamily = client.getColumnFamilyBean(ksName, cfName);  
   
    }

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
