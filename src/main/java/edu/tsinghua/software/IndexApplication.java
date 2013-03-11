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
package edu.tsinghua.software;

import javax.servlet.ServletContext;

import org.apache.log4j.PropertyConfigurator;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.time.Duration;
import org.odlabs.wiquery.ui.themes.IThemableApplication;
import org.odlabs.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import edu.tsinghua.software.pages.login.LoginPage;


/**
 * file:IndexApplication.java
 * KloudAdmin application class
 * created at:11-03-2012
 * @author 林丹
 * */
public class IndexApplication extends WebApplication implements  IThemableApplication{
	private ResourceReference theme;
	/**
	 * Constructor.
	 */
	public IndexApplication() {
		setTheme(new WiQueryCoreThemeResourceReference("Flick"));
	}
	
	@Override
	protected void init() {

	    getRequestCycleSettings().setTimeout(Duration.ONE_HOUR); 
	    //log
	    ServletContext context = WebApplication.get().getServletContext();
		PropertyConfigurator.configure (context.getRealPath("/log4j.properties")) ;
			
	}

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return LoginPage.class;
	
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new ClientSession(request);
	}

	public ResourceReference getTheme(Session session) {
		
		return theme;
	}

	public ResourceReference getTheme() {
		return theme;
	}

	public void setTheme(ResourceReference theme) {
		this.theme = theme;
	}

}
