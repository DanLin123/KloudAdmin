package edu.tsinghua.software.deploy;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.tabs.Tabs;
import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;

import edu.tsinghua.software.cassandra.tools.ReadFile;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;

public class DeployView extends BasePage {

	public DeployView() {

		Tabs tabs = new Tabs("tabs");
		add(tabs);
		
		//tab1 content
		ReadFile readYaml = new ReadFile("/home/ld/Downloads/apache-cassandra-1.0.8/conf/cassandra.yaml");
		final String content = readYaml.getContent();
		final TextArea<String> yamlFileTxt = new TextArea<String>("yamlTxt",Model.of(content));
		yamlFileTxt.setRequired(true);
		yamlFileTxt.setOutputMarkupPlaceholderTag(true);
		//reset button
		AjaxButton resetButton = new AjaxButton("reset"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				yamlFileTxt.setModelObject(content);
				target.add(yamlFileTxt);
				
			}
			
		};
		AjaxButton applyButton = new AjaxButton("apply"){

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				
			}
			
		};
		Form form = new Form("form");
		tabs.add(form);
		form.add(yamlFileTxt);
		form.add(resetButton);
		form.add(applyButton);
	}

	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
