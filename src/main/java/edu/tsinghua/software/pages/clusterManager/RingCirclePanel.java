package edu.tsinghua.software.pages.clusterManager;

import java.util.ArrayList;

import org.apache.cassandra.dht.Token;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.odlabs.wiquery.ui.dialog.Dialog;

import com.googlecode.wicket.jquery.ui.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.dialog.MessageDialog;

import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;
import edu.tsinghua.software.reuseComponent.OperationModalWindow;

public class RingCirclePanel extends Panel {
	private OperationModalWindow moveTokenWindow;
	public RingCirclePanel(String id,final ArrayList<NodeInfo> nodes, final int jmxPort, final int thriftPort,
			AbstractAjaxBehavior nodesWebserviceJson)
	{
		super(id);	

        final Dialog dialog = new Dialog("dialog");
        add(dialog);
        
      //error dialog
		final MessageDialog errorDialog = new MessageDialog("errorDialog",
				"Error", "Can not connect to this server!", DialogButton.OK,
				DialogIcon.ERROR) {

			private static final long serialVersionUID = 1L;
			@Override
			protected void onClose(AjaxRequestTarget target, String button) {
			}
		};
		this.add(errorDialog);
        
        ListView nodeView = new ListView("nodes", nodes) {
			@Override
			protected void populateItem(ListItem item) {
				final NodeInfo nodeInfo = (NodeInfo) item.getModelObject();	
				AjaxLink nodeDetailLink = new AjaxLink("nodeDetail")
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
					
				};
				nodeDetailLink.setOutputMarkupId(true);
				nodeDetailLink.setMarkupId(nodeInfo.getToken().toString());
				item.add(nodeDetailLink);
			
			}
		};
		add(nodeView);
	
	}
}
