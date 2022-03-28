package stepDefinitions;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

public class Jira_steps extends Utils {
	int getID;
	int countComments;
	String getCommentId;
	String expectedMessage;
	String res;
	ResponseSpecification resspec;
	Response response;
	JsonPath js;
	TestDataBuild data = new TestDataBuild();
	SessionFilter session = new SessionFilter();

	@Given("User logged into Jira and hit the Post httpMethod and get name and value from response")
	public void user_logged_into_jira_and_hit_the_post_httpmethod_and_get_name_and_value_from_response()
			throws IOException {
		// Write code here that turns the phrase above into concrete actions
		RestAssured.baseURI = "http://localhost:8080/";
		String authorize = given().header("Content-Type", "application/json").body(data.logInData()).log().all()
				.filter(session).when().post(getGlobalValue("authorizeAPI")).then().log().all().extract().response()
				.asString();

		JsonPath js = rawToJson(authorize);
		String name = js.getString("session.name");
		System.out.println(name);
		String value = js.get("session.value");
		System.out.println(value);
		String cookie = "" + name + "=" + value + "";
		System.out.println(cookie);

	}

	@When("User create issue with Post httpMethod and get issueId")
	public void user_create_issue_with_post_httpmethod_and_get_issueid() throws IOException {
		// Write code here that turns the phrase above into concrete actions

		String issue = given().header("Content-Type", "application/json").header("Cookie", "cookie")
				.body(data.createissueJson()).when().log().all().filter(session).post(getGlobalValue("createIssueAPI"))
				.then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js1 = rawToJson(issue);
		getID = js1.getInt("id");
		System.out.println(getID);

	}

	@When("User add comment to the issue")
	public void user_add_comment_to_the_issue() throws IOException {
		// Write code here that turns the phrase above into concrete actions

		String comment = given().pathParam("i_d", getID).log().all().header("Content-Type", "application/json")
				.body(data.commentJson()).filter(session).when().post(getGlobalValue("addCommentAPI")).then().log()
				.all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js = rawToJson(comment);
		getCommentId = js.get("id");
		System.out.println(getCommentId);
	}

	@Then("User attach file to the issue")
	public void user_attach_file_to_the_issue() throws IOException {
		// Write code here that turns the phrase above into concrete actions
		given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("i_d", getID)
				.header("Content-Type", "multipart/form-data").multiPart("file", new File("jira.txt")).when()
				.post(getGlobalValue("attachFileAPI")).then().log().all().assertThat().statusCode(200);
	}

//get issue and verifying comment
	@Then("Verify the comment")
	public void verify_the_comment() throws IOException {
		// Write code here that turns the phrase above into concrete actions
		String getIssue = given().filter(session).pathParam("i_d", getID).log().all().get(getGlobalValue("getIssueAPI"))
				.then().log().all().extract().response().asString();
		JsonPath js = rawToJson(getIssue);
//verify the comment
		// this is for counting the no of comments
		countComments = js.getInt("fields.comment.comments.size()");
		for (int i = 0; i < countComments; i++) {
			String commentIdCount = js.get("fields.comment.comments[" + i + "]").toString();
			if (commentIdCount.equalsIgnoreCase(getCommentId)) {
				String message = js.get("fields.comment.comments[" + i + "].body");
				System.out.println(message);
				Assert.assertEquals(expectedMessage, message);
			}
		}

	}

	@Then("^User update the comment")
	public void user_update_the_comment() throws Throwable {
		String updateComment = given().filter(session).pathParam("i_d", getID).pathParam("comment_id", getCommentId)
				.header("Content-Type", "application/json").body(data.updateCommentJson()).filter(session).when().log()
				.all().put("/rest/api/2/issue/{i_d}/comment/{comment_id}").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js = rawToJson(updateComment);
		String dateCreated = js.get("created");
		String dateUpdated = js.get("updated");
		System.out.println(dateCreated);
		System.out.println(dateUpdated);
		
		//	
		System.out.println(dateCreated);
		System.out.println(dateUpdated);
		//added
		@Then("^User update the comment")
		public void user_update_the_qcomment() throws Throwable {
			String updateComment = given().filter(session).pathParam("i_d", getID).pathParam("comment_id", getCommentId)
					.header("Content-Type", "application/json").body(data.updateCommentJson()).filter(session).when().log()
					.all().put("/rest/api/2/issue/{i_d}/comment/{comment_id}").then().log().all().assertThat()
					.statusCode(200).extract().response().asString();
	}

}
