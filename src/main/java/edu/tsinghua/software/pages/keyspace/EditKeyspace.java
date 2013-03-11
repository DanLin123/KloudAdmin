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

package edu.tsinghua.software.pages.keyspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import edu.tsinghua.software.cassandra.*;
import edu.tsinghua.software.cassandra.tools.DataManager;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;


/**
 * EditKeyspace page
 * @author 林丹
 * */
public class EditKeyspace extends BasePage{
	private static final long serialVersionUID = 1L;
	transient private static List<String> STRATEGY = new ArrayList<String>();
	transient DataManager client =  new DataManager(getConnection());
	
	static {
	//STRATEGY.add("LocalStrategy");
	STRATEGY.add("NetworkTopologyStrategy");
	//STRATEGY.add("OldNetworkTopologyStrategy");
	STRATEGY.add("SimpleStrategy" );
	}
	FormComponent<Integer> re;
	transient KsDef ksDef;
	String clusterName;  //cluster name
	String keyspaceName; //keysapce's name
	KeyspaceModel keyspceModel;   //keyspace model
	PageParameters pageParameters;


	private class InputForm extends Form<KeyspaceModel>{
		private static final long serialVersionUID = 1L;
		/**
		 * InputForm Construct
		 * @param name InputForm id
		 * @param keyspaceModel model for InputForm
		 * */
		@SuppressWarnings("serial")
		public InputForm(String name,KeyspaceModel keyspaceModel)
		{
			super(name, new CompoundPropertyModel<KeyspaceModel>(keyspaceModel));
			
		    add(new TextField<Integer>("relicationFactor", Integer.class).setRequired(true));
		    final TextField strategyOptions = new TextField<String>("strategyOptions");
		    add(strategyOptions);
		    strategyOptions.setOutputMarkupId(true);
		    //add strateyChoice
		    final DropDownChoice strategyChoice = new DropDownChoice( "strategy", STRATEGY);
		    add(strategyChoice);
		    strategyChoice.setRequired(true);
		    strategyChoice.setNullValid(true);
		    
		    strategyChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if(strategyChoice.getModelObject().toString().equals("SimpleStrategy"))
					{
						strategyOptions.setEnabled(false);
						target.add(strategyOptions);		
					}
					else
					{
						strategyOptions.setRequired(true);
						target.add(strategyOptions);
					}
				}
		    });
		
	    	add(new Button("cancel"){

				@Override
	    		public void onSubmit()
	    		{
                    try {
						setResponsePage(new EditKeyspace(pageParameters));
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidRequestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}.setDefaultFormProcessing(false));
	    	
		}
		  /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        public void onSubmit()
        {
        	if(getConnection().isConnected())
        	{
				KeyspaceModel keyspaceModel = (KeyspaceModel) getDefaultModelObject();
				String keyspaceName = keyspaceModel.getKeyspaceName();
				String strategy = keyspaceModel.getStrategy();
				Map<String, String> strategyOptions = String2Map(keyspaceModel.getStrategyOptions());
				int factor = keyspaceModel.getRelicationFactor();
				//make sure the strategy opstions famet is right
				if(!strategyOptions.isEmpty())
				{
					try {
						if(strategy.equals("SimpleStrategy"))
						{
							strategyOptions.clear();
						}
						client.updateKeyspace(keyspaceName,strategy,strategyOptions,factor);
						info("表空间更新完成");
					} catch (Exception e) {
						info("表空间更新失败");
						e.printStackTrace();
					}
				}
			
			}
		}
	}
	
	public String Map2String(Map<String,String> map)
	{
		String str = map.toString();
		return str.substring(1,str.length()-1);
		
	}
	
	public Map<String,String> String2Map(String str)
	{
		// transfer str String to Map
		Map<String,String> map = new HashMap<String,String>();
		if (str != null && !str.isEmpty()) {
			String[] split1 = str.split(",");
			for (String s : split1) {
				String[] split2 = s.split("=");
				if (split2.length != 2) {
					info("Strategy Options format error.");
				} else {
					 map.put(split2[0], split2[1]);
				}
			}
		}
		return map;
	}

	/**
	 * EditKeyspace Construct
	 * @param pageParameters
	 * */
	public EditKeyspace(PageParameters pageParameters) throws NotFoundException, InvalidRequestException, TException
	{
		super();
		this.pageParameters = pageParameters;
		clusterName =  pageParameters.get("clusterParam").toString();
		keyspaceName = pageParameters.get("keyspaceParam").toString();
		ksDef = client.describeKeyspace(keyspaceName);
		keyspceModel = new KeyspaceModel(ksDef.getName(),ksDef.getReplication_factor(),ksDef.strategy_class,Map2String(ksDef.getStrategy_options()));
		
		// navigation cluster>>keysapce>>edit keyspace
		add(new BookmarkablePageLink<Void>("clusterNevigation",ClusterView.class,pageParameters).add(new Label("clusterName", clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNevigation",KeyspacePage.class,pageParameters).add(new Label("keyspaceName", keyspaceName)));

		add(new InputForm("inputForm", keyspceModel));
		add(new FeedbackPanel("feedback"));
		
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
