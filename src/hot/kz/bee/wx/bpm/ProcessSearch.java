package kz.bee.wx.bpm;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import kz.bee.wx.security.User;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jboss.seam.log.Log;
import org.jbpm.graph.def.ProcessDefinition;

@Name("processSearch")
public class ProcessSearch {
	
	@In("entityManager")
	EntityManager em;

	@Logger
	Log log;
	
	private Long processId;
	private User initiator;
	private String processName;
	private Date beginDate,endDate;
	
	@In(value="listOfProcessInstances",scope=ScopeType.CONVERSATION,required=false)
	@Out(value="listOfProcessInstances",scope=ScopeType.CONVERSATION,required=false)
	List<Object> listOfProcessInstances;	

	
	@Factory(value="processDefinitionListForSearch",scope=ScopeType.PAGE)
	public List<ProcessDefinition> fetchProcessDefinitionList(){
		return (List<ProcessDefinition>) ManagedJbpmContext.instance().getGraphSession().findLatestProcessDefinitions();
	}
	
	@Begin(join = true)
	public void search(){		

		
		log.info("id: #0", this.processId);
		log.info("type:#0", this.processName);
		log.info("initiator:#0", this.initiator);
		log.info("begin:#0", beginDate);
		log.info("end:#0", endDate);
		
		Session session = ManagedJbpmContext.instance().getSession();
		try {
			
			if(this.processId != null || this.processName!=null || this.beginDate !=null || this.endDate!=null ){
			
				String queryString = "select p from org.jbpm.graph.exe.ProcessInstance p join fetch p.rootToken r join fetch r.node join fetch p.processDefinition where p.id is not null  "+
												(this.processId == null ? "" : "  AND (p.id = " + processId +")")+
												(this.processName == null ? "" : " AND (p.processDefinition.name = '" + processName +"')")+
												//(this.initiator == null ? "": " AND p.id = " + initiator )+
												
												((this.beginDate != null && this.endDate!=null)? " AND (p.start between  '" + new java.sql.Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate()) +"' and '" + new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDate()) +"')" : "")+
												((this.beginDate != null && this.endDate==null)? " AND (p.start >=  '" + new java.sql.Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate()) +"')" : "")+
												((this.beginDate == null && this.endDate!=null)? " AND (p.start <=  '" + new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDate()) +"')" : "")+
												
												
												
												//(this.endDate == null ? "" : " AND (p.end  between '" + new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDate()) +" 00:00:00' and '" + new java.sql.Date(endDate.getYear(),endDate.getMonth(),endDate.getDate()) +" 23:59:59')" )+
												" order by p.start desc";
				
				Query query = session.createQuery(queryString);
				log.info("queryString: #0", queryString);
				this.listOfProcessInstances = query.list();
				log.info("size: #0", this.listOfProcessInstances.size());
			}
			
		} catch (Exception e) {
			log.error(e);
		}	
		
		
	}
	
	
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public User getInitiator() {
		return initiator;
	}
	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}
	public String getProcessName() {
		return processName;
	}
	public void setprocessName(String processName) {
		this.processName = processName;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
