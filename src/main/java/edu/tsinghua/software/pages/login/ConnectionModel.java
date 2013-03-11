package edu.tsinghua.software.pages.login;

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


import org.apache.wicket.IClusterable;

/**
 * ConnectionModel for login Page 
 */
public final class ConnectionModel implements IClusterable{

	private static final long serialVersionUID = 1L;
	private String host = "localhost";
	private Integer thrift = 9160;
	private Integer JMX = 7199;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getThrift() {
		return thrift;
	}
	public void setThrift(Integer thrift) {
		this.thrift = thrift;
	}
	public Integer getJMX() {
		return JMX;
	}
	public void setJMX(Integer jMX) {
		JMX = jMX;
	}
	
	   /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
     StringBuilder b = new StringBuilder();
        b.append("[TestInputObject host = '")
            .append(host)
            .append("', host = ")
            .append(host)
            .append(", thrift = ")
            .append(thrift)
            .append(", JMX = ")
            .append(JMX);
        return b.toString();
    }

}
