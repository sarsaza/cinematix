package kz.bee.kudos.bim;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import kz.bee.wx.security.Group;
import kz.bee.wx.security.User;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.Messages;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Name("charts")
public class Charts {
	
	@In("templateConfiguration")
	Configuration cfg;
	
	@In
	FacesContext facesContext;
	
	@In("entityManager")
	EntityManager em;
	
	
	private String urlPath = "";
	
	@Factory(value="totalMarkByStudent",scope=ScopeType.EVENT)
	public String XMLindoc4answer() throws TemplateException, IOException{
		Template template = cfg.getTemplate("/freemarker/totalMarkByStudent.ftl");
		Map<String, Object> root = new HashMap<String, Object>();
		List<Object[]> dataList = new ArrayList<Object[]>();
		
		List<Object[]> resultList = em.createQuery("select count(m.id), m.student.name from Mark m group by m.student.name order by count(*) desc").setMaxResults(20).getResultList();
		System.out.println(resultList.size());
		for(Object[] obj:resultList){
			Object[] t = new Object[2];
			t[0] = obj[0];
			
			User user = em.find(User.class, obj[1]);
			Group group = (Group)em.createQuery("select m.group from Membership m where m.user = :user and m.role = 'STUDENT'").setParameter("user", user).getResultList().get(0);
			String t1 = user.getFirstname() + " " + user.getLastname()+"("+Messages.instance().get("Group."+group.getName()+".name")+")";
			t[1]=t1;
			dataList.add(t);
		}
		root.put("data", dataList);
		root.put("urlPath", this.getUrlPath());
		Writer out = new StringWriter();
		template.process(root, out);
		String ans = out.toString();
		ans = ans.replaceAll("\\r", "").replaceAll("\\n", "");
		System.out.println(ans);
		return ans;
	}
	
	public String getUrlPath() {
		if(urlPath.isEmpty()){
			Map map = facesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();
			urlPath = map.get("host")+facesContext.getExternalContext().getRequestContextPath();
		}
		return urlPath;
	}
}
