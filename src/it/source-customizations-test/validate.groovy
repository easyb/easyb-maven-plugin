// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert behaviorReport.exists()

def results = new XmlParser().parse(behaviorReport)
assert '2' == results.'@totalspecifications'
assert '0' == results.'@totalfailedspecifications'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")
def storyElements = [
        'scenario some cool feature',
        'given some context',
        'when some event occurs',
        'then some outcome is triggered'
]
storyElements.each {assert storyReport.text.contains(it)}