/**
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package edu.tsinghua.software.pages.cluster;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.tsinghua.software.cassandra.*;
import org.apache.cassandra.dht.Token;
import edu.tsinghua.software.cassandra.node.NodeInfo;
import edu.tsinghua.software.cassandra.node.RingNode;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.apache.wicket.markup.html.basic.Label;

/**
 * file:ClusterPropertuModel.java
 * the model for ClusterView, which contains all the properties of cluster
 * created at:11-03-2012
 * @author 林丹
 * */
public class ClusterPropertyModel {

	//cluster name
	private String name = "";
	//clsuter snitch
	private String snitch = "";
	//cluster patitioner
	private String patitioner = "";
	//cluster schema
	private String schema ="";
	//cluster api
	private String api = "";
	//cluster keyspace
	private String keyspace ="";
	//node on the ring
	private ArrayList<NodeInfo> nodes = new ArrayList<NodeInfo>();
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSnitch() {
		return snitch;
	}
	public void setSnitch(String snitch) {
		this.snitch = snitch;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getPatitioner() {
		return patitioner;
	}
	public void setPatitioner(String patitioner) {
		this.patitioner = patitioner;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public ArrayList<NodeInfo> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<NodeInfo> nodes) {
		this.nodes = nodes;
	}

}
