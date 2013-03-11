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

package edu.tsinghua.software.pages.columnFamily;



import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
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
import edu.tsinghua.software.cassandra.unit.ColumnFamily;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
import edu.tsinghua.software.reuseComponent.DefaultTextField;

/**
 * file:AddColumnFamily.java
 * Add columnFamily page
 * created at :11-03-2012
 * @author 林丹
 * */
public class AddColumnFamily  extends BasePage{
	private static final long serialVersionUID = 1L;
	private String keyspaceName;  
	private String clusterName;
	transient DataManager client =  new DataManager(getConnection());
	private PageParameters pageParameters;
	

	//default values for column family
	final String ROWS_CACHED = "0.0";
	final String KEYS_CACHED = "200000.0";
	final String ROWS_CACHED_SAVE = "0";
	final String KEYS_CACHED_SAVE	="14400";
	final String READR_REPAIR_CHANCE =	"1.0";
	final String GC_GRACE_SECONDS	="864000";
	final String MEMTABLE_FLUSH_AFTER ="0";
	final String MEMTABLE_OPERATIONS ="0.0";
	final String MEMTABLE_THROUGHPUT ="0";
	final String DEFAULT_VALIDATION_CLASS = "org.apache.cassandra.db.marshal.BytesType";
	final String MIN_COMPACTION ="4";
	final String MAX_COMPACTION ="32";
	
	
	private static List<String> COLUMNTYPE = new ArrayList<String>();
	static {
	COLUMNTYPE.add("Super");
	COLUMNTYPE.add("Standard");
	}
	private static List<String> COMPARATORTYPE = new ArrayList<String>();
	static {
	COMPARATORTYPE.add("AsciiType");
	COMPARATORTYPE.add("BytesType");
	COMPARATORTYPE.add("LexicalUUIDType");
	COMPARATORTYPE.add("LongType");
	COMPARATORTYPE.add("TimeUUIDType");
	COMPARATORTYPE.add("UTF8Type");
	}

	private class InputForm extends Form<ColumnFamily>{
		private static final long serialVersionUID = 1L;
		/**
		 * InputForm Construct 
		 * @param name InputForm id
		 */
		@SuppressWarnings("serial")
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel<ColumnFamily>(new ColumnFamily()));
			add(new TextField<String>("columnFamilyName").setRequired(true).add(new NameValidator()));

		    DropDownChoice type = new DropDownChoice( "columnType", COLUMNTYPE);
		    add(type);
		    type.setRequired(true);
		    type.setNullValid(true);
		    
		    DropDownChoice comparatorType = new DropDownChoice( "comparator", COMPARATORTYPE);
		    add(comparatorType);
		    comparatorType.setRequired(true);
		    comparatorType.setNullValid(true);
		    
		    DropDownChoice subcomparatorType = new DropDownChoice( "subcomparator", COMPARATORTYPE);
		    add(subcomparatorType);
		    subcomparatorType.setRequired(true);
		    subcomparatorType.setNullValid(true);
		    
		    add(new TextField<String>("comment"));
			add(new DefaultTextField("rowsCached",ROWS_CACHED));
			add(new DefaultTextField("rowCacheSavePeriod",ROWS_CACHED_SAVE));
			add(new DefaultTextField("keysCached",KEYS_CACHED));
			add(new DefaultTextField("keyCacheSavePeriod",KEYS_CACHED_SAVE));
			add(new DefaultTextField("readRepairChance",READR_REPAIR_CHANCE));
			add(new DefaultTextField("gcGrace",GC_GRACE_SECONDS));
			add(new DefaultTextField("defaultValidationClass",DEFAULT_VALIDATION_CLASS));
			add(new DefaultTextField("minCompactionThreshold",MIN_COMPACTION));
			add(new DefaultTextField("maxCompactionThreshold",MAX_COMPACTION));
			
	    	add(new Button("cancel"){

				@Override
	    		public void onSubmit()
	    		{
			
					// just set a new instance of the page
                    setResponsePage(new AddColumnFamily(pageParameters));
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
        		try {
        			ColumnFamily columnFamily = (ColumnFamily) getDefaultModelObject();
					client.addColumnFamily(keyspaceName, columnFamily);
					info("表创建成功");
				} catch (InvalidRequestException e) {
				    info("创建表失败，系统中已经存在该表");
					e.printStackTrace();
				} catch (TException e) {
					info("创建表失败");
					e.printStackTrace();
				} catch (SchemaDisagreementException e) {
					info("创建表失败");
					e.printStackTrace();
				}
        	}
        }
	}
	
	/**
	 * AddColumnFamily Construct
	 * @param pageParameters clusterParam, keyspaceParam
	 * */
	public AddColumnFamily(PageParameters pageParameters)
	{
		super();
		this.pageParameters =pageParameters;
		clusterName =  pageParameters.get("clusterParam").toString();
		keyspaceName = pageParameters.get("keyspaceParam").toString();
		
		add(new BookmarkablePageLink<Void>("clusterNevigation",ClusterView.class,pageParameters).add(new Label("clusterName", clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNevigation",KeyspacePage.class,pageParameters).add(new Label("keyspaceName", keyspaceName)));
	
		
		add(new InputForm("inputForm"));
		add(new FeedbackPanel("feedback"));
	    keyspaceName = pageParameters.get("keyspaceParam").toString();
	}


	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}


}
