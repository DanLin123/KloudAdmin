package edu.tsinghua.software.pages.clusterManager;

import java.util.HashMap;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.json.simple.JSONObject;

public abstract class AjaxJsonBehavior extends AbstractAjaxBehavior{
	private static final long serialVersionUID = 1L;
	RequestCycle requestCycle;
	JSONObject obj;
	WebRequest wr;
	private String message;   //message showed when finish acions
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Constructor
	 * */
	public AjaxJsonBehavior(String message)
	{
		super();
		this.message =  message;
	
	}
	
	public void onRequest()
    {		
        //get parameters
        requestCycle = RequestCycle.get();
        wr=(WebRequest)requestCycle.getRequest();
        action(wr);
        responseJson();
        //valuewResponseJson(wr);
        requestCycle.scheduleRequestHandlerAfterCurrent(
        		new TextRequestHandler("application/json", 
        		"UTF-8", obj.toJSONString()));
    }
	/**
	 * real action done in background
	 * */
	public abstract void action(WebRequest wr); 
	/**
	 * response after finish
	 * */
	public void responseJson()
	{
		 //response content
        obj=new JSONObject();
        obj.put("success",new Boolean(true));
        obj.put("message", message);
	}
	/*public abstract void valuewResponseJson(WebRequest wr); */
}
