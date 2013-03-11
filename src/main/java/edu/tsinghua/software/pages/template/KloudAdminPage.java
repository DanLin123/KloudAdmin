package edu.tsinghua.software.pages.template;

import org.apache.wicket.markup.html.WebPage;

import edu.tsinghua.software.ClientSession;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;

public abstract class KloudAdminPage extends WebPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClientSession getClinetSession() {
		return (ClientSession) getSession();
	}

	public ClusterConnection getConnection() {
		ClusterConnection connection =getClinetSession().getConnection();
		return connection;
	}

	
}
