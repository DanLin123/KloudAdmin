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
import java.util.List;
import java.util.Locale;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
/**
 * DeleteKeyspace page
 * @author 林丹
 * */
public class DeleteKeyspacePage extends BasePage {
	private static final long serialVersionUID = 1L;
	private PageParameters pageParameters = new PageParameters();
	transient DataManager client =  new DataManager(getConnection()); // client which is responsible to comunicate with cassandra
	String clusterName = new String();
	ArrayList<String> ksList = new ArrayList<String>(); // list of keyspace name 

	private ArrayList<String> keyspaceNameList = new ArrayList<String>();
	// hold the checkbox values
	private ArrayList<String> keyspacesSelect = new ArrayList<String>();

	/**
	 * DeleteKeyspacePage Construct
	 * @param parameters 
	 * */
	public DeleteKeyspacePage(final PageParameters parameters) throws TException {
		init(parameters);
		//navigation
		add (new BookmarkablePageLink<Void>("clusterNavigation", ClusterView.class).add(new Label("clusterName",clusterName)));
		final FeedbackPanel feedBack = new FeedbackPanel("feedback");
		feedBack.setOutputMarkupId(true);
		
 
		final CheckBoxMultipleChoice<String> listKeyspaces = 
                           new CheckBoxMultipleChoice<String>(
				"keyspaces", new Model(keyspacesSelect), keyspaceNameList);
		listKeyspaces.setOutputMarkupId(true);
		
		AjaxLink checkAll = new AjaxLink<Void>("checkAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				keyspacesSelect.addAll(listKeyspaces.getChoices());
				target.add(listKeyspaces);
			}
		};
		AjaxLink uncheckAll = new AjaxLink<Void>("uncheckAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				keyspacesSelect.clear();
				target.add(listKeyspaces);
			}
		};
 
		Form<?> form = new Form<Void>("userForm"){
			@Override
			public void onSubmit() {
				for (String keyspaceName : keyspacesSelect) {
					try {
						client.dropKeyspace(keyspaceName);
						keyspaceNameList.remove(keyspaceName);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				if(getSession().getLocale() == Locale.CHINA)
				{
					info("删除表空间："+ keyspacesSelect);
					try {
						setResponsePage(new DeleteKeyspacePage(pageParameters));
					} catch (TException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(getSession().getLocale() == Locale.US)
				{
					info("drop keyspace："+ keyspacesSelect);
					try {
						setResponsePage(new DeleteKeyspacePage(pageParameters));
					} catch (TException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
        add(feedBack);
		add(form);
		form.add(listKeyspaces);
		form.add(checkAll);
		form.add(uncheckAll);
	}

	/**
	 * init for DeleteKeyspacePage class
	 * @param pageParameters
	 * */
	public void init(PageParameters pageParameters) throws TException {
		this.pageParameters = pageParameters;
		clusterName = pageParameters.get("clusterParam").toString();
		List<KsDef> keyspaceList = new ArrayList<KsDef>();
		try {
			keyspaceList = client.getKeyspaces();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (KsDef keyspace : keyspaceList) {
			keyspaceNameList.add(keyspace.getName());
			keyspaceNameList.remove("system");
		}

	}
	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}
}