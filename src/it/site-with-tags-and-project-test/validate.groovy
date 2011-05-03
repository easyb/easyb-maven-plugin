println "Verifying easyb HTML report"
// Verify HTML report
storyReport = new File("${basedir}/target/easyb/easyb.html")
storyReportContents = storyReport.text
assert storyReportContents.contains("<a href='http://my.issuetracker.com/jira/browse/MYPROJECT-123'>123</a>")
assert storyReportContents.contains("<a href='http://my.issuetracker.com/jira/browse/MYPROJECT-456'>MYPROJECT-456</a>")
