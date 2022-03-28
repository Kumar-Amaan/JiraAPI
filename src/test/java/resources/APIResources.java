package resources;

public enum APIResources {

//enum is a special class in java which has collection of constants or methods
	createIssueAPI("rest/api/2/issue"), addCommentAPI("/rest/api/2/issue/{i_d}/comment"),authorizeAPI("/rest/auth/1/session"),
	attachFileAPI("/rest/api/2/issue/{i_d}/attachments");

	private String resource;

	
	//constructor
	APIResources(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
}
