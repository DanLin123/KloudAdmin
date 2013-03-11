package edu.tsinghua.software;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import edu.tsinghua.software.cassandra.tools.ClusterConnection;

public class ClientSession extends WebSession{
	/*private ClusterConnection connection = new ClusterConnection();
*/
	private ClusterConnection connection;
	
	protected ClientSession(Request request) {
		super(request);

	}

	public ClusterConnection getConnection() {
		return connection;
	}

	public void setConnection(ClusterConnection connection) {
		this.connection = connection;
	}

}
