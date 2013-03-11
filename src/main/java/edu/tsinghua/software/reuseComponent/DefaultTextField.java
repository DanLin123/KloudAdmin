package edu.tsinghua.software.reuseComponent;
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

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.TextField;

/**
 * DefaultTextField ,TextField with default values
 * @author 林丹
 * */
public class DefaultTextField extends TextField{
	private String id;
	private String text;
	private float textFloat;
	
	public DefaultTextField(String id, String text)
	{
		super(id);
		this.id = id;
		this.text = text;		
		this.add(new SimpleAttributeModifier("value", text));
	}
	public DefaultTextField(String id, float textFloat)
	{
		super(id);
		this.id = id;
		this.textFloat = textFloat;		
		this.add(new SimpleAttributeModifier("value", textFloat+""));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String gettext() {
		return text;
	}
	public void settext(String text) {
		this.text = text;
	}
	

}
