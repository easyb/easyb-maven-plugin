// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert behaviorReport.exists()

def results = new XmlParser().parse(behaviorReport)
assert '2' == results.'@totalspecifications'
assert '0' == results.'@totalfailedspecifications'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")
assert storyReport.text ==
        '''1 specifications (including 0 pending) executed successfully


  Story: simple stack

    scenario some cool feature
      given some context
      when some event occurs
      then some outcome is triggered
'''