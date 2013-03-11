package edu.tsinghua.software.reuseComponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.pages.login.LoginPage;

public abstract class MoveTokenPanel extends Panel {
//	private Component parent = new WebMarkupContainer("parent");
	public MoveTokenPanel(String id, NodeInfo nodeInfo) {
		
		super(id);
		
		add(new Label("token", nodeInfo.getToken().toString()));

		// Create the form, to use later for the buttons
		Form form = new Form("form");
		add(form);
		
		final TextField<String> newTokenField = new TextField<String>("newToken",Model.of(""));
		newTokenField.setRequired(true);
		newTokenField.setOutputMarkupId(true);
		newTokenField.setMarkupId("input-new-token");
		form.add(newTokenField);
		
		AjaxButton moveButton = new AjaxButton("move"){
			private static final long serialVersionUID = 1L;

			@Override
			protected AjaxPreprocessingCallDecorator getAjaxCallDecorator() {
				return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {
					private static final long serialVersionUID = 1L;

					/*@Override
					public CharSequence preDecorateScript(CharSequence script) {
						return "if(!confirm('Are you sure you want to move token?')) return false;"
								+ script;
					}*/
				};
			}	
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				  onSelect(target, newTokenField.getModelObject());
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}
		};
		moveButton.setOutputMarkupId(true);
		moveButton.setMarkupId("move-button");
		form.add(moveButton);
		
		// Add a cancel / close button.
		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				onCancel(target);
			}

		});
	
	}

	abstract void onSelect(AjaxRequestTarget target,String selection);

	abstract void onCancel(AjaxRequestTarget target);
}
