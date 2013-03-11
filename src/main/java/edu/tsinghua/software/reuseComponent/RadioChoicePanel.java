package edu.tsinghua.software.reuseComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class RadioChoicePanel extends Panel {

	//variable to hold radio button values
	private String selected = "";
	private String operation;


	public RadioChoicePanel(String id, ArrayList<String> choice,String operation) {
		super(id);
		RadioChoice<String> hostingType = new RadioChoice<String>(
				"hosting", new PropertyModel<String>(this, "selected"), choice);

		AjaxButton yesButton = new AjaxButton("yes"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				  onSelect(target, selected);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				
			}
		};
 
		Form<?> form = new Form<Void>("form");
		add(form);
		form.add(hostingType);
		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				onCancel(target);
			}

		});
		form.add(yesButton);
		Label operationTitle = new Label("operationTitle",operation);
		add(operationTitle);
		
	}
	public abstract void onSelect(AjaxRequestTarget target,String selection);

	public abstract void onCancel(AjaxRequestTarget target);
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}

	private static final long serialVersionUID = 1L;
}
