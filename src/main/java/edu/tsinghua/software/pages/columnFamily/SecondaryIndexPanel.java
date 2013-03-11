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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.thrift.IndexType;
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
import edu.tsinghua.software.cassandra.unit.ColumnFamilyMetaData;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

/**
 * file:SecondaryIndexPanel.java
 * Secondary IndexPanel, show index for columnFamily
 * created at :11-03-2012
 * @author 林丹
 * */
public class SecondaryIndexPanel extends BasePage {
	private String columnFamilyName;
	private String keyspaceName;
	private String clusterName;
	private List<ColumnFamilyMetaData> metaDatas = new ArrayList<ColumnFamilyMetaData>();
	transient DataManager dataManager =  new DataManager(getConnection());

	private static List<String> VALIDATIONCLASS = new ArrayList<String>();
	static {
		VALIDATIONCLASS.add("AsciiType");
		VALIDATIONCLASS.add("BytesType");
		VALIDATIONCLASS.add("LexicalUUIDType");
		VALIDATIONCLASS.add("LongType");
		VALIDATIONCLASS.add("TimeUUIDType");
		VALIDATIONCLASS.add("UTF8Type");
	}
	private static List<IndexType> INDEXTYPE = new ArrayList<IndexType>();
	static {
		INDEXTYPE.add( IndexType.KEYS);
	}

	private class InputForm extends Form<ColumnFamilyMetaData> {
	
		ColumnFamily columnFamily = null;   //make index on this column family
		PageParameters pageParameters;      	
		/**
		 * InputForm Construct
		 * @param name InputForm id
		 * @param pageParameters cluster name, columnFamily name 
		 * @param columnFamily Index for this columnFamily
		 * */
		public InputForm(String name,PageParameters pageParameters, final ColumnFamily columnFamily) {
		
			super(name, new CompoundPropertyModel<ColumnFamilyMetaData>(
					new ColumnFamilyMetaData()));          //this form represent ColumnFamilyMetaData
			
			this.columnFamily = columnFamily;
			this.pageParameters = pageParameters;
			
			add(new TextField<String>("columnName").setRequired(true));    //add column name textfield
			//add valiDationClass choice and indexType choice
			DropDownChoice validationClassChoice = new DropDownChoice("valiDationClass", VALIDATIONCLASS);
			DropDownChoice indexTypeChoice = new DropDownChoice("indexType",INDEXTYPE);
			validationClassChoice.setRequired(true);
	
			add(validationClassChoice);
			add(indexTypeChoice);

			add(new TextField<String>("indexName"));
			
			// add createIndexLink
			add(new Button("createIndexLink") {
				@Override
				public void onSubmit() {
				}
			});
		}
		/**
		 * this function is excuted when form is delivered
		 * in this function , get the columnFamilyMeta, add it to the metaDatas,then update the columnfamily with new meta
		 * */		
	
		public void onSubmit() {
			ColumnFamilyMetaData columnFamilyMeta = (ColumnFamilyMetaData) getDefaultModelObject();
			if(columnFamilyMeta.getIndexName() == null)
			{
				
			}
			metaDatas = columnFamily.getMetaDatas();   //get the old metaDatas
			metaDatas.add(columnFamilyMeta);
			columnFamily.setMetaDatas(metaDatas);
			
			try {
				dataManager.updateColumnFamily(keyspaceName, columnFamily);
			} catch (InvalidRequestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SchemaDisagreementException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		/*	setResponsePage(SecondaryIndexPanel.class,pageParameters);*/
			try {
				dataManager.updateColumnFamily(keyspaceName, columnFamily);
			} catch (InvalidRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SchemaDisagreementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			info("成功创建列\\索引");
			}
		}

	/**
	 * construction method
	 * @param pageParameters columnFamilyParam keyspaceParam clusterParam
	 * */
	public SecondaryIndexPanel(PageParameters pageParameters) {

		super();
		columnFamilyName = pageParameters.get("columnFamilyParam").toString();
		keyspaceName =  pageParameters.get("keyspaceParam").toString();
		clusterName = pageParameters.get("clusterParam").toString();
		
		ColumnFamily columnFamily = null;
		try {
			columnFamily = dataManager.getColumnFamilyBean(keyspaceName, columnFamilyName);
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

		//send columnfamily ad param to inputForm
		add(new InputForm("metaDatas", pageParameters,columnFamily));
		add(new FeedbackPanel("feedback"));

		// add navigation
		add(new BookmarkablePageLink<Void>("clusterNavigation",
				ClusterView.class).add(new Label("clusterName",clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNavigation",
				KeyspacePage.class, pageParameters).add(new Label(
				"keyspaceName", keyspaceName)));
		add(new BookmarkablePageLink<Void>("columnFamilyNavigation",
				ColumnFamilyPage.class, pageParameters).add(new Label(
				"columnFamilyName", columnFamilyName)));
	}

@Override
public MenuItemEnum getActiveMenu() {
	// TODO Auto-generated method stub
	return null;
}

}
