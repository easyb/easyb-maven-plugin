storyReportPath = new File("${basedir}/target/easyb")
assert storyReportPath.exists()

storyReport = new File(storyReportPath , 'stories.html')
assert storyReport.text.contains("<html")