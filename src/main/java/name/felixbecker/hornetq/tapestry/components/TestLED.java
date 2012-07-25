package name.felixbecker.hornetq.tapestry.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class TestLED {
	@Parameter(required=true)
	@Property
	boolean test;
	
	@Parameter(required=true, defaultPrefix=BindingConstants.LITERAL)
	@Property
	String label;
}