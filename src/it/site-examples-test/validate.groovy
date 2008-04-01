// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert behaviorReport.exists()

def results = new XmlParser().parse(behaviorReport)
assert '5' == results.'@totalbehaviors'
assert '0' == results.'@totalfailedbehaviors'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")
assert storyReport.exists()