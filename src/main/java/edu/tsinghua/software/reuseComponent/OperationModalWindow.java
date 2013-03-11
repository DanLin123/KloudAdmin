package edu.tsinghua.software.reuseComponent;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import edu.tsinghua.software.cassandra.node.NodeInfo;

public abstract class OperationModalWindow extends ModalWindow {
	
    public OperationModalWindow(String id,String title, NodeInfo nodeInfo) {
        super(id);
        // Set sizes of this ModalWindow. You can also do this from the HomePage
        // but its not a bad idea to set some good default values.
        setInitialWidth(450);
        setInitialHeight(170);
        setTitle(title);
        
        // Set the content panel, implementing the abstract methods
        setContent(new MoveTokenPanel("content", nodeInfo){

			@Override
			void onCancel(AjaxRequestTarget target) {
				OperationModalWindow.this.onCancel(target);
			}
	    	@Override
			void onSelect(AjaxRequestTarget target,String selection) {
	    		OperationModalWindow.this.onSelect(target, selection);
	    	}
        });
    }
   
    public abstract void onCancel(AjaxRequestTarget target);

    public abstract void onSelect(AjaxRequestTarget target, String selection);
  
}

