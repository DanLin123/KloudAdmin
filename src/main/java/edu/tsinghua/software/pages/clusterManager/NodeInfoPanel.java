package edu.tsinghua.software.pages.clusterManager;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import edu.tsinghua.software.cassandra.node.NodeInfo;

public class NodeInfoPanel extends Panel {
	

	public NodeInfoPanel(String id, NodeInfo nodeInfo) {
		super(id);
		add(new Label("endpoint",nodeInfo.getEndpoint()));
		Label rangeLabel = new Label("range",nodeInfo.getToken().toString());
		rangeLabel.setOutputMarkupId(true);
		rangeLabel.setMarkupId("node-info-pangel-range");
		add(rangeLabel);
		add(new Label("uptime",nodeInfo.getUptime()+""));
		add(new Label("memMax",nodeInfo.getMemMax()+""));
		add(new Label("memUsed",nodeInfo.getMemUsed()+""));
		add(new Label("dataCenter",nodeInfo.getDataCenter()));
		add(new Label("rack",nodeInfo.getRack()));
		add(new Label("status",nodeInfo.getStatus()));
		add(new Label("state",nodeInfo.getState()));
		add(new Label("load",nodeInfo.getLoad()));
		add(new Label("owns",nodeInfo.getOwns()));
	
	}

	private static final long serialVersionUID = 1L;

}
