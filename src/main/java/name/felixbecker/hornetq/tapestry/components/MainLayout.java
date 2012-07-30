package name.felixbecker.hornetq.tapestry.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

@Import(
		library = {
				"context:bootstrap/js/bootstrap.js"
		},
		
		stylesheet = {
				"context:bootstrap/css/bootstrap.css"
		}
		
		)
public class MainLayout {

		@Property
		@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
		private String title;
}
