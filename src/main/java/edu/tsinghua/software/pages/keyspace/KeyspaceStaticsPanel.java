package edu.tsinghua.software.pages.keyspace;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class KeyspaceStaticsPanel extends Panel {

	public KeyspaceStaticsPanel(String id, Map<String,String> keyspaceStatics) {
		super(id);
		
		add(new Label("readCount",keyspaceStatics.get("Read Count")  ));
		add(new Label("readLatency",keyspaceStatics.get("Read Latency") ));
		add(new Label("writeCount", keyspaceStatics.get("Write Count") ));
		add(new Label("writeLatency",keyspaceStatics.get("Write Latency") ));
		add(new Label("pendingTask",keyspaceStatics.get("Pending Tasks")  ));
	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
