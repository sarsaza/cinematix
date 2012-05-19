package kz.bee.kudos.bim;

import java.net.URL;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.annotations.Unwrap;

import freemarker.cache.URLTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

@Startup
@Name("templateConfiguration")
@Scope(ScopeType.APPLICATION)
public class TemplateConfiguration {
	private Configuration cfg;

	@Unwrap
	public Configuration getTemplateConfiguration() {
		if (this.cfg == null) {
			cfg = new Configuration();
			cfg.setTemplateLoader(new URLTemplateLoader() {
				@Override
				protected URL getURL(String name) {
					return Thread.currentThread().getContextClassLoader().getResource(name);
				}
			});
			BeansWrapper wrapper = new BeansWrapper(); 
	        wrapper.setSimpleMapWrapper(true); 
	        cfg.setObjectWrapper(wrapper); 
		}
		return this.cfg;
	}
}