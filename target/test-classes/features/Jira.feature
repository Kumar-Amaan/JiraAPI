Feature: Jira Authorization

Scenario: Jira API 
Given User logged into Jira and hit the Post httpMethod and get name and value from response
When User create issue with Post httpMethod and get issueId
And User add comment to the issue
Then User attach file to the issue
Then Verify the comment
Then User update the comment