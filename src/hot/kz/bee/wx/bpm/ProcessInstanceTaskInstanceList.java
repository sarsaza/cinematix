package kz.bee.wx.bpm;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Unwrap;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("processInstanceTaskInstanceList")
@SuppressWarnings("unchecked")
public class ProcessInstanceTaskInstanceList {
	
	List<TaskInstance> taskInstanceList;
	
	@Unwrap
	public List<TaskInstance> fetchTaskInstanceList(){
		if(taskInstanceList == null){
			if(org.jboss.seam.bpm.ProcessInstance.instance() != null){	
				Session session = ManagedJbpmContext.instance().getSession();
				try {
					String queryString = "select ti from org.jbpm.taskmgmt.exe.TaskInstance ti where ti.token.processInstance = :processInstance order by ti.id";
					Query query = session.createQuery(queryString);
					query.setParameter("processInstance", org.jboss.seam.bpm.ProcessInstance.instance());
					taskInstanceList = query.list(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
		return taskInstanceList;	
	}

	public static List<TaskInstance> instance() {
		return (List<TaskInstance>) Component.getInstance(ProcessInstanceTaskInstanceList.class, ScopeType.EVENT);
	}
}
