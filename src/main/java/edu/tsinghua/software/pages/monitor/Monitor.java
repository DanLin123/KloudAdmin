package edu.tsinghua.software.pages.monitor;
import org.apache.log4j.Logger;
import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;


/**
 * read graph.conf file, and send the attribute to javascript
 * a list of : metric + tags
 * JSON.stringify({'host':'ld-VirtualBox','type_instance':'idle','plugin_instance':'0'});
 * host和time是在网页上选择，因此不用传输
 * 需要传输的参数包括：
 * {blockName:{picsName:    metric, a list of type_instance, a list of plugin_instance}}
 * */
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
