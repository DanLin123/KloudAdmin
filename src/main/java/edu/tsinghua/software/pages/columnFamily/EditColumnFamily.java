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

/* * EditColumnFamily page
 * @author 林丹
 */
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
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
import edu.tsinghua.software.cassandra.unit.ColumnFamily;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

public class EditColumnFamily  extends BasePage{
	private static final long serialVersionUID = 1L;
	private String keyspaceName;  //create columnfamily under which keyspace
	private String clusterName;
	private String columnFamilyName;
	private PageParameters pageParameters;
	transient DataManager client =  new DataManager(getConnection());
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
		 * @param cf InputForm object(columnFamily)
		 */
		@SuppressWarnings("serial")
		public InputForm(String name,ColumnFamily cf)
		{
			super(name, new CompoundPropertyModel<ColumnFamily>(cf));
	/*	    DropDownChoice type = new DropDownChoice( "columnType", COLUMNTYPE);
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
		    subcomparatorType.setNullValid(true);*/
		    
			add(new TextField<String>("comment"));
			add(new TextField<String>("rowsCached"));
			add(new TextField<String>("rowCacheSavePeriod"));
			add(new TextField<String>("keysCached"));
			add(new TextField<String>("keyCacheSavePeriod"));
			add(new TextField<String>("readRepairChance"));
			add(new TextField<String>("gcGrace"));
			/*add(new TextField<String>("memtableOperations"));
			add(new TextField<String>("memtableThroughput"));
			add(new TextField<String>("memtableFlushAfter"));*/
			add(new TextField<String>("defaultValidationClass"));
			add(new TextField<String>("minCompactionThreshold"));
			add(new TextField<String>("maxCompactionThreshold"));
			
	    	add(new Button("确定"){
				@Override
	    		public void onSubmit()
	    		{
				/*	info("button is clicked");*/
	    		}
	    	});
	    	add(new Button("取消"){

				@Override
	    		public void onSubmit()
	    		{
			
					// just set a new instance of the page
                    try {
						setResponsePage(new EditColumnFamily(pageParameters));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidRequestException e) {
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
        	    ColumnFamily columnFamily = (ColumnFamily) getDefaultModelObject();
        		try {
					client.updateColumnFamily(keyspaceName, columnFamily);
					info("表更新完成");
				
				} catch (Exception e) {
					info("表更新失败");
					e.printStackTrace();
				} 
        	}
        }
	}
	
	/**
	 * EditColumnFamily Construct
	 * @param pageParameters clusterParam keyspaceParam columnFamilyParam
	 * */
	public EditColumnFamily(PageParameters pageParameters) throws UnsupportedEncodingException, NotFoundException, TException, InvalidRequestException
	{
		super();
		this.pageParameters = pageParameters;
		clusterName =  pageParameters.get("clusterParam").toString();
		keyspaceName = pageParameters.get("keyspaceParam").toString();
		columnFamilyName = pageParameters.get("columnFamilyParam").toString();
		
	
		
		add(new BookmarkablePageLink<Void>("clusterNevigation",ClusterView.class,pageParameters).add(new Label("clusterName", clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNevigation",KeyspacePage.class,pageParameters).add(new Label("keyspaceName", keyspaceName)));
		add(new BookmarkablePageLink<Void>("columnFamilyNevigation",ColumnFamilyPage.class,pageParameters).add(new Label("columnFamilyName", columnFamilyName)));
		

		ColumnFamily cf = client.getColumnFamilyBean(keyspaceName, columnFamilyName);
		add(new InputForm("inputForm",cf));
		add(new FeedbackPanel("feedback"));
	    keyspaceName = pageParameters.get("keyspaceParam").toString();
	}


	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
