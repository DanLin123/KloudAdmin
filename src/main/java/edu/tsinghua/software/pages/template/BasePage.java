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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.thrift.TException;

import edu.tsinghua.software.cassandra.dataStatics.DataStatics;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.clusterManager.RingView;
import edu.tsinghua.software.pages.cql.CqlPage;
import edu.tsinghua.software.pages.monitor.Monitor;


/**
 * BasePage,left frame implements this class
 * @author 林丹
 * */
public abstract class BasePage extends KloudAdminPage {
	private String clusterName;
	private ArrayList<String> keyspaceNameList = new ArrayList<String>();
	transient DataManager client =  new DataManager(getConnection());
	
	
	public BasePage() {			
		init();
		add(new LeftPanel("panelLeft", clusterName, keyspaceNameList,client));
		if(getSession().getLocale() == Locale.US)
		{
			  add(new TwitterBootstrapNavBarPanel.Builder("navBar", ClusterView.class, "Kloud Admin", getActiveMenu())
	            .withMenuItem(MenuItemEnum.DATAADMIN, ClusterView.class)
	            .withMenuItem(MenuItemEnum.CLUSTERADMIN, RingView.class)
	            .withMenuItem(MenuItemEnum.CQL, CqlPage.class)
	            .withMenuItem(MenuItemEnum.MONITOR, Monitor.class)
	            .build());
		}
		else if (getSession().getLocale() == Locale.CHINA)
		{
			 add(new TwitterBootstrapNavBarPanel.Builder("navBar", ClusterView.class, "Kloud Admin", getActiveMenu())
	         .withMenuItem(MenuItemEnum.DATAADMIN_zh_CN, ClusterView.class)
	         .withMenuItem(MenuItemEnum.CLUSTERADMIN_zh_CN, RingView.class)
	         .withMenuItem(MenuItemEnum.CQL_zh_CN, CqlPage.class)
	         .withMenuItem(MenuItemEnum.MONITOR_zh_CN, Monitor.class)
	         .withMenuItem(MenuItemEnum.DATASTATICS_zh_CN, DataStatics.class)
	         .build());
		}
	
   }

	void init()
	{
		try {
			clusterName = client.describeClusterName();
			List<KsDef> ksDefList = client.getKeyspaces();
			Iterator it = ksDefList.iterator();
			while(it.hasNext())
			{
				KsDef ksDef = (KsDef) it.next();
				keyspaceNameList.add(ksDef.name);
			}
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    public abstract MenuItemEnum getActiveMenu();
    
}
