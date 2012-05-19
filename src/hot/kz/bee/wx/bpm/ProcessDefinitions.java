package kz.bee.wx.bpm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;

import kz.bee.wx.i18n.ResourceBundle;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;
import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.node.NodeTypes;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@Name("kz.bee.wx.bpm.processDefinitions")
public class ProcessDefinitions {
	
	@Logger
	Log log;

	@RequestParameter
	Long processDefinitionId;

	@RequestParameter
	String processDefinitionName;

	@In
	JbpmContext jbpmContext;

	List<ProcessDefinition> latestProcessDefinitionList;

	List<ProcessDefinition> processDefinitionVersionList;

	List<String> processDefinitionFiles;

	@In(scope = ScopeType.CONVERSATION, required = false)
	@Out(scope = ScopeType.CONVERSATION, required = false)
	ProcessDefinition processDefinition;
	
	byte[] fileData;

	@In(scope = ScopeType.CONVERSATION, required = false)
	@Out(scope = ScopeType.CONVERSATION, required = false)
	ResourceBundle resourceBundle;
	
	@RequestParameter
	String messagesFileName;

	public List<ProcessDefinition> getLatestProcessDefinitionList() {
		if (latestProcessDefinitionList == null) {
			latestProcessDefinitionList = new ArrayList<ProcessDefinition>();
			latestProcessDefinitionList.addAll(jbpmContext.getGraphSession().findLatestProcessDefinitions());
		}

		return latestProcessDefinitionList;
	}

	public List<ProcessDefinition> getProcessDefinitionVersionList() {
		if (processDefinitionVersionList == null) {
			processDefinitionVersionList = new ArrayList<ProcessDefinition>();
			processDefinitionVersionList.addAll(jbpmContext.getGraphSession().findAllProcessDefinitionVersions(this.processDefinitionName != null ? this.processDefinitionName : this.processDefinition.getName()));
		}

		return processDefinitionVersionList;
	}
	
	public List<String> getProcessDefinitionFiles(){
		if(processDefinitionFiles == null){
			processDefinitionFiles = new ArrayList<String>();
			processDefinitionFiles.addAll(jbpmContext.getGraphSession().getProcessDefinition(processDefinition.getId()).getFileDefinition().getBytesMap().keySet());
		}
		return processDefinitionFiles;
	}

	public void edit() {
		this.processDefinition = jbpmContext.getGraphSession().getProcessDefinition(processDefinitionId);
	}
	
	public byte[] getFile(String name) {
		return jbpmContext.getGraphSession().getProcessDefinition(processDefinition.getId()).getFileDefinition().getBytes(name);
	}
	
	public String getFileType(String name){
		if(name.endsWith(".xhtml")){
			return "task-form";
		}
		else if(name.equals("processdefinition.xml")){
			return "process-definition";
		}
		else if(name.equals("processimage.jpg")){
			return "process-image";
		}
		else if(name.startsWith("messages") && name.endsWith(".properties")){
			return "messages-properties";
		}
		else {
			return "file";
		}
		
	}
	
	public void showMessages(){
		this.resourceBundle = getResourceBundle(this.messagesFileName);
	}
	
	/**
	 * Get ResourceBundle by messagesFileName
	 * 
	 * Actually parses messages_{LANGUAGE}_{COUNTRY}_{VARIANT}.properties
	 * and gets ResourceBundle(LANGUAGE, COUNTRY, VARIANT) from persistence context
	 * 
	 * @param messagesFileName
	 * 
	 * @return kz.bee.wx.i18n.ResourceBundle
	 */
	public ResourceBundle getResourceBundle(String messagesFileName) {
		String localeString = messagesFileName.replaceAll("messages", "").replaceAll(".properties", "");
		String language = "", country = "", variant = "";
		if (localeString.length() > 0) {
			localeString = localeString.substring(1);
			StringTokenizer tokenizer = new StringTokenizer(localeString, "_");
			int tokenCount = tokenizer.countTokens();
			if (tokenCount >= 1) {
				language = tokenizer.nextToken();
			}
			if (tokenCount >= 2) {
				country = tokenizer.nextToken();
			}
			if (tokenCount >= 3) {
				variant = tokenizer.nextToken();
			}
		}

		EntityManager em = (EntityManager) Component.getInstance("entityManager", ScopeType.APPLICATION);
		List<ResourceBundle> resourceBundleList = (List<ResourceBundle>) em.createNamedQuery("resourceBundles").setParameter("language", language).setParameter("country", country).setParameter("variant", variant).getResultList();
		if (resourceBundleList.size() > 0) {
			return resourceBundleList.get(0);
		}
		
		return null;
	}

	public List<Map.Entry<String, String>> getMessages() throws DocumentException, IOException{
		return this.getMessages(this.resourceBundle);
	}
	
	/**
	 * Constructs messages_{LANGUAGE}_{COUNTRY}_{VARIANT}.properties from all the entries previously uploaded
	 * with process definition par + entries missed by developer (if any)
	 * 
	 * @param ResourceBundle - in order to identify which messages_*.properties file to get
	 * 
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public List<Map.Entry<String, String>> getMessages(ResourceBundle resourceBundle) throws DocumentException, IOException{
		Map<String, String> messages = new TreeMap<String, String>();
			
		this.processDefinition = jbpmContext.getGraphSession().getProcessDefinition(processDefinition.getId());
		String prefix = "ProcessDefinition." + processDefinition.getName() + "." + processDefinition.getVersion();
		messages.put("name", prefix + ".name");

		InputStream pd = processDefinition.getFileDefinition().getInputStream("processdefinition.xml");
		SAXReader pdReader = new SAXReader();
		for(Element pdElement : (List<Element>)pdReader.read(pd).getRootElement().elements()){
			if(NodeTypes.getNodeTypes().contains(pdElement.getName())){
				messages.put("nodes." + pdElement.attributeValue("name") + ".name", prefix + ".nodes." + pdElement.attributeValue("name") + ".name");
				for(Element trElement : (List<Element>) pdElement.elements("transition")){
					if(trElement.attributeValue("name") != null){
						messages.put("nodes." + pdElement.attributeValue("name") +  ".transitions." + trElement.attributeValue("name") + ".name", prefix + ".nodes." + pdElement.attributeValue("name") + ".transitions." + trElement.attributeValue("name") + ".name");
					}
				}
				if(pdElement.getName().equals("task-node")){
					for(Element taskElement : (List<Element>) pdElement.elements("task")){
						messages.put("tasks." + taskElement.attributeValue("name") + ".name", prefix + ".tasks." + taskElement.attributeValue("name") + ".name");
					}
				}			
			}
			else if(pdElement.getName().equals("task")) {
				messages.put("tasks." + pdElement.attributeValue("name") + ".name", prefix + ".tasks." + pdElement.attributeValue("name") + ".name");
			}
			else if(pdElement.getName().equals("properties")){
				for(Element propertiesEntryElement : (List<Element>) pdElement.elements("entry")){
					messages.put("properties." + pdElement.attributeValue("name") + "." + propertiesEntryElement.attributeValue("name"), prefix + ".properties." + pdElement.attributeValue("name") + "." + propertiesEntryElement.attributeValue("name"));
				}
			}
			//else if(pdElement.getName().equals("swimlane")) {
			//	messages.put("swimlanes." + pdElement.attributeValue("name") + ".name", resourceBundle.getMessages().get(prefix + ".swimlanes." + pdElement.attributeValue("name") + ".name"));
			//}
		}
		pd.close();
		
		String fileName = "messages_" + resourceBundle.getLanguage() + (resourceBundle.getCountry() != null && resourceBundle.getCountry().length() > 0 ? "_"+resourceBundle.getCountry() : "") + (resourceBundle.getVariant() != null && resourceBundle.getVariant().length() > 0 ? "_"+resourceBundle.getVariant() : "") + ".properties";
		if(processDefinition.getFileDefinition().hasFile(fileName)){
			try {
				Properties properties = new Properties();
				properties.load(processDefinition.getFileDefinition().getInputStream(fileName));
				for (Entry<Object, Object> propertiesEntry : properties.entrySet()) {
					messages.put((String)propertiesEntry.getKey(), prefix + "." + (String)propertiesEntry.getKey());
				}
			}
			catch(JbpmException e){
				log.info("Couldn't get #0", fileName);
			}
		}
		
		List<Map.Entry<String, String>> messagesList = new ArrayList<Map.Entry<String, String>>();
		messagesList.addAll(messages.entrySet());
		
		return messagesList;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public byte[] getMessagesFile() throws DocumentException, IOException{
		return this.getMessagesFile(this.resourceBundle);
	}
	
	/**
	 * Gets messages_{LANGUAGE}_{COUNTRY}_{VARIANT}.properties bytes.
	 * 
	 * @param resourceBundle
	 * 
	 * @return bytes[]
	 * 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] getMessagesFile(ResourceBundle resourceBundle) throws DocumentException, IOException{
		Properties properties = new Properties() {
			
			@Override
			public Enumeration keys() {
				Enumeration keysEnum = super.keys();
				Vector<String> keyList = new Vector<String>();
				while (keysEnum.hasMoreElements()) {
					keyList.add((String) keysEnum.nextElement());
				}
				Collections.sort(keyList);
				return keyList.elements();
			}
		};
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		List<Map.Entry<String, String>> entries = this.getMessages(resourceBundle);
		for(Map.Entry<String, String> entry : entries){
			String value = resourceBundle.getMessages().get(entry.getValue());
			properties.setProperty(entry.getKey(), value != null ? value : "");
		}

		properties.store(out, "ProcessDefinition: " + processDefinition.getName());
		
		out.close();
		
		return out.toByteArray();
	}
	
	/**
	 * Extract locale from ResourceBundle
	 * 
	 * @return java.util.Locale
	 */
	public Locale getLocale(){
		Locale locale = null;
		if(this.resourceBundle.getVariant() != null && !this.resourceBundle.getVariant().isEmpty()){
			locale = new Locale(this.resourceBundle.getLanguage(), this.resourceBundle.getCountry(), this.resourceBundle.getVariant());			
		}
		else if(this.resourceBundle.getCountry() != null && !this.resourceBundle.getCountry().isEmpty()){
			locale = new Locale(this.resourceBundle.getLanguage(), this.resourceBundle.getCountry());			
		}
		else {
			locale = new Locale(this.resourceBundle.getLanguage());			
		}
		return locale;
	}
	
	/**
	 * Used with <rich:fileUpload/> to upload process definition files
	 * 
	 * @param uploadEvent
	 * @throws IOException
	 */
	public void upload(UploadEvent event)throws IOException{
		this.processDefinition = jbpmContext.getGraphSession().getProcessDefinition(processDefinition.getId());
		
		if(event.isMultiUpload()){
			for(UploadItem item : event.getUploadItems()){
				FileInputStream in = new FileInputStream(item.getFile());
				byte[] data = new byte[item.getFileSize()];
				in.read(data);
		
				this.processDefinition.getFileDefinition().addFile(item.getFileName(), data);
			}
		}
		else {
			UploadItem item = event.getUploadItem();
			FileInputStream in = new FileInputStream(item.getFile());
			byte[] data = new byte[item.getFileSize()];
			in.read(data);
	
			this.processDefinition.getFileDefinition().addFile(item.getFileName(), data);
		}
	}
	
	/**
	 * Gets complete process definition .par (process archive) file with all files
	 * 
	 * Additionally gets messages_*.properties from kz.bee.wx.bpm.ProcessDefinitions.getMessagesFile(ResourceBundle)
	 * 
	 * @return process archive (.par) byte array
	 * @throws IOException
	 * @throws DocumentException 
	 * 
	 * @see kz.bee.wx.bpm.ProcessDefinitions.getMessagesFile(ResourceBundle)
	 */
	public byte[] getPar() throws IOException, DocumentException {
		this.processDefinition = jbpmContext.getGraphSession().getProcessDefinition(processDefinition.getId());
		ByteArrayOutputStream par = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(par);
		
		Set<Map.Entry<String, byte[]>> files = this.processDefinition.getFileDefinition().getBytesMap().entrySet();
		for(Map.Entry<String, byte[]> file : files){
			ZipEntry entry = new ZipEntry(file.getKey());
			out.putNextEntry(entry);
			try {
				if(this.getFileType(file.getKey()).equals("messages-properties")){
					ResourceBundle rb = this.getResourceBundle(file.getKey());
					if(rb != null){
						byte[] b = this.getMessagesFile(rb);
						out.write(b);
					}
					else {
						out.write(file.getValue());
					}
				}
				else {
					out.write(file.getValue());
				}
			} catch (NullPointerException e) {
				// skip empty files - leave them empty
				e.printStackTrace();
			}
		}
		out.close();

		par.close();
		
		return par.toByteArray();
	}
}