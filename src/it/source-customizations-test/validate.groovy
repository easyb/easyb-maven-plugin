// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert behaviorReport.exists()

def results = new XmlParser().parse(behaviorReport)
assert '2' == results.'@totalbehaviors'
assert '0' == results.'@totalfailedbehaviors'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")

def reportText = storyReport.text
assert reportText.contains('scenario some cool feature') 
assert reportText.contains('given some context') 
assert reportText.contains('when some event occurs') 
assert reportText.contains('then some outcome is triggered')