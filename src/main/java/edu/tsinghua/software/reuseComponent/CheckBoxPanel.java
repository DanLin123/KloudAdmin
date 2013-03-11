package edu.tsinghua.software.reuseComponent;

import java.util.ArrayList;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public abstract class CheckBoxPanel extends Panel {
	ArrayList<String> columnFamilySelect = new ArrayList<String>();
	ArrayList<String> columnFamilyNameList;
	String operation;
	


	public CheckBoxPanel(String id,ArrayList<String> choice,String operation) {
		super(id);
		columnFamilyNameList = choice;
		this.operation = operation;
		final CheckBoxMultipleChoice<String> listColumnFamilies = new CheckBoxMultipleChoice<String>(
				"listColumnFamilies", new Model(columnFamilySelect),columnFamilyNameList);
		AjaxLink checkAll = new AjaxLink<Void>("checkAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				columnFamilySelect.addAll(listColumnFamilies.getChoices());
				target.add(listColumnFamilies);
			}
		};
		AjaxLink uncheckAll = new AjaxLink<Void>("uncheckAll") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				columnFamilySelect.clear();
				target.add(listColumnFamilies);
			}
		};

		Form<?> form = new Form<Void>("form");
		AjaxButton ok = new AjaxButton("ok", form) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSelect(target,columnFamilySelect);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub

			}
		};
	
		add(form);
		form.add(new AjaxLink("cancel") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				target.add(this.getParent().getParent());
				this.getParent().getParent().setVisible(false);
			}

		});
		form.add(ok);
		form.add(listColumnFamilies);
		form.add(checkAll);
		form.add(uncheckAll);
	}
	
	public ArrayList<String> getColumnFamilyNameList() {
		return columnFamilyNameList;
	}

	public void setColumnFamilyNameList(ArrayList<String> columnFamilyNameList) {
		this.columnFamilyNameList = columnFamilyNameList;
	}
	
	public abstract void onSelect(AjaxRequestTarget target,ArrayList<String> listSelection);
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	/*public abstract void onCancel(AjaxRequestTarget target);*/
	private static final long serialVersionUID = 1L;

}
