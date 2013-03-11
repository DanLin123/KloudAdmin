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

package edu.tsinghua.software.pages.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.dht.Token;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.node.RingNode;
import edu.tsinghua.software.pages.keyspace.AddKeyspace;
import edu.tsinghua.software.pages.keyspace.DeleteKeyspacePage;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;


/**
 * file:ClusterView.java
 * cluster view Page:show cluster setting info, ring , and keyspace list
 * created at :11-03-2012
 * @author 林丹
 * */

public class ClusterView extends BasePage{
	private static final long serialVersionUID = 1L;
	transient DataManager client =  new DataManager(getConnection());
	            //client which is responsible to comunicate with cassandra
	static PageParameters pageParameters =  new PageParameters();
	ArrayList<String> ksList = new ArrayList<String>(); //list of keyspace name , which is bounded to ksView
	private ArrayList<String> keyspaceNameList =   new ArrayList<String>();
	String clusterName = new String();
 
	/**
	 * Construtor
	 * @throws TException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public ClusterView() throws TException, IOException, InterruptedException {
		super();
		inti();
		
		//add keyspace button
		add( new AjaxLink( "deleteKsLinkButton" ){ 
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(DeleteKeyspacePage.class,pageParameters);
			} 
	    	} ); 
		
		//delete keyspace button
		add( new AjaxLink( "addKsLinkButton" ){ 
			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(AddKeyspace.class,pageParameters);
			} 
	    	} ); 
		
		// cluster detials
		final ClusterPropertyModel clusterProperty = new ClusterPropertyModel();  
		this.setClusterProperties(clusterProperty);
		add(new Label("name", clusterProperty.getName()));
		add(new Label("snitch", clusterProperty.getSnitch()));
		add(new Label("partitioner", clusterProperty.getPatitioner()));
		add(new Label("schema", clusterProperty.getSchema()));
		add(new Label("api", clusterProperty.getApi()));
		add(new Label("keyspaceNum", clusterProperty.getKeyspace()));


		// keyspace viewList
		ListView keyspaceView = new ListView("keyspaceList", keyspaceNameList) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			protected void populateItem(ListItem item) {
				PageParameters keyspaceParameters = new PageParameters();
				keyspaceParameters.add("clusterParam",clusterName);
				keyspaceParameters.add("keyspaceParam", item.getModelObject().toString());
				item.add(new BookmarkablePageLink<Void>("keyspaceLink",
						KeyspacePage.class, keyspaceParameters).add(new Label("keyspaceName",
						item.getModel())));
			}
			
		};
		add(keyspaceView);
		
		
	}
	
/**
 * init the parameters 
 * @throws InterruptedException 
 * @throws IOException 
 * */
	private void inti() throws TException, IOException, InterruptedException {
		clusterName = client.describeClusterName();
		//init parameters
		pageParameters.add("clusterParam", clusterName);
		List<KsDef> keyspaceList = new ArrayList<KsDef>();
		try {
			keyspaceList = client.getKeyspaces();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (KsDef keyspace : keyspaceList) {
			keyspaceNameList.add(keyspace.getName());
		}
		
	}


	// setClusterProperties to ClusyerPropertyModel
	public  ClusterPropertyModel setClusterProperties(ClusterPropertyModel clusterProperty) throws TTransportException, IOException, InterruptedException {
		if (client != null && getConnection().isConnected()) {
			try {
				clusterProperty.setName(client.describeClusterName());
				clusterProperty.setSnitch(client.describeSnitch());
			    clusterProperty.setPatitioner(client.describePartitioner());
			    clusterProperty.setSchema(client.describeSchemaVersions().toString());
			    clusterProperty.setApi(client.descriveVersion());
			    clusterProperty.setKeyspace(client.getKeyspaces().size()+"");
			    

			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return clusterProperty;

	}


	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}
