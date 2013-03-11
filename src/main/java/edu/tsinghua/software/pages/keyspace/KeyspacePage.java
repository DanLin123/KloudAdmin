package edu.tsinghua.software.pages.keyspace;

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

import java.util.Locale;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.columnFamily.AddColumnFamily;
import edu.tsinghua.software.pages.columnFamily.ColumnFamilyPage;
import edu.tsinghua.software.pages.columnFamily.DeleteColumnFamilyPage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
import edu.tsinghua.software.reuseComponent.ConfirmLink;
/* * 
 * keyspace page which contains the detail of one keyspace, and list column family in this keyspace
 * @author 林丹
 */
public class KeyspacePage extends BasePage {
	String keyspaceName = ""; // keyspace name of this page
	String clusterName = "";
	transient KsDef ksDef = new KsDef(); // keyspace object of this page
	transient DataManager client;
	transient ClusterManager clusterManager;

	private static final long serialVersionUID = 1L;

	/**
	 * KeyspacePage Contruct
	 * @param pageParameters
	 * @throws InvalidRequestException 
	 * @throws TException 
	 * @throws NotFoundException 
	 * */
	public KeyspacePage(final PageParameters pageParameters) throws NotFoundException, TException, InvalidRequestException {
		super();
		init(pageParameters);

		// add neviation, cluster>>name
		add(new BookmarkablePageLink<Void>("clusterNevigation",
				ClusterView.class).add(new Label("clusterName", clusterName)));
		add(new Label("keyspaceName", keyspaceName));

		ConfirmLink deleteKeyspaceButton = new ConfirmLink(
				"deleteKslinkButton", "确定要删除 " +this.keyspaceName+"?") {
			@Override
			public void onClick() {
				try {
					client.dropKeyspace(keyspaceName);
					setResponsePage(new KeyspacePage(pageParameters));
				} catch (Exception e) {
					e.printStackTrace();
				}
				setResponsePage(ClusterView.class, pageParameters);

			}
		};
		
		//for mutiple languge
		String deleteKsStr;
		if(getSession().getLocale() == Locale.US)
		{
			deleteKsStr="Delete";
		}
		else
		{
			deleteKsStr="删除";
		}
		deleteKeyspaceButton.add(new AttributeModifier("value", new Model(deleteKsStr+" '"
				+ this.keyspaceName+"'")));
		add(deleteKeyspaceButton);
		
		add(new AjaxLink("editKslinkButton") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(EditKeyspace.class, pageParameters);

			}
		});
		add(new AjaxLink("addCflinkButton") {

		@Override
		public void onClick(AjaxRequestTarget target) {
			setResponsePage(AddColumnFamily.class,pageParameters);
			
		} 
      	} ); 
        // delete ColumnFamily button
    		add(new AjaxLink("deleteCflinkButton") {
    			@Override
    			public void onClick(AjaxRequestTarget target) {
    				setResponsePage(DeleteColumnFamilyPage.class, pageParameters);
    			}
    		});

    	//columnFamily List
		ListView cfView = new ListView("cfList", new CfListModel(this.keyspaceName,this.client)) {
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {
				PageParameters params = new PageParameters();
				params.add("columnFamilyParam", item.getModelObject().toString());
				params.add("keyspaceParam", keyspaceName);
				params.add("clusterParam",clusterName);
				item.add(new BookmarkablePageLink<Void>("cfLink",
						ColumnFamilyPage.class, params).add(new Label("cfName",item.getModel())));
			}
		};
		
		add(cfView);
		add(new KeyspaceInfoPanel("kespaceInfo",ksDef));
		Map<String,String> keyspaceStatics = clusterManager.getKeyspaceStatics(keyspaceName);
		add(new KeyspaceStaticsPanel("kespaceStatic",keyspaceStatics));
		
	}
	
	 private void init(PageParameters pageParameters) throws NotFoundException, InvalidRequestException, TException {
			// get the paramkeyspace
			keyspaceName = pageParameters.get("keyspaceParam").toString();
			clusterName = pageParameters.get("clusterParam").toString();
			client =  new DataManager(getConnection());
			clusterManager = new ClusterManager(getConnection());
			ksDef = client.describeKeyspace(keyspaceName);
		
	}
	

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
