package kz.bee.wx.bpm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Query;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.Actor;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.Messages;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmException;
import org.jbpm.taskmgmt.exe.TaskInstance;

@Name("wxArchivedTaskInstanceList")
@Scope(ScopeType.EVENT)
@SuppressWarnings("unchecked")
public class WxArchivedTaskInstanceList {
	
	private static String[] restrictedChars = {"&", "|", "!", ":", "'", "\"", "(", ")", "\\"};

	@Logger
	Log log;
	
	@In
	private FacesMessages facesMessages;

	static DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

	List<TaskInstance> taskInstanceList;

	@In(scope = ScopeType.SESSION, required = false)
	@Out(scope = ScopeType.SESSION, required = false)
	private String dateRange = "week";

	@In(scope = ScopeType.SESSION, required = false)
	@Out(scope = ScopeType.SESSION, required = false)
	private Date dateFrom;
	
	@In(scope = ScopeType.SESSION, required = false)
	@Out(scope = ScopeType.SESSION, required = false)
	private Date dateTo;

	@In(scope = ScopeType.SESSION, required = false)
	@Out(scope = ScopeType.SESSION, required = false)
	private String searchString;
	
	@Create
	public void create(){
		log.info("wxArchivedTaskInstanceList created");
	}
	
	/**
	 * Parse date range
	 * 
	 * 01.12.2011
	 * 01.02.2011 - 01.03.2011
	 */
	public void list(){
		
		log.info("Range: " + dateRange);
		
		if(dateRange != null){
			dateRange = dateRange.trim();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		// if dateRange not set then use default date range - this week
		if(dateRange == null || dateRange.length() == 0 || dateRange.equals("week")){
			// To: today
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			dateTo = calendar.getTime();

			// From: this Monday
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			dateFrom = calendar.getTime();
		}
		else if(dateRange.equals("today")){
			// From: today
			dateFrom = calendar.getTime();
			// To: today
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			dateTo = calendar.getTime();
		}
		else if(dateRange.equals("yesterday")){
			// From: yesterday
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			dateFrom = calendar.getTime();
			// To: yesterday
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			dateTo = calendar.getTime();
		}
		else if(dateRange.equals("month")){
			// To: today
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			dateTo = calendar.getTime();

			// From: this Monday
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			dateFrom = calendar.getTime();
		}
		// one day
		else if (dateRange.startsWith("date:")){
			try {
				dateFrom = format.parse(dateRange.substring(5));

				calendar.setTime(dateFrom);
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				dateTo = calendar.getTime();
			} catch (ParseException e) {
				facesMessages.addToControl("dateRange", Messages.instance().get("javax.faces.converter.DateTimeConverter.DATE_detail"), "01.02.2011");
			}
		}
		// date range
		else if (dateRange.startsWith("range:")){
			try {
				dateFrom = format.parse(dateRange.substring(6, 16));

				calendar.setTime(format.parse(dateRange.substring(19)));
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				dateTo = calendar.getTime();
			} catch (ParseException e) {
				facesMessages.addToControl("dateRange", Messages.instance().get("javax.faces.converter.DateTimeConverter.DATE_detail"), "01.02.2011 - 01.03.2011");
			}
		}
		else {
			facesMessages.addToControl("dateRange", Messages.instance().get("javax.faces.converter.DateTimeConverter.DATE_detail"), "01.02.2011, 01.02.2011 - 01.03.2011");
		}
		log.info("Date set: #0 - #1", dateFrom, dateTo);
		log.info("Search string set: #0", searchString);
	}
	
	public List<TaskInstance> findTaskInstancesByActorId() {
		if (taskInstanceList == null) {
			Actor actor = Actor.instance();
			if (actor != null && dateFrom != null && dateTo != null) {
				taskInstanceList = this.executeQuery("WxArchivedTaskInstanceList.findTaskInstancesByActorId", actor.getId(), dateFrom, dateTo, searchString);
			}
		}
		return taskInstanceList;
	}

	public List<TaskInstance> findTaskInstancesByActorId(String actorId) {
		if (taskInstanceList == null) {
			if (actorId != null && dateFrom != null && dateTo != null) {
				taskInstanceList = this.executeQuery("WxArchivedTaskInstanceList.findTaskInstancesByActorId", actorId, dateFrom, dateTo, searchString);
			}
		}
		return taskInstanceList;
	}

	public List<TaskInstance> executeQuery(String queryName, String actorId, Date dateFrom, Date dateTo, String searchString) {
		try {
			Query query = ManagedJbpmContext.instance().getSession().getNamedQuery(queryName);
			query.setString("actorId", actorId);
			query.setDate("dateFrom", dateFrom);
			query.setDate("dateTo", dateTo);
			query.setString("searchQuery", getSearchQuery(searchString));
			return query.list();
		} catch (Exception e) {
			throw new JbpmException("couldn't get task instances list for actor '" + actorId + "'", e);
		}
	}

	public String getDateRange() {
		if(dateRange == null){
			dateRange = "week";
		}
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		if(!"same".equals(dateRange)){
			this.dateRange = dateRange;
		}
	}

	public Date getDateFrom() {
		if(dateFrom == null){
			list();
		}
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		if(dateTo == null){
			list();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTo);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();

	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
	public static String getSearchQuery(String searchString){
		if(searchString == null){
			return null;
		}
		else if(searchString.trim().isEmpty()){
			return null;
		}
		else {
			StringBuffer searchQuery = new StringBuffer();
			String temp = searchString.trim();
			for(String restricted : restrictedChars){
				temp = temp.replace(restricted, "");
			}
			StringTokenizer st = new StringTokenizer(temp, " ");
			while(st.hasMoreTokens()){
				searchQuery.append(st.nextToken().trim());
				searchQuery.append(" & ");
			}
			if(searchQuery.length() > 2){
				searchQuery.delete(searchQuery.length()-3, searchQuery.length());
			}
			return searchQuery.length() > 0 ? searchQuery.toString() : null;
		}
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	
}
