package kz.bee.wx.bpm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("customTaskInstanceList")
@Scope(ScopeType.EVENT)
@SuppressWarnings("unchecked")
public class CustomTaskInstanceList {

	@In(scope = ScopeType.PAGE, required = false)
	@Out(scope = ScopeType.PAGE, required = false)
	private String searchString;

	List<TaskInstance> taskInstanceList, nonSignallingTaskInstanceList;
	Long count, countSignalling, countNonSignalling;
	Map<String, Long> processDefinitionTaskInstanceCounts = new HashMap<String, Long>();
	
	@In
	Actor actor;

	public List<TaskInstance> list() {
		if (taskInstanceList == null) {
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.findSignallingTaskInstancesByActorId");
					query.setString("actorId", actor.getId());
					query.setString("searchQuery", WxArchivedTaskInstanceList.getSearchQuery(searchString));
					taskInstanceList = query.list();
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
		}

		return taskInstanceList;
	}

	public List<TaskInstance> listNonSignalling() {
		if (nonSignallingTaskInstanceList == null) {
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.findNonSignallingTaskInstancesByActorId");
					query.setString("actorId", actor.getId());
					query.setString("searchQuery", WxArchivedTaskInstanceList.getSearchQuery(searchString));
					nonSignallingTaskInstanceList = query.list();
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
		}

		return nonSignallingTaskInstanceList;
	}

	public long count() {
		if(count == null){
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.countTaskInstancesByActorId");
					query.setString("actorId", actor.getId());
					count = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
			else {
				count = 0L;
			}
		}
		
		return count;
	}

	public long count(String processDefinitionName) {
		if(processDefinitionTaskInstanceCounts.get(processDefinitionName) == null){
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.countProcessDefinitionTaskInstances");
					query.setString("processDefinitionName", processDefinitionName);
					query.setString("actorId", actor.getId());
					processDefinitionTaskInstanceCounts.put(processDefinitionName, (Long)query.uniqueResult());
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
			else {
				processDefinitionTaskInstanceCounts.put(processDefinitionName, 0L);
			}
		}
		
		return processDefinitionTaskInstanceCounts.get(processDefinitionName);
	}

	public long countSignalling() {
		if(countSignalling == null){
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.countSignallingTaskInstancesByActorId");
					query.setString("actorId", actor.getId());
					countSignalling = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
			else {
				countSignalling = 0L;
			}
		}
		
		return countSignalling;
	}

	public long countSignalling(String processDefinitionName) {
		if(processDefinitionTaskInstanceCounts.get(processDefinitionName) == null){
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.countSignallingProcessDefinitionTaskInstances");
					query.setString("processDefinitionName", processDefinitionName);
					query.setString("actorId", actor.getId());
					processDefinitionTaskInstanceCounts.put(processDefinitionName, (Long)query.uniqueResult());
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
			else {
				processDefinitionTaskInstanceCounts.put(processDefinitionName, 0L);
			}
		}
		
		return processDefinitionTaskInstanceCounts.get(processDefinitionName);
	}


	public long countNonSignalling() {
		if(countNonSignalling == null){
			if (actor != null) {
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomTaskInstanceList.countNonSignallingTaskInstancesByActorId");
					query.setString("actorId", actor.getId());
					countNonSignalling = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get task instances list for actor '" + actor.getId() + "'", e);
				}
			}
			else {
				countNonSignalling = 0L;
			}
		}
		
		return countNonSignalling;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
