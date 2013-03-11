package edu.tsinghua.software.pages.monitor;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;



public class Monitor extends BasePage {
    transient DataManager dataManager = new DataManager(getConnection());
     //log
  	 static Logger logger = Logger.getLogger 
  				( Monitor.class.getName () ) ;
    
	public Monitor()
	{
		
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
