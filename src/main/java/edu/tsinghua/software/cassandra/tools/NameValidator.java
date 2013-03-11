package edu.tsinghua.software.cassandra.tools;

import org.apache.wicket.validation.CompoundValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;
 
public class NameValidator extends CompoundValidator<String> {
 
	private static final long serialVersionUID = 1L;
 
	public NameValidator() {
 
		add(StringValidator.lengthBetween(1, 15));
		add(new PatternValidator("[a-z0-9_-]+"));
 
	}
}