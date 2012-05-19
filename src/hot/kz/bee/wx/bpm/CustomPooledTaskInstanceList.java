package kz.bee.wx.bpm;

import java.util.ArrayList;
import java.util.List;

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

@Name("customPooledTaskInstanceList")
@Scope(ScopeType.EVENT)
@SuppressWarnings("unchecked")
public class CustomPooledTaskInstanceList {

	@In(scope = ScopeType.PAGE, required = false)
	@Out(scope = ScopeType.PAGE, required = false)
	private String searchString;

	List<TaskInstance> taskInstanceList, nonSignallingTaskInstanceList;
	Long count, countSignalling, countNonSignalling;

	public List<TaskInstance> list() {
		if (taskInstanceList == null) {
			Actor actor = Actor.instance();
			String actorId = actor.getId();
			if (actorId != null) {
				ArrayList groupIds = new ArrayList(actor.getGroupActorIds());
				groupIds.add(actorId);

				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomPooledTaskInstanceList.findSignallingPooledTaskInstancesByActorIds");
					query.setParameterList("actorIds", groupIds);
					query.setString("searchQuery", WxArchivedTaskInstanceList.getSearchQuery(searchString));
					taskInstanceList = query.list();
				} catch (Exception e) {
					throw new JbpmException("couldn't get pooled task instances list for actors '" + groupIds + "'", e);
				}
			}
		}

		return taskInstanceList;
	}

	public List<TaskInstance> listNonSignalling() {
		if (nonSignallingTaskInstanceList == null) {
			Actor actor = Actor.instance();
			String actorId = actor.getId();
			if (actorId != null) {
				ArrayList groupIds = new ArrayList(actor.getGroupActorIds());
				groupIds.add(actorId);

				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomPooledTaskInstanceList.findNonSignallingPooledTaskInstancesByActorIds");
					query.setParameterList("actorIds", groupIds);
					query.setString("searchQuery", WxArchivedTaskInstanceList.getSearchQuery(searchString));
					nonSignallingTaskInstanceList = query.list();
				} catch (Exception e) {
					throw new JbpmException("couldn't get pooled task instances list for actors '" + groupIds + "'", e);
				}
			}
		}

		return nonSignallingTaskInstanceList;
	}

	public long count() {
		if(count == null) {
			Actor actor = Actor.instance();
			String actorId = actor.getId();
			if (actorId != null) {
				ArrayList groupIds = new ArrayList(actor.getGroupActorIds());
				groupIds.add(actorId);
	
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomPooledTaskInstanceList.countPooledTaskInstancesByActorIds");
					query.setParameterList("actorIds", groupIds);
					count = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get pooled task instances list for actors '" + groupIds + "'", e);
				}
			}
			else {
				count = 0L;
			}
		}
		
		return count;
	}

	public long countSignalling() {
		if(countSignalling == null) {
			Actor actor = Actor.instance();
			String actorId = actor.getId();
			if (actorId != null) {
				ArrayList groupIds = new ArrayList(actor.getGroupActorIds());
				groupIds.add(actorId);
	
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomPooledTaskInstanceList.countSignallingPooledTaskInstancesByActorIds");
					query.setParameterList("actorIds", groupIds);
					countSignalling = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get pooled task instances list for actors '" + groupIds + "'", e);
				}
			}
			else {
				countSignalling = 0L;
			}
		}
		
		return countSignalling;
	}

	public long countNonSignalling() {
		if(countNonSignalling == null) {
			Actor actor = Actor.instance();
			String actorId = actor.getId();
			if (actorId != null) {
				ArrayList groupIds = new ArrayList(actor.getGroupActorIds());
				groupIds.add(actorId);
	
				try {
					Query query = ManagedJbpmContext.instance().getSession().getNamedQuery("CustomPooledTaskInstanceList.countNonSignallingPooledTaskInstancesByActorIds");
					query.setParameterList("actorIds", groupIds);
					countNonSignalling = (Long)query.uniqueResult();
				} catch (Exception e) {
					throw new JbpmException("couldn't get pooled task instances list for actors '" + groupIds + "'", e);
				}
			}
			else {
				countNonSignalling = 0L;
			}
		}
		
		return countNonSignalling;
	}
}
