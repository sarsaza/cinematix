package kz.bee.wx.bpm;

import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.bpm.TaskInstance;

@Name("bpm")
@Scope(ScopeType.CONVERSATION)
public class Bpm {

	private boolean showTaskList = false;

	private boolean showDiagram = false;

	public boolean isShowTaskList() {
		return showTaskList;
	}

	public void setShowTaskList(boolean showTaskList) {
		this.showTaskList = showTaskList;
	}
	
	public void showTaskList(){
		this.showTaskList = true;
	}

	public boolean isShowDiagram() {
		return showDiagram;
	}

	public void setShowDiagram(boolean showDiagram) {
		this.showDiagram = showDiagram;
	}
	
	public void showDiagram(){
		this.showDiagram = true;
	}
	
	@Observer("kz.bee.wx.bpm.begin")
	public void markTaskInstanceAsRead(){
		try {
			TaskInstance.instance().start();
		}
		catch(Exception e){
			
		}
	}

}
