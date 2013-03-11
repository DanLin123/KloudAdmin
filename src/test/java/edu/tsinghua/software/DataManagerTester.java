package edu.tsinghua.software;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.thrift.TException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.tsinghua.software.cassandra.tools.ClusterConnection;
import edu.tsinghua.software.cassandra.tools.DataManager;

public class DataManagerTester {
	String host = "localhost";
	int JMX = 7199;
	int thrift = 9160;
	DataManager dataManager;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ClusterConnection connection = new ClusterConnection(host, thrift, JMX);
		connection.connect();
		dataManager = new DataManager(connection);
	}

	@After
	public void tearDown() throws Exception {
	}



	@Test
	public void testDescribeClustername()
	{
		String clusterName = "";
		try {
			clusterName	= dataManager.describeClusterName();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("Test Cluster", clusterName);	
	}
	
	@Test 
	public void testUpdateKeyspace1()
	{
		String keyspaceName = "cgyu";
		String strategy = "SimpleStrategy";
		Map<String, String> strategyOptions = new HashMap<String,String>();
		strategyOptions.put("datacenter1", "2");
		strategyOptions.put("datacenter", "1");
		int factor = 2;
		strategyOptions.clear();
		try {
			dataManager.updateKeyspace(keyspaceName,strategy,strategyOptions,factor);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchemaDisagreementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testUpdateKeyspace2()
	{
		String keyspaceName = "cgyu";
		String strategy = "NetworkTopologyStrategy";
		Map<String, String> strategyOptions = new HashMap<String,String>();
		strategyOptions.put("datacenter1", "2");
		strategyOptions.put("datacenter", "1");
		int factor = 2;
		try {
			dataManager.updateKeyspace(keyspaceName,strategy,strategyOptions,factor);
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchemaDisagreementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
