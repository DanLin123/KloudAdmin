package edu.tsinghua.software.reuseComponent;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class YesNoPanel extends Panel {
	 public YesNoPanel(String id, String message, final ModalWindow modalWindow) {
	        super(id);
	 
	        Form yesNoForm = new Form("yesNoForm");
	 
	        MultiLineLabel messageLabel = new MultiLineLabel("message", message);
	        yesNoForm.add(messageLabel);
	        modalWindow.setTitle("Please confirm");
	        modalWindow.setInitialHeight(200);
	        modalWindow.setInitialWidth(350);
	 
	        AjaxButton yesButton = new AjaxButton("yesButton", yesNoForm) {
	 
	            @Override
	            protected void onSubmit(AjaxRequestTarget target, Form form) {
	                if (target != null) {
	                	modalWindow.close(target);
	                }
	            }
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// TODO Auto-generated method stub
					
				}
	        };
	 
	        AjaxButton noButton = new AjaxButton("noButton", yesNoForm) {
	 
	            @Override
	            protected void onSubmit(AjaxRequestTarget target, Form form) {
	                if (target != null) {
	                    modalWindow.close(target);
	                }
	            }

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					// TODO Auto-generated method stub
					
				}
	        };
	 
	        yesNoForm.add(yesButton);
	        yesNoForm.add(noButton);
	 
	        add(yesNoForm);
	    }
}
