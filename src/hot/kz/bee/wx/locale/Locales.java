package kz.bee.wx.locale;

import javax.persistence.EntityManager;

import kz.bee.wx.core.Properties;
import kz.bee.wx.security.User;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.international.LocaleSelector;

@Name("kz.bee.wx.locale.locales")
@BypassInterceptors
public class Locales {

	@Observer("org.jboss.seam.localeSelected")
	public void setUserLocaleSettings(String locale){
		User user = (User) Component.getInstance("org.jboss.seam.security.management.authenticatedUser");
		if(user != null){	
			EntityManager em = (EntityManager) Component.getInstance("entityManager");
			if(user.getProperties() == null) {
				Properties properties = new Properties("USER_PROPERTIES_"+user.getName());
				em.persist(properties);
				user.setProperties(properties);
			}
			user.getProperties().put("locale", locale);
			user = em.merge(user);
		}	
	}
	
	@Observer("org.jboss.seam.security.loginSuccessful")
	public void getUserLocaleSettings(){
		User user = (User) Component.getInstance("org.jboss.seam.security.management.authenticatedUser");
		if(user != null && user.getProperties() != null && user.getProperties().get("locale") != null){
			LocaleSelector localeSelector =  (LocaleSelector) Component.getInstance("org.jboss.seam.international.localeSelector");
			localeSelector.setLocaleString(user.getProperties().get("locale"));
			localeSelector.select();
		}
	}
	
	public void selectKk(){
		select("kk");
	}
	
	public void selectRu(){
		select("ru");
	}
	
	public void selectEn(){
		select("en");
	}
	
	public void select(String locale){
		LocaleSelector localeSelector =  (LocaleSelector) Component.getInstance("org.jboss.seam.international.localeSelector");
		localeSelector.setLocaleString(locale);
		localeSelector.select();
	}
}
