package edu.tsinghua.software.pages.clusterManager;

import java.util.ArrayList;

import org.apache.cassandra.tools.NodeCmd;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.http.WebRequest;
import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;
import edu.tsinghua.software.cassandra.tools.ClusterManager;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
import edu.tsinghua.software.reuseComponent.OperationModalWindow;


/**
 * detail information of a node
 * operations on a node
 * */
public class NodeView extends BasePage {
	private static final long serialVersionUID = 1L;
	private AjaxJsonBehavior executeMoveTokenBehaviour;
	private OperationModalWindow moveTokenWindow;
	private NodeInfoPanel nodeInfoPanel;
	ArrayList<String> keyspaces; 
	ArrayList<AjaxJsonBehavior> behavious;
	String[] confirmButtons = {"drain","decommission","gc","cleanUp","flush","scrub","repair","compaction"};
	
	NodeInfo nodeInfo;
	int jmxPort = getConnection().getJmxPort();
	
	//NodeProxy for this node
	transient ClusterManager clusterManager;
	transient DataManager dataManager = new DataManager(getConnection());

	@Override
	protected void onBeforeRender() {
		//called before the component is rendered
		String callbackUrl = executeMoveTokenBehaviour.getCallbackUrl().toString();
		moveTokenWindow.add(new AttributeModifier("executeMoveTokenURL", callbackUrl));
		super.onBeforeRender();
	}
 
	public NodeView(NodeInfo nodeInfo,ClusterConnection connection){
		this.nodeInfo = nodeInfo;
		try {
			clusterManager = new ClusterManager(connection,
					nodeInfo.getEndpoint(), jmxPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyspaces = this.dataManager.getKeyspacesName();
		keyspaces.remove("system");
		//add navigatio
		add(new BookmarkablePageLink<Void>("ringView",RingView.class));
		nodeInfoPanel = new NodeInfoPanel("nodeInfoPanel", nodeInfo);
		nodeInfoPanel.setOutputMarkupPlaceholderTag(true);
		add(nodeInfoPanel);
		initBehavior();
		initDialog();    //add pop up dialogs
		initButton();   //add Buttons

	}
	/**
	 * add behaviors, which do real action
	 * */
	private void initBehavior()
	{	
		executeMoveTokenBehaviour = new AjaxJsonBehavior("Finish moving token") {

			@Override
			public void action(WebRequest wr) {
				String selection = wr.getPostParameters()
						.getParameterValue("selection").toString();
				try {
					// do real actions
					clusterManager.move(selection);
				} catch (Exception e) {
					this.setMessage("Move Token Error: "+e.toString());
				}
			}
		};
		add(executeMoveTokenBehaviour);
	
	    behavious = new ArrayList<AjaxJsonBehavior>();
	    for( int i=0;i<confirmButtons.length;i++)
	    {
	    	final String buttonName =confirmButtons[i];
	    	AjaxJsonBehavior executeBehaviour = new AjaxJsonBehavior("Finish "+confirmButtons[i]) {
	    		@Override
	    		public void action(WebRequest wr) {		
					try {
							if(buttonName.equals("drain"))
				    	    	{clusterManager.drain();
				    	    	}
							if(buttonName.equals("decommission"))
			    	    	{clusterManager.decommission();
			    	    	}
							if(buttonName.equals("gc"))
			    	    	{clusterManager.gc();
			    	    	}
							if(buttonName.equals("cleanUp"))
			    	    	{clusterManager.optionalKSs(keyspaces, ClusterManager.NodeCommand.CLEANUP);
			    	    	}
							if(buttonName.equals("flush"))
			    	    	{clusterManager.optionalKSs(keyspaces, ClusterManager.NodeCommand.FLUSH);
			    	    	}
							if(buttonName.equals("scrub"))
							{clusterManager.optionalKSs(keyspaces, ClusterManager.NodeCommand.SCRUB);
							}
							if(buttonName.equals("repair"))
							{clusterManager.optionalKSs(keyspaces, ClusterManager.NodeCommand.REPAIR);
							}
							if(buttonName.equals("compaction"))
							{clusterManager.optionalKSs(keyspaces, ClusterManager.NodeCommand.COMPACT);
							}
						} catch (Exception e) {
							this.setMessage(buttonName+"Error: "+e.toString());
	    	    	}
				}
			
			};
			add(executeBehaviour);
			behavious.add(executeBehaviour);
	    }
		
	}
	
	private void initDialog()
	{
		moveTokenWindow = new OperationModalWindow("moveDialog","move token", nodeInfo){
			@Override
			public void onCancel(AjaxRequestTarget target) {
				close(target);
			}
			@Override
			public void onSelect(AjaxRequestTarget target, String selection) {
				target.appendJavaScript("executeMoveToken("+selection+")");   //excute move token java script 
				close(target);
			}
		};
		add(moveTokenWindow);
		
	}	

	private void initButton(){
		final Form<Void> form = new Form<Void>("form");
		this.add(form);
		//add move token button
		AjaxLink moveTokenLink = new AjaxLink("move"){

			@Override
			public void onClick(AjaxRequestTarget target) {
				moveTokenWindow.show(target);
			}

		};
		form.add(moveTokenLink);
		
		//add drain,gc, decommission buttons
	   
	    for(int i=0;i<confirmButtons.length;i++)
	    {
	    	AjaxLink confirmLink = null;
	    	String buttonName = confirmButtons[i];
	    	final AjaxJsonBehavior behaviou = behavious.get(i);
	    	if(buttonName.equals("drain")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'drain'," +
								"'"+confirmText+"');");   //excute java script 
						
					}
				};
				

	    	}
	    	if(buttonName.equals("decommission")){
	    		final String confirmText = "Are you sure to do"+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'decommission'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
					
	    	}
	    	if(buttonName.equals("gc")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'gc'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	if(buttonName.equals("cleanUp")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'cleanUp'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	//flush
	    	if(buttonName.equals("flush")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'flush'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	if(buttonName.equals("scrub")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'scrub'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	if(buttonName.equals("repair")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'repair'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	if(buttonName.equals("compaction")){
	    		final String confirmText = "Are you sure to do "+confirmButtons[i];
	    			confirmLink = new AjaxLink(buttonName){
	    			
					@Override
					public void onClick(AjaxRequestTarget target) {
						target.appendJavaScript("executeConfirm(" +
								"'"+behaviou.getCallbackUrl().toString()+"'," +
								"'compaction'," +
								"'"+confirmText+"');");   //excute java script 
						
					}};
	    	}
	    	form.add(confirmLink);
	    }
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
