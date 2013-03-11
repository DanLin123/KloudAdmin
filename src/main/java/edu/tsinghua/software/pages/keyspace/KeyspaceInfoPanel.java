package edu.tsinghua.software.pages.keyspace;

import org.apache.cassandra.thrift.KsDef;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * file: KeyspaceInfoPanel.java
* keyspace Information panel,used in keyspacePage
* created at :11-03-2012
* @author 林丹
* */
public class KeyspaceInfoPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public KeyspaceInfoPanel(String id, KsDef ksDef)
	{
		super(id);
			
		add(new Label("ksName",ksDef.getName()));
		add(new Label("replication",ksDef.getReplication_factor()+""));
		add(new Label("strategy",ksDef.getStrategy_class()));
		
	}

}
