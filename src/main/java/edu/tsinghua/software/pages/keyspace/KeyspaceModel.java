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

package edu.tsinghua.software.pages.keyspace;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.form.TextField;

/**
 * KeyspaceModel 
 * @author 林丹
 */
public final class KeyspaceModel implements IClusterable{

	private static final long serialVersionUID = 1L;
	private String keyspaceName;
	private Integer relicationFactor;
	private String strategy;
	private String strategyOptions;
	public KeyspaceModel()
	{
		
	}
	
	public KeyspaceModel(String keyspaceName, int replicationFactor, String strategy, String strategyOptions)
	{
		this.setKeyspaceName(keyspaceName);
		this.setRelicationFactor(replicationFactor);
		this.setStrategy(strategy);
		this.setStrategyOptions(strategyOptions);
	}

	public String getKeyspaceName() {
		return keyspaceName;
	}

	public void setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
	}

	public Integer getRelicationFactor() {
		return relicationFactor;
	}

	public void setRelicationFactor(Integer relicationFactor) {
		this.relicationFactor = relicationFactor;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getStrategyOptions() {
		return strategyOptions;
	}

	public void setStrategyOptions(String strategyOptions) {
		this.strategyOptions = strategyOptions;
	}

}
