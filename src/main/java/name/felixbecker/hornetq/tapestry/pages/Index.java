package name.felixbecker.hornetq.tapestry.pages;

import name.felixbecker.hornetq.services.HornetQService;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;


public class Index {

	@Inject
	@Property
	HornetQService hornetQService;

	@InjectComponent
	Zone formZone;
	
	@InjectComponent
	Zone sidebarZone;

	@Inject
	AjaxResponseRenderer ajaxResponseRenderer;
	
	void onSuccess(){
		ajaxResponseRenderer.addRender("formZone", formZone).addRender("sidebarZone", sidebarZone);
	}
	
}
