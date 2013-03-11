package edu.tsinghua.software.pages.clusterManager;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.thrift.transport.TTransportException;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.odlabs.wiquery.ui.tabs.Tabs;

import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.node.RingNode;
import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

/**
 * cluster manager homepage, shhow nodering
 * */
public class RingView extends BasePage{
	private static final long serialVersionUID = 1L;
	transient ClusterManager clusterManager = new ClusterManager(getConnection());
	AbstractAjaxBehavior nodesWebserviceJson;
	
	
	@Override
	protected void onBeforeRender() {
		Label nodeInfo = new Label("nodesInfoJsonCallbackURL","");
		nodeInfo.add(new SimpleAttributeModifier("value", nodesWebserviceJson.getCallbackUrl().toString()));
		add(nodeInfo);
		super.onBeforeRender();
	}
	
	
	
	
	public RingView() throws TTransportException, IOException, InterruptedException
	{
		super();
		final ArrayList<NodeInfo> nodes= clusterManager.getNodeInfoList();
		int jmxPort = getConnection().getJmxPort();
		int thriftPort = getConnection().getThriftPort();
		Tabs tabs = new Tabs("tabs");
		add(tabs);
		tabs.add(new RingPanel("ringPanel", nodes,jmxPort,thriftPort));
		
		
		//ajaxbehavior
		nodesWebserviceJson = new AbstractAjaxBehavior(){
			public void onRequest()
		    {		
		        //get parameters
				RequestCycle requestCycle = RequestCycle.get();
		        
		        JSONArray objects = new JSONArray();
				for(NodeInfo nodeInfo : nodes)
				{
					JSONObject obj = new JSONObject();
					obj.put("token", nodeInfo.getToken());
					obj.put("ip", nodeInfo.getEndpoint());
					obj.put("range", nodeInfo.getRange());
					obj.put("jmx", nodeInfo.getJmx());
					obj.put("thrift", nodeInfo.getThrift());
					obj.put("uptime", nodeInfo.getUptime());
					obj.put("memMax", nodeInfo.getMemMax());
					obj.put("memUsed", nodeInfo.getMemUsed());
					obj.put("dataCenter", nodeInfo.getDataCenter());
					obj.put("rack", nodeInfo.getRack());
					obj.put("status", nodeInfo.getStatus());
					obj.put("state", nodeInfo.getState());
					obj.put("load", nodeInfo.getLoad());
					obj.put("owns", nodeInfo.getOwns());

					objects.add(obj);
				}
		        //valuewResponseJson(wr);
		        requestCycle.scheduleRequestHandlerAfterCurrent(
		        		new TextRequestHandler("application/json", 
		        		"UTF-8", objects.toJSONString()));
		    }
		};
		
		add(nodesWebserviceJson);
		
		tabs.add(new RingCirclePanel("ringCirclePanel", nodes,jmxPort,thriftPort, nodesWebserviceJson));
	}

	
	
	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
