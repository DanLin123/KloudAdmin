package edu.tsinghua.software.pages.template;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import edu.tsinghua.software.cassandra.tools.DataManager;
import edu.tsinghua.software.pages.cluster.ClusterView;
import edu.tsinghua.software.pages.cql.CqlPage;
import edu.tsinghua.software.pages.keyspace.KeyspacePage;
import edu.tsinghua.software.pages.login.LoginPage;

public class LeftPanel extends Panel {
	private String clusterName;
	private ArrayList<String> keyspaceNameList;
	private DataManager dataManager;

	public LeftPanel(String id, String clusterNameInput,
			ArrayList<String> keyspaceListInput, DataManager client) {
		super(id);
		this.clusterName = clusterNameInput;
		this.keyspaceNameList = keyspaceListInput;
		this.dataManager = client;

		// homepage link
		add(new BookmarkablePageLink<Void>("homePageLink", ClusterView.class));
		// exit link
		Link logOutLink = new Link("logOutLink") {
			@Override
			public void onClick() {
				dataManager.diconnect();
			}
		};
		add(new BookmarkablePageLink<Void>("logOutLink", LoginPage.class));
		add(new BookmarkablePageLink<Void>("cqlLink", CqlPage.class));

		// seperate system , laud , custom keyspace

		ArrayList<String> cassandraSystemksList = new ArrayList<String>();
		ArrayList<String> laudSystemksList = new ArrayList<String>();

		// read laud System keyspace from file
		try {
			ServletContext context = WebApplication.get().getServletContext();
			FileInputStream fstream = new FileInputStream(
					context.getRealPath("laudSystemKs.property"));
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (keyspaceNameList.contains(strLine)) {
					laudSystemksList.add(strLine);
					keyspaceNameList.remove(strLine);
				}

			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		keyspaceNameList.remove("system");
		cassandraSystemksList.add("system");
		ListView listview = new ListView("listview", cassandraSystemksList) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				String keyspaceName = (String) item.getModelObject();
				Label l = new Label("linkName", keyspaceName);
				PageParameters params = new PageParameters();
				params.add("clusterParam", clusterName);
				params.add("keyspaceParam", item.getModelObject().toString());

				item.add(new BookmarkablePageLink<Void>("framePageLink",
						KeyspacePage.class, params).add(l));
			}
		};
		add(listview);

		ListView laudksListview = new ListView("laudksListview",
				laudSystemksList) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				String keyspaceName = (String) item.getModelObject();
				Label l = new Label("linkName", keyspaceName);

				PageParameters params = new PageParameters();
				// send two params to keyspacePage, cluster name and keyspace
				// name
				params.add("clusterParam", clusterName);
				params.add("keyspaceParam", item.getModelObject().toString());

				item.add(new BookmarkablePageLink<Void>("framePageLink",
						KeyspacePage.class, params).add(l));
			}
		};
		add(laudksListview);

		ListView customksListview = new ListView("customksListview",
				keyspaceNameList) {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				String keyspaceName = (String) item.getModelObject();
				Label l = new Label("linkName", keyspaceName);

				PageParameters params = new PageParameters();
				// send two params to keyspacePage, cluster name and keyspace
				// name
				params.add("clusterParam", clusterName);
				params.add("keyspaceParam", item.getModelObject().toString());

				item.add(new BookmarkablePageLink<Void>("framePageLink",
						KeyspacePage.class, params).add(l));
			}
		};
		add(customksListview);
	}

}
