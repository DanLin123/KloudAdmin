package edu.tsinghua.software.pages.columnFamily;

import java.util.ArrayList;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.cassandra.unit.ColumnFamily;
/**
  * file:CfInfoPanel.java
 * columnFamily information panel, used in columnFamily page
 * created at :11-03-2012
 * @author 林丹
 * */
public class CfInfoPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  public CfInfoPanel(String id,ColumnFamily cf) {
	        super(id);
	        add(new Label("keyspace", cf.getKeyspace()));
	        add(new Label("columnFamilyName", cf.getColumnFamilyName()));
	        add(new Label("ColumnType", cf.getColumnType()));
	        add(new Label("comparators", cf.getComparator()));
	        add(new Label("comment", cf.getComment()));
	        add(new Label("rowsCached", cf.getRowsCached()));
	        add(new Label("keysCached", cf.getKeysCached()));
	        add(new Label("rowCacheSavePeriod", cf.getRowCacheSavePeriod()));
	        add(new Label("keyCacheSavePeriod", cf.getKeyCacheSavePeriod()));
	        add(new Label("readRepair", cf.getReadRepairChance()));
	        add(new Label("gc", cf.getGcGrace()));
	      /*  add(new Label("memtableFlushAfter", cf.getMemtableFlushAfter()));
	        add(new Label("memtableOperations", cf.getMemtableOperations()));
	        add(new Label("memtableThroughput", cf.getMemtableThroughput()));*/
	        add(new Label("defaultValidationClass", cf.getDefaultValidationClass()));
	        add(new Label("minCompactionThreshold", cf.getMinCompactionThreshold()));
	        add(new Label("maxCompactionThreshold", cf.getMaxCompactionThreshold()));
	        
	      }
}
