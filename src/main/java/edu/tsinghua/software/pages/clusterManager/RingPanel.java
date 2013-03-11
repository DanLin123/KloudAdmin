package edu.tsinghua.software.pages.clusterManager;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.thrift.transport.TTransportException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.googlecode.wicket.jquery.ui.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.dialog.MessageDialog;

import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;

public class RingPanel extends Panel {

	public RingPanel(String id,ArrayList<NodeInfo> nodes, final int jmxPort, final int thriftPort) {
	//public RingPanel(String id,ArrayList<NodeInfo> nodes) {
		super(id);
		
		//error dialog
		final MessageDialog errorDialog = new MessageDialog("errorDialog", "Error", "Can not connect to this server!", DialogButton.OK, DialogIcon.ERROR) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClose(AjaxRequestTarget target, String button)
			{
			}
		};
		this.add(errorDialog);
		
		PageableListView nodeView = new PageableListView("rows", nodes, 30) {
			@Override
			protected void populateItem(ListItem item) {
				final NodeInfo nodeInfo = (NodeInfo) item.getModelObject();		
			
				item.add(new Label("address", nodeInfo.getEndpoint()));
				item.add(new Label("load", nodeInfo.getLoad()));
				item.add(new Label("token", nodeInfo.getToken()+""));
				item.add(new Label("status", nodeInfo.getStatus()+""));
				item.add(new Label("state", nodeInfo.getState()+""));
			
				
				item.add(new AjaxLink("nodeDetail")
				{
					@Override
					public void onClick(AjaxRequestTarget target) {
						
						ClusterConnection connection = new ClusterConnection(nodeInfo.getEndpoint(), thriftPort, jmxPort);
						try {
							connection.connect();
						} catch (Exception e) {
							errorDialog.open(target);
							System.out.print(e);
						}
						if(connection.isConnected())
						{
							setResponsePage(new NodeView(nodeInfo, connection));
						}				

					}
					
				});
			
			}
		};
		add(nodeView);
		add(new PagingNavigator("navigator", nodeView));
	
	}
	
	
	
	private static final long serialVersionUID = 1L;

}
