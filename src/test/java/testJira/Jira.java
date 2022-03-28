package testJira;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class Jira {
	public static void main(String[] args) {
		SessionFilter session = new SessionFilter();
		// {}-->path parameter
		RestAssured.baseURI = "http://localhost:8080/";
		// login scenarion and extract sessionid
		String response = given().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\":\"Aman.1.Kumar\",\"password\":\"Singhal0123@\"\r\n" + "}\r\n"
						+ " \r\n" + "")
				.log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract()
				.asString();

		JsonPath js = new JsonPath(response);
		String name = js.getString("session.name");
		System.out.println(name);
		String value = js.get("session.value");
		System.out.println(value);
		String cookie = "" + name + "=" + value + "";
		System.out.println(cookie);

		// create issue
		String issue = given().header("Content-Type", "application/json").header("Cookie", "cookie")
				.body("{\r\n" + "    \"fields\": {\r\n" + "        \"project\": {\r\n"
						+ "            \"key\": \"DEM\"\r\n" + "        },\r\n" + "        \"summary\": \"Raised\",\r\n"
						+ "        \"description\":\"Creating defect\",\r\n" + "        \"issuetype\": {\r\n"
						+ "            \"name\": \"Bug\"\r\n" + "        }\r\n" + "    }\r\n" + "}")
				.when().log().all().filter(session).post("rest/api/2/issue").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();
		JsonPath js1 = new JsonPath(issue);
		String getID = js1.get("id");
		System.out.println(getID);

		// Add Comment
		String expectedMessage = "This is the expected comment";
		String comment = given().pathParam("i_d", "10112").log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"" + expectedMessage + "\",\r\n" + "    \"visiblity\": {\r\n"
						+ "        \"type\":\"role\",\r\n" + "        \"value\":\"Senior Tester\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{i_d}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();
		JsonPath js2 = new JsonPath(comment);
		String getCommentId = js2.get("id");
		System.out.println(getCommentId);

		// Add Attachment
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("i_d", "10112")
				.header("Content-Type", "multipart/form-data").multiPart("file", new File("jira.txt")).when()
				.post("/rest/api/2/issue/{i_d}/attachments").then().log().all().assertThat().statusCode(200);

		// get the issue
		// get Issue: Query Parameter-->fiels,expand,properties

		String getIssue = given().filter(session).pathParam("i_d", "10112").log().all().get("/rest/api/2/issue/{i_d}")
				.then().log().all().extract().response().asString();
		JsonPath js3 = new JsonPath(getIssue);
//verify the comment
		// this is for counting the no of comments
		int countComments = js3.getInt("fields.comment.comments.size()");
		for (int i = 0; i < countComments; i++) {
			String commentIdCount = js3.get("fields.comment.comments[" + i + "]").toString();
			if (commentIdCount.equalsIgnoreCase(getCommentId)) {
				String message = js3.get("fields.comment.comments[" + i + "].body");
				System.out.println(message);
				Assert.assertEquals(expectedMessage, message);
	
			}

		}


	}
}
