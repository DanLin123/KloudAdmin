package edu.tsinghua.software.pages.keyspace;

import java.util.ArrayList;
import java.util.Set;
import org.apache.wicket.model.LoadableDetachableModel;
import edu.tsinghua.software.cassandra.tools.DataManager;
/**
 * columnFamilyName list model, used in KeyspacePage
 * */

class CfListModel extends LoadableDetachableModel<java.util.List> {

	String keyspace;
	transient DataManager dataManager;

	CfListModel(String keyspace,DataManager dataManager)	 {
		this.keyspace = keyspace;
		this.dataManager = dataManager;
	}

	@Override
	protected ArrayList<String> load() {
		ArrayList<String> cfList = new ArrayList<String>();
		Set<String> columnFamilyNames = null;
		columnFamilyNames = dataManager.getColumnFamilys(keyspace);
		cfList = new ArrayList(columnFamilyNames);
		return cfList;

	}
}