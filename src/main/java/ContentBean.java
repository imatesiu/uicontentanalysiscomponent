

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;

import eu.learnpad.ca.rest.data.QualityCriteria;
import eu.learnpad.ca.rest.data.collaborative.CollaborativeContent;
import eu.learnpad.ca.rest.data.collaborative.CollaborativeContentAnalysis;




@ManagedBean(name="ContentBean")
@RequestScoped
public class ContentBean implements Serializable{

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ContentBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2574895928017048791L;
	private String Title;
	private int id;
	private String Content;
	private String ContentHTML;
	private String Language;
	private String restid;
	private String typedoc;
	private String filecontent;
	private String filecontenthtml;
	private String filename;
	private boolean Correctness;
	private boolean Simplicity;
	private boolean ContentClarity;
	private boolean NonAmbiguity;
	private boolean Completeness;
	private boolean PresentationClarity;
	
	
	
	
	public ContentBean(){
		Correctness = true;
		Simplicity= true;
		ContentClarity= true;
		NonAmbiguity= true;
		Completeness= false;
		PresentationClarity= false;
	}

	

	public String getTypedoc() {
		return typedoc;
	}



	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}



	public boolean isCorrectness() {
		return Correctness;
	}

	

	public String getContentHTML() {
		return ContentHTML;
	}



	public void setContentHTML(String contentHTML) {
		ContentHTML = contentHTML;
	}



	public void setCorrectness(boolean correctness) {
		Correctness = correctness;
	}



	public boolean isSimplicity() {
		return Simplicity;
	}



	public void setSimplicity(boolean simplicity) {
		Simplicity = simplicity;
	}



	public boolean isContentClarity() {
		return ContentClarity;
	}



	public void setContentClarity(boolean contentClarity) {
		ContentClarity = contentClarity;
	}



	public boolean isNonAmbiguity() {
		return NonAmbiguity;
	}



	public void setNonAmbiguity(boolean nonAmbiguity) {
		NonAmbiguity = nonAmbiguity;
	}



	public boolean isCompleteness() {
		return Completeness;
	}



	public void setCompleteness(boolean completeness) {
		Completeness = completeness;
	}



	public boolean isPresentationClarity() {
		return PresentationClarity;
	}



	public void setPresentationClarity(boolean presentationClarity) {
		PresentationClarity = presentationClarity;
	}



	public String getFilecontent() {
		return filecontent;
	}


	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}


	public String getRestid() {
		return restid;
	}



	public void setRestid(String restid) {
		this.restid = restid;
	}



	public String getLanguage() {
		return Language;
	}



	public void setLanguage(String language) {
		Language = language;
	}



	public String getTitle() {
		return Title;
	}



	public void setTitle(String title) {
		Title = title;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getContent() {
		return Content;
	}



	public void setContent(String content) {
		Content = content;
	}

	public void sampleListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry)e.getComponent();
		FileEntryResults results = fe.getResults();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			log.info(fileInfo);


			if (fileInfo.isSaved()) {
				// Process the file. Only save cloned copies of results or fileInfo,StandardCharsets.UTF_8

				Path path = Paths.get(fileInfo.getFile().getAbsolutePath());
				try {
					filecontent = 	new String(Files.readAllBytes(path), "UTF8");
					filename = fileInfo.getFileName();
					log.info(filecontent);
				} catch (IOException e1) {
					
					log.error(e1);
					log.error(e1.getMessage());
				}
			}
		}

	}
	
	public void ListenerFileHTML(FileEntryEvent e) {
		FileEntry fe = (FileEntry)e.getComponent();
		FileEntryResults results = fe.getResults();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			log.info(fileInfo);


			if (fileInfo.isSaved()) {
				// Process the file. Only save cloned copies of results or fileInfo,StandardCharsets.UTF_8

				Path path = Paths.get(fileInfo.getFile().getAbsolutePath());
				try {
					filecontenthtml = 	new String(Files.readAllBytes(path), "UTF8");
					
					log.info(fileInfo.getFileName());
				} catch (IOException e1) {
					log.error(e1);
					log.error(e1.getMessage());
				}
			}
		}

	}

	public void submitButton(ActionEvent event) {


		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080").path("lp-content-analysis/learnpad/ca/bridge/validatecollaborativecontent");

		CollaborativeContentAnalysis cca = new CollaborativeContentAnalysis();
		cca.setLanguage(this.getLanguage());
		
		if(filecontent!=null){
			cca.setCollaborativeContent(new CollaborativeContent(String.valueOf(this.getId()), this.filename));
			//cca.getCollaborativeContent().setContent(new eu.learnpad.ca.rest.data.Content());
			cca.getCollaborativeContent().setContentplain(filecontent);
		}else{
			cca.setCollaborativeContent(new CollaborativeContent(String.valueOf(this.getId()), this.getTitle()));
			//cca.getCollaborativeContent().setContent(new eu.learnpad.ca.rest.data.Content());
			cca.getCollaborativeContent().setContentplain(getContent());
		}
		if(filecontenthtml!=null){
			cca.getCollaborativeContent().setContenthtml(filecontenthtml);
		}else{
			cca.getCollaborativeContent().setContenthtml(getContentHTML());
		}
		
		cca.setQualityCriteria(new QualityCriteria());
		cca.getQualityCriteria().setCorrectness(this.isCorrectness());
		cca.getQualityCriteria().setSimplicity(this.isSimplicity());
		cca.getQualityCriteria().setContentClarity(this.isContentClarity());
		cca.getQualityCriteria().setNonAmbiguity(this.isNonAmbiguity());
		cca.getQualityCriteria().setCompleteness(this.isCompleteness());
		cca.getQualityCriteria().setPresentationClarity(this.isPresentationClarity());

		Entity<CollaborativeContentAnalysis> entity = Entity.entity(cca,MediaType.APPLICATION_XML);
		//GenericEntity<JAXBElement<CollaborativeContentAnalysis>> gw = new GenericEntity<JAXBElement<CollaborativeContentAnalysis>>(cca){};
		Response response =  target.request(MediaType.APPLICATION_XML).post(entity);

		String id = response.readEntity(String.class);

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getRequestMap().put("rest", id);

		//context.getApplication().evaluateExpressionGet(context, "#{ContentAnalysisBean.setId("+id+")}", String.class);

		this.setRestid(id);

		log.info("Submit Clicked: " + Title + ", " + Content + ", " + id + "; ");
	}
}
