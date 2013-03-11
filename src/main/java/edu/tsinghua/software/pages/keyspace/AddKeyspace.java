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
import java.util.Locale;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.tools.NameValidator;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

/**
 * Add keyspace page
 * @author 林丹
 * */
public class AddKeyspace  extends BasePage{
	private static final long serialVersionUID = 1L;
	transient DataManager client =  new DataManager(getConnection());
	String clusterName;
	
	private static List<String> STRATEGY = new ArrayList<String>();
	static {
	//	STRATEGY.add("LocalStrategy");
		STRATEGY.add("NetworkTopologyStrategy");
	//	STRATEGY.add("OldNetworkTopologyStrategy");
		STRATEGY.add("SimpleStrategy");
	}
	private PageParameters pageParameters;


	private class InputForm extends Form<KeyspaceModel>{

		private static final long serialVersionUID = 1L;
		/**
		 * InputForm Construct 
		 * @param InputForm id
		 */
		@SuppressWarnings("serial")
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel<KeyspaceModel>(new KeyspaceModel()));
			
			
			add(new TextField<String>("keyspaceName").setRequired(true).add(new NameValidator()));
		    add(new TextField<Integer>("relicationFactor", Integer.class).setRequired(true));
		    final TextField strategyOptions = new TextField<String>("strategyOptions");
		    strategyOptions.setOutputMarkupId(true);
		    add(strategyOptions);
		    
		    
		    //add strateyChoice,  choose simple strategy , disable strategyOptions
		    final DropDownChoice strategyChoice = new DropDownChoice( "strategy", STRATEGY);
		    add(strategyChoice);
		    strategyChoice.setRequired(true);
		    strategyChoice.setNullValid(true);
		    
		    strategyChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					if(strategyChoice.getModelObject().toString().equals("SimpleStrategy"))
					{
						strategyOptions.setModelObject("");
						strategyOptions.setEnabled(false);
						target.add(strategyOptions);		
					}
					else
					{
						strategyOptions.setEnabled(true);
						strategyOptions.setRequired(true);
						target.add(strategyOptions);
					}
				}
		    });
		
	    	add(new Button("cancel"){

				@Override
	    		public void onSubmit()
	    		{
                    setResponsePage(new AddKeyspace(pageParameters));
	    		}
	    	}.setDefaultFormProcessing(false));
	    	
		}
		  
		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit() {
			
			if (getConnection().isConnected()) {
				KeyspaceModel keyspaceModel = (KeyspaceModel) getDefaultModelObject();
				String keyspaceName = keyspaceModel.getKeyspaceName();
				String strategy = keyspaceModel.getStrategy();
				Map<String, String> strategyOptions = String2Map(keyspaceModel
						.getStrategyOptions());
				int factor = keyspaceModel.getRelicationFactor();

				try {
					client.addKeyspace(keyspaceName, strategy, strategyOptions,
							factor);
					if(getSession().getLocale() == Locale.CHINA)
					{
						info("创建表空间成功");
						setResponsePage(new AddKeyspace(pageParameters));
						
					}else if(getSession().getLocale() == Locale.US)
					{
						info("keyspace is created");
						setResponsePage(new AddKeyspace(pageParameters));
					}
				}catch (InvalidRequestException e) {
					if(getSession().getLocale() == Locale.CHINA)
					{
					//	System.out.println(e.why.substring(0,47));
						if(e.why.substring(0,48).endsWith("Keyspace names must be case-insensitively unique"))
						{
							info("创建表空间失败,已经存在该表空间.");
						}else
						{
							info("创建表空间失败,不支持所选备份策略.");
						}
						
					}else if(getSession().getLocale() == Locale.US)
					{
						info(e.why);
					}
					System.out.println(e.why);
				}catch (SchemaDisagreementException e) {
					if(getSession().getLocale() == Locale.CHINA)
					{
						info("创建表空间失败.");
					}else if(getSession().getLocale() == Locale.US)
					{
						info("keyspace can not be created.");
					}
					System.out.println(e.toString());
				}
				catch (TException e) {
					if(getSession().getLocale() == Locale.CHINA)
					{
						info("创建表空间失败.");
					}else if(getSession().getLocale() == Locale.US)
					{
						info("keyspace can not be created.");
					}
					System.out.println(e.toString());
				}
			}
		}
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
					if(getSession().getLocale() == Locale.CHINA)
					{
						info("策略选项格式错误.");
			
					}else if(getSession().getLocale() == Locale.US)
					{
						info("Strategy Options format error.");
						
					}
					
				} else {
					 map.put(split2[0], split2[1]);
				}
			}
		}
		return map;
	}
	/**
	 * AddKepsace Constuct 
	 * @param pageParameters clusterParam
	 * */
	public AddKeyspace(PageParameters pageParameters)
	{
		super();  
		this.pageParameters = pageParameters;
		//get the cluster name from pageParameters
        if (pageParameters == null || pageParameters.get("clusterParam") == null)
        {
        	clusterName = "there is no cluster";
        }
        else
        {
        	clusterName = pageParameters.get("clusterParam").toString();
        }
        //add navigation
		add (new BookmarkablePageLink<Void>("clusterNavigation", ClusterView.class).add(new Label("clusterName",clusterName)));
		//add keyspace input form
		add(new InputForm("inputForm"));
		add(new FeedbackPanel("feedback"));
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
