package edu.tsinghua.software.reuseComponent;

import java.util.ArrayList;

import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class KsRadioChoicePanel extends Panel {
	private String selected="";
	RadioChoice<String> keyspaceList;
	public KsRadioChoicePanel(String id, ArrayList<String> ksList) {
		super(id);
		
		keyspaceList = new RadioChoice<String>(
				"keyspace", ksList);
		add(keyspaceList);
	}
	public String getSelected() {
		selected = keyspaceList.getModelObject();
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
