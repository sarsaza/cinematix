package kz.bee.wx.bpm;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import kz.bee.wx.fs.File;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityIdentifier;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("wxTaskInstanceList")
@SuppressWarnings("unchecked")
public class WxTaskInstanceList {

	public String processDefinitionName;
	public String taskName;
	public String actorId;
	public String groupActorId;

	@Logger
	Log log;

	@In
	JbpmContext jbpmContext;

	public void list() {

	}

	public TaskInstance findTaskInstanceByNameAndTokenId(String taskName, Long tokenId) {
		return this.executeQuery("WxTaskInstanceList.findTaskInstanceByNameAndTokenId", taskName, tokenId);
	}

	public List<Object[]> findProcessDefinitionNamesByPooledTaskInstanceActorIds(String... actorIds) {
		return this.executeQuery("WxTaskInstanceList.findProcessDefinitionNamesByPooledTaskInstanceActorIds", Arrays.asList(actorIds));
	}

	public List<Object[]> findProcessDefinitionNamesByTaskInstanceActorId(String actorId) {
		return this.executeQuery("WxTaskInstanceList.findProcessDefinitionNamesByTaskInstanceActorId", actorId);
	}

	public List<Object[]> findPooledTaskInstanceNamesByActorIds(String... actorIds) {
		return this.executeQuery("WxTaskInstanceList.findPooledTaskInstanceNamesByActorIds", Arrays.asList(actorIds));
	}

	public List<Object[]> findPooledTaskInstancesByActorIdsGroupedByDueDateAndActorId(String... actorIds) {
		return this.executeQuery("WxTaskInstanceList.findPooledTaskInstancesByActorIdsGroupedByDueDateAndActorId", Arrays.asList(actorIds));
	}

	public List<Object[]> findPooledTaskInstancesByActorIdsAndDueDateGroupedByDueDate(Date startDate, Date endDate, String... actorIds) {
		return this.executeQuery("WxTaskInstanceList.findPooledTaskInstancesByActorIdsAndDueDateGroupedByDueDate", Arrays.asList(actorIds), startDate, endDate);
	}

	public List<Object[]> findTaskInstanceNamesByActorId(String actorId) {
		return this.executeQuery("WxTaskInstanceList.findTaskInstanceNamesByActorId", actorId);
	}

	public List<TaskInstance> findPooledTaskInstances(String processDefintionName, String taskName, String groupActorId) {
		return this.executeQuery("WxTaskInstanceList.findPooledTaskInstances", processDefintionName, taskName, groupActorId);
	}

	public List<TaskInstance> findTaskInstances(String processDefintionName, String taskName, String actorId) {
		return this.executeQuery("WxTaskInstanceList.findTaskInstances", processDefintionName, taskName, actorId);
	}

	public List<TaskInstance> findPooledTaskInstancesByPooledActorIds(String... actorIds) {
		return this.executeQuery2("WxTaskInstanceList.findPooledTaskInstancesByPooledActorIds", Arrays.asList(actorIds));
	}

	public List<Object[]> findPooledTaskInstancesByPooledActorIdsAndByDate(Date date, String... actorIds) {
		return this.executeQuery4("WxTaskInstanceList.findPooledTaskInstancesByPooledActorIdsAndByDate", date, Arrays.asList(actorIds));
	}

	public List<Object[]> findPooledTaskInstancesByActorIdAndByDate(Date date, String actorId) {
		return this.executeQuery5("WxTaskInstanceList.findPooledTaskInstancesByActorIdAndByDate", date, actorId);
	}

	public List<TaskInstance> findTaskInstancesByActorId(String actorId) {
		return this.executeQuery3("WxTaskInstanceList.findTaskInstancesByActorId", actorId);
	}

	public List<Object[]> findActorIdsByPooledActorIds(String... actorIds) {
		return this.executeQuery("WxTaskInstanceList.findActorIdsByPooledActorIds", Arrays.asList(actorIds));
	}

	private TaskInstance executeQuery(String queryName, String taskName, Long tokenId) {
		TaskInstance result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameter("taskName", taskName);
			query.setParameter("tokenId", tokenId);
			result = (TaskInstance) query.uniqueResult();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get task instance for task name '" + taskName + "' and for token id '" + tokenId + "'" + e);
		}
		return result;
	}

	private List<Object[]> executeQuery(String queryName, List<String> actorIds) {
		List<Object[]> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameterList("actorIds", actorIds);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorIds + "'", e);
		}
		return result;
	}

	private List<Object[]> executeQuery(String queryName, List<String> actorIds, Date startDate, Date endDate) {
		List<Object[]> result = null;
		Session session = jbpmContext.getSession();
		try {
			java.sql.Date start = new java.sql.Date(startDate.getTime()); 
			java.sql.Date end = new java.sql.Date(endDate.getTime()); 
			Query query = session.getNamedQuery(queryName);
			query.setParameterList("actorIds", actorIds);
			query.setParameter("startDate", start);
			query.setParameter("endDate", end);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorIds + "'", e);
		}
		return result;
	}

	private List<TaskInstance> executeQuery2(String queryName, List<String> actorIds) {
		List<TaskInstance> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameterList("actorIds", actorIds);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorIds + "'", e);
		}
		return result;
	}

	private List<TaskInstance> executeQuery3(String queryName, String actorId) {
		List<TaskInstance> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameter("actorId", actorId);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorId + "'", e);
		}
		return result;
	}

	private List<Object[]> executeQuery4(String queryName, Date date, List<String> actorIds) {
		List<Object[]> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameterList("actorIds", actorIds);
			query.setParameter("date", new java.sql.Date(date.getYear(), date.getMonth(), date.getDate()));
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorIds + "'", e);
		}
		return result;
	}

	private List<Object[]> executeQuery5(String queryName, Date date, String actorId) {
		List<Object[]> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameter("actorId", actorId);
			query.setParameter("date", new java.sql.Date(date.getYear(), date.getMonth(), date.getDate()));
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + actorId + "'", e);
		}
		return result;
	}

	private List<Object[]> executeQuery(String queryName, String actorId) {
		List<Object[]> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery("TaskMgmtSession.findProcessDefinitionNamesByTaskInstanceActorId");
			query.setParameter("actorId", actorId);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for task instance list actor '" + actorId + "'", e);
		}
		return result;
	}

	private List<TaskInstance> executeQuery(String queryName, String processDefinitionName, String taskName, String groupActorId) {
		List<TaskInstance> result = null;
		Session session = jbpmContext.getSession();
		try {
			Query query = session.getNamedQuery(queryName);
			query.setParameter("actorId", groupActorId);
			query.setParameter("processDefinitionName", processDefinitionName);
			query.setParameter("taskName", taskName);
			result = query.list();
		} catch (Exception e) {
			log.error(e);
			throw new JbpmException("couldn't get process definitions for pooled task instance list actors '" + groupActorId + "'", e);
		}
		return result;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<ProcessInstance> findActiveProcessInstancesWithEntity(Object entity) {
		EntityManager em = getPersistenceContext();
		EntityIdentifier entityIdentifier;
		List<ProcessInstance> result = null;

		entityIdentifier = new EntityIdentifier(entity, em);
		String queryName = null;
		if (entityIdentifier.getId().getClass() == Long.class) {
			queryName = "WxTaskInstanceList.findActiveProcessInstancesWithEntity(Long)";
		} else if (entityIdentifier.getClazz() == String.class) {
			queryName = "WxTaskInstanceList.findActiveProcessInstancesWithEntity(String)";
		}

		Session session = jbpmContext.getSession();
		result = session.getNamedQuery(queryName).setParameter("entityClassName", entity.getClass().getName()).setParameter("entityId", entityIdentifier.getId()).list();

		return result;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<TaskInstance> findOpenTaskInstancesWithEntity(Object entity) {
		EntityManager em = getPersistenceContext();
		EntityIdentifier entityIdentifier;
		List<TaskInstance> result = null;

		entityIdentifier = new EntityIdentifier(entity, em);
		String queryName = null;
		if (entityIdentifier.getId().getClass() == Long.class) {
			queryName = "WxTaskInstanceList.findOpenTaskInstancesWithEntity(Long)";
		} else if (entityIdentifier.getClazz() == String.class) {
			queryName = "WxTaskInstanceList.findOpenTaskInstancesWithEntity(String)";
		}

		Session session = jbpmContext.getSession();
		result = session.getNamedQuery(queryName).setParameter("entityClassName", entity.getClass().getName()).setParameter("entityId", entityIdentifier.getId()).list();

		return result;
	}

	public List<ProcessInstance> listPi() {
		File file = getPersistenceContext().find(File.class, 932722L);
		return this.findActiveProcessInstancesWithEntity(file);
	}

	public List<TaskInstance> listTi() {
		File file = getPersistenceContext().find(File.class, 932722L);
		return this.findOpenTaskInstancesWithEntity(file);
	}

	protected EntityManager getPersistenceContext() {
		return (EntityManager) Component.getInstance("entityManager", ScopeType.APPLICATION);
	}

}
