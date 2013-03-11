package edu.tsinghua.software.pages.cql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.cassandra.cql.jdbc.CassandraResultSet;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.tabs.Tabs;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;

import edu.tsinghua.software.cassandra.tools.ReadFile;
import edu.tsinghua.software.cassandra.unit.Cell;
import edu.tsinghua.software.cassandra.unit.Key;
import edu.tsinghua.software.pages.row.BrowsePanel;
import edu.tsinghua.software.pages.template.BasePage;
import edu.tsinghua.software.pages.template.MenuItemEnum;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ContextRelativeResource;

public class CqlPage extends BasePage {

	private static final long serialVersionUID = 1L;
	String host = getConnection().getHost();
	int port = getConnection().getThriftPort();
	transient private static java.sql.Connection con = null;
	transient private final CqlSchema schema = new CqlSchema();
	transient private String resultInfo="";
	transient private Map<String,Key> selectResult= new LinkedHashMap<String,Key>();
	transient Panel browsePanel;
	WebMarkupContainer resultDiv;
	
	public CqlPage()
	{
		initJdbc();    //init connection
		String content ="";
		
		//text area for cql input
		final TextArea<String> lasqlCommandArea = new TextArea<String>("lasqlCommandArea",Model.of(content));
		lasqlCommandArea.setRequired(true);
		lasqlCommandArea.setOutputMarkupPlaceholderTag(true);
		Form form = new Form("form");
		//reset button
		AjaxButton resetButton = new AjaxButton("clear"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				lasqlCommandArea.setModelObject("");
				target.add(lasqlCommandArea);
			}
		};
		resetButton.setDefaultFormProcessing(false);
		AjaxButton applyButton = new AjaxButton("excute"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				String lasql = lasqlCommandArea.getModelObject();
				try {
					if(lasql.length()>6 && lasql.substring(0, 6).equals("select"))   //if sql is select , show the result
					{
						selectResult.clear();
						ResultSet resultSet = schema.executeResults(con, lasql);
						target.addComponent(form.get("result"));
						CassandraResultSet crs = resultSet.unwrap(CassandraResultSet.class);
						int item=1;
						while(!crs.isLast())
						{
							Map<String, Cell> cells = new HashMap<String,Cell>();
							String selectResultString  = "";
							crs.next();
							ResultSetMetaData rsmd = crs.getMetaData();
							int cols = rsmd.getColumnCount();
							for (int i = 1; i <= cols; i++) {
								String colNm = rsmd.getColumnName(i);
								String colVal = "";
								String colType = rsmd.getColumnTypeName(i);
								if (colType.equals("JdbcLong")) {
									colVal = "" + crs.getLong(i);
								} else if (colType.equals("JdbcInt32")) {
									colVal = "" + crs.getInt(i);
								} else {
									colVal = crs.getString(i);
								}
								Cell cell = new Cell(colNm,colVal);
								cells.put(colNm, cell);
								selectResultString+= (colNm + ": " + colVal+"  ");
							}
							Key key = new Key(item+"",cells);	
							selectResult.put(item+"", key);
							item++;
						}	
			
						BrowsePanel p  = new BrowsePanel("resultPanel",selectResult,"ItemID","results"){};
						browsePanel.replaceWith(p);
						browsePanel = p;
						target.add(resultDiv);
						
					}
					else    //else show sucess message
					{
						schema.executeNoResults(con, lasql);
						target.addComponent(form.get("result"));
						info("Succees");
						info(lasql);
						
						//set select result invisible
						BrowsePanel p  = new BrowsePanel("resultPanel",selectResult,"ItemID","results"){};
						p.setVisible(false);
						browsePanel.replaceWith(p);
						browsePanel = p;
						target.add(resultDiv);
						
					}

				} catch (SQLException e) {
					target.addComponent(form.get("result"));
					info("Fail");
					info("why? "+e.toString());
					
					//set select result invisible
					BrowsePanel p  = new BrowsePanel("resultPanel",selectResult,"ItemID","results"){};
					p.setVisible(false);
					browsePanel.replaceWith(p);
					browsePanel = p;
					target.add(resultDiv);
				}
				catch(NullPointerException e)
				{
					target.addComponent(form.get("result"));
					info("Fail");
					info("why? Some column value is null");
					
					//set select result invisible
					BrowsePanel p  = new BrowsePanel("resultPanel",selectResult,"ItemID","results"){};
					p.setVisible(false);
					browsePanel.replaceWith(p);
					browsePanel = p;
					target.add(resultDiv);
				}
				
			}
		};
		resetButton.setDefaultFormProcessing(true);
		
		form.add(lasqlCommandArea);
		form.add(resetButton);
		form.add(applyButton);
		add(form);
		form.add(new FeedbackPanel("result").setOutputMarkupId(true));
		

		// help window
	//	String path = this.getClassRelativePath();
		
		ServletContext context = WebApplication.get().getServletContext();
		ReadFile helpDocument = new ReadFile(context.getRealPath("/sqlHelp.properties")); // read from  help file
		
		String helpContent = helpDocument.getContent();
		final TextArea<String> helpTextArea = new TextArea<String>(
				"helpDocument", Model.of(helpContent));
		helpTextArea.setOutputMarkupId(true);
		helpTextArea.setVisible(false);
		final WebMarkupContainer div = new WebMarkupContainer("helpCont"); // a container for textArea
		div.add(helpTextArea);
		div.setOutputMarkupId(true);
		
		add(new AjaxLink("sqlHelp") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				helpTextArea.setVisible(!helpTextArea.isVisible());
				target.addComponent(div);
			}
		});
		add(div);
		
		//result panel fpr select
		browsePanel = new BrowsePanel("resultPanel",selectResult,"",""){};
		browsePanel.setOutputMarkupId(true);
		browsePanel.setVisible(false);
		resultDiv = new WebMarkupContainer("result"); // a container for select result panel
		resultDiv.add(browsePanel);
		resultDiv.setOutputMarkupId(true);
		add(resultDiv);
	}
	
	private void initJdbc() {
		  try {
			Class.forName("org.apache.cassandra.cql.jdbc.CassandraDriver");
			
			con = DriverManager.getConnection(String.format("jdbc:cassandra://%s:%d/system", host, port));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		// TODO Auto-generated method stub
		return null;
	}

}
