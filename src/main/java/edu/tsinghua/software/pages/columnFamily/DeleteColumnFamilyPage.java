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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.keyspace.DeleteKeyspacePage;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

/**
 * DeleteKeyspace page
 * @author 林丹
 * */
public class DeleteColumnFamilyPage extends BasePage {
	private static final long serialVersionUID = 1L;
	static PageParameters pageParameters = new PageParameters();
	transient DataManager client =  new DataManager(getConnection());
	String clusterName = new String();
	String keyspaceName = new String();
	private ArrayList<String> columnFamilyNameList = new ArrayList<String>();
	// hold the checkbox values
	private ArrayList<String> columnFamilySelect = new ArrayList<String>();

	/**
	 * DeleteKeyspacePage Construct
	 * @param parameters 
	 * */
	public DeleteColumnFamilyPage(final PageParameters parameters) throws TException {
		init(parameters);
		//navigation
		add (new BookmarkablePageLink<Void>("clusterNavigation", ClusterView.class).add(new Label("clusterName",clusterName)));
		add(new BookmarkablePageLink<Void>("keyspaceNevigation",KeyspacePage.class,parameters).add(new Label("keyspaceName", keyspaceName)));
		
		final FeedbackPanel feedBack = new FeedbackPanel("feedback");
		feedBack.setOutputMarkupId(true);
		
 
		final CheckBoxMultipleChoice<String> listColumnFamilies = 
                           new CheckBoxMultipleChoice<String>(
				"listColumnFamilies", new Model(columnFamilySelect), columnFamilyNameList);
		listColumnFamilies.setOutputMarkupId(true);
		
		AjaxLink checkAll = new AjaxLink<Void>("checkAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				columnFamilySelect.addAll(listColumnFamilies.getChoices());
				target.add(listColumnFamilies);
			}
		};
		AjaxLink uncheckAll = new AjaxLink<Void>("uncheckAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				columnFamilySelect.clear();
				target.add(listColumnFamilies);
			}
		};
 
		Form<?> form = new Form<Void>("userForm"){
			@Override
			public void onSubmit() {
				for (String columnFamilyName : columnFamilySelect) {
					try {
						client.dropColumnFamily(keyspaceName, columnFamilyName);
						columnFamilyNameList.remove(columnFamilyName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				info("drop"+ columnFamilySelect);
				listColumnFamilies.setChoices(columnFamilyNameList);
				try {
					pageParameters.set("clusterParam", clusterName);
					pageParameters.set("keyspaceParam", keyspaceName);
					setResponsePage(new DeleteColumnFamilyPage(pageParameters));
				} catch (TException e) {
					// TODO Auto-generated catch block
					System.out.println("*************");
					e.printStackTrace();
				}
			}
		};

        add(feedBack);
		add(form);
		form.add(listColumnFamilies);
		form.add(checkAll);
		form.add(uncheckAll);
	}

	/**
	 * init for DeleteKeyspacePage class
	 * @param pageParameters
	 * */
	public void init(PageParameters pageParameters) throws TException {

		clusterName = pageParameters.get("clusterParam").toString();
		keyspaceName = pageParameters.get("keyspaceParam").toString();
		Set<String> set = client.getColumnFamilys(keyspaceName);		
		for(String name:set)
		{
			columnFamilyNameList.add(name); 
		}

	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}