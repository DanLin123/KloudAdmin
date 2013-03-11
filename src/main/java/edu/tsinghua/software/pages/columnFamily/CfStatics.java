package edu.tsinghua.software.pages.columnFamily;

import org.apache.cassandra.cache.InstrumentingCacheMBean;
import org.apache.cassandra.db.ColumnFamilyStoreMBean;
import org.apache.cassandra.tools.NodeProbe;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import edu.tsinghua.software.cassandra.tools.ClusterManager;

public class CfStatics extends Panel {
	
	public CfStatics(String id,ColumnFamilyStoreMBean cfstore, String keySpace, ClusterManager clusterManager) {
		super(id);
		NodeProbe probe = clusterManager.getProbe();
	    add(new Label("sstableCount",cfstore.getLiveSSTableCount()+"" ));
	    add(new Label("spaceUsedLive", cfstore.getLiveDiskSpaceUsed()+""));
	    add(new Label("spaceUsedTotal",cfstore.getTotalDiskSpaceUsed()+"" ));
	    add(new Label("numberOfkeys", cfstore.estimateKeys()+""));
	    add(new Label("mentableColumnsCount", cfstore.getMemtableColumnsCount()+""));
	    add(new Label("mentableDataSize", cfstore.getMemtableDataSize()+""));
	    add(new Label("mentableSwitchCount", cfstore.getMemtableSwitchCount()+""));
	    add(new Label("readCount",cfstore.getReadCount() +""));
	    add(new Label("readLatency", String.format("%01.3f", cfstore.getRecentReadLatencyMicros() / 1000) + " ms."));
	    add(new Label("writeCount",cfstore.getWriteCount()+"" ));
	    add(new Label("writeLatency", String.format("%01.3f", cfstore.getRecentWriteLatencyMicros() / 1000) + " ms."));
	    add(new Label("pendingTask", cfstore.getPendingTasks()+""));
	    
	    
	    add(new Label("bloomFilterFalsePostives", cfstore.getBloomFilterFalsePositives()+""));
	    add(new Label("bloomFilterFalseRatio",cfstore.getBloomFilterFalseRatio()+"" ));
	   /* add(new Label("bloomFilterSpaceUsed", cfstore.getBloomFilterDiskSpaceUsed()+""));*/
	       
        InstrumentingCacheMBean keyCacheMBean = probe.getKeyCacheMBean(keySpace, cfstore.getColumnFamilyName());
        if (keyCacheMBean.getCapacity() > 0)
        {
        	add(new Label("keyCache", "enable"));
    	    add(new Label("keyCacheCapacity", keyCacheMBean.getCapacity()+""));
    	    add(new Label("keyCacheSize", keyCacheMBean.getSize()+""));
    	    add(new Label("keyCacheHitRate",keyCacheMBean.getRecentHitRate()+""));
        }
        else
        {
        	add(new Label("keyCache", "disable"));
    	    add(new Label("keyCacheCapacity", ""));
    	    add(new Label("keyCacheSize", ""));
    	    add(new Label("keyCacheHitRate",""));
        }
        
        InstrumentingCacheMBean rowCacheMBean = probe.getRowCacheMBean(keySpace, cfstore.getColumnFamilyName());
        if (rowCacheMBean.getCapacity() > 0)
        {
        	add(new Label("rowCache", "enable"));
    	    add(new Label("rowCacheCapacity", rowCacheMBean.getCapacity()+""));
    	    add(new Label("rowCacheSize", rowCacheMBean.getSize()+""));
    	    add(new Label("rowCacheHitRate",rowCacheMBean.getRecentHitRate()+""));
        }
        else
        {
        	add(new Label("rowCache", "disable"));
    	    add(new Label("rowCacheCapacity",""));
    	    add(new Label("rowCacheSize", ""));
    	    add(new Label("rowCacheHitRate",""));
        }
	  
	    add(new Label("compactedRowMinimumSize", cfstore.getMinRowSize()+""));
	    add(new Label("compactedRowMaximumSize", cfstore.getMaxRowSize()+""));
	    add(new Label("compactedRowMeanSize", cfstore.getMeanRowSize()+""));
	    
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
