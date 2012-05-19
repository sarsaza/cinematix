package kz.bee.wx.bpm;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.bee.wx.jbpm.context.def.VariableList;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.ManagedJbpmContext;
import org.jboss.seam.log.Log;
import org.jbpm.graph.exe.ProcessInstance;

@Name("kz.bee.wx.bpm.processInstanceContext")
public class ProcessInstanceContext implements Map<String, Object> {

	@In(value = "processInstance")
	ProcessInstance processInstance;
	
	@Logger
	Log log;
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Object get(Object key) {
		Object value = processInstance.getContextInstance().getVariable((String) key);
		log.debug("getting variable '#0': #1", key, value);
		return value;
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	public String[] getNames() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Object put(String key, Object value) {
		return this.set(key, value);
	}

	@SuppressWarnings("unchecked")
	public Object set(String key, Object value) {
		log.debug("setting variable '#0': #1", key, value);
		if((value instanceof Collection) && !(value instanceof VariableList)){
			VariableList list = new VariableList();
			list.addAll((List)value);
			ManagedJbpmContext.instance().getSession().save(list);
			processInstance.getContextInstance().setVariable(key, list);
			return list;
		}
		else {
			processInstance.getContextInstance().setVariable(key, value);
			return value;
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException("Current Map implementation does not support the operation");
	}

	public static ProcessInstanceContext instance() {
		return (ProcessInstanceContext) Component.getInstance(ProcessInstanceContext.class, ScopeType.EVENT);
	}

}
