package edu.tsinghua.software.pages.login;

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

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import edu.tsinghua.software.ClientSession;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.template.KloudAdminPage;
import edu.tsinghua.software.staticClass.Log;

/* * Login Page
 * 
 * @author 林丹
 */
public class LoginPage extends KloudAdminPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class InputForm extends Form<ConnectionModel> {
		private final List<String> LANGUGE = Arrays.asList(new String[] {
				"Chinese", "English"});
		private String selected = "Chinese";
		/**
		 *login form 
		 *@param name InputForm id
		 * */
		@SuppressWarnings("serial")
		public InputForm(String name) {
			super(name, new CompoundPropertyModel<ConnectionModel>(
					new ConnectionModel()));
			DropDownChoice<String> listLanguge = new DropDownChoice<String>(
					"languge", new PropertyModel<String>(this, "selected"), LANGUGE);
			add(listLanguge);
			add(new TextField<String>("host").setRequired(true));
			add(new TextField<Integer>("thrift", Integer.class)
					.setRequired(true));
			add(new TextField<Integer>("JMX", Integer.class).setRequired(true));
			add(new Button("connect") {
				@Override
				public void onSubmit() {
				}
			});
		}

		@Override
		public void onSubmit() {
			// Form validation successful. Display message showing edited model.
			ConnectionModel connection = (ConnectionModel) getDefaultModelObject();
			info("Saved model " + connection);
			ClusterConnection c = new ClusterConnection();
			
			c.setHost(connection.getHost());
			c.setJmxPort(connection.getJMX());
			c.setThriftPort(connection.getThrift());
			
			try {
				c.connect();
			} catch (Exception e1) {
				Log.logger.error("connect exception", e1);
			}
		
			getClinetSession().setConnection(c);
			
			// if connect to database successfully
			if (getConnection().isConnected()) {
				setResponsePage(ClusterView.class);

			} else {
				info("Connect failed");
				Log.logger.info("Connect failed");
			}
		
			if(selected.equals("English"))
			{
				getSession().setLocale(Locale.US);
			}
			else if(selected.equals("Chinese"))
			{
				getSession().setLocale(Locale.CHINA);
			}
			
		}
	}

	public LoginPage() {
		super();
		add(new InputForm("inputForm"));
		add(new FeedbackPanel("feedback"));
	
	}

}