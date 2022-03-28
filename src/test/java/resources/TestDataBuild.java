package resources;

import java.io.IOException;

public class TestDataBuild extends Utils{
	public String logInData() throws IOException {
		String uName=getGlobalValue("username");
		String pass=getGlobalValue("password");
		return "{\r\n" + "    \"username\":\""+uName+"\",\"password\":\""+pass+"\"\r\n" + "}";
	}
	
	public String createissueJson() {
		return "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \"DEM\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"Raised Defect\",\r\n"
				+ "        \"description\":\"Creating a defect\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "    }\r\n"
				+ "}";
	}
	
	public String commentJson() {
		String expectedMessage = "This is the expected comment";
return "{\r\n"
		+ "    \"body\": \""+expectedMessage+"\",\r\n"
		+ "    \"visiblity\": {\r\n"
		+ "        \"type\":\"role\",\r\n"
		+ "        \"value\":\"Senior\"\r\n"
		+ "    }\r\n"
		+ "}";
	}
	
	public String updateCommentJson() {
		String updateComment="Comment is updated";
		return "{\r\n"
				+ "    \"body\": \""+updateComment+"\",\r\n"
				+ "    \"visiblity\": {\r\n"
				+ "        \"type\":\"role\",\r\n"
				+ "        \"value\":\"Tester\"\r\n"
				+ "    }\r\n"
				+ "}";
	}
	
}
