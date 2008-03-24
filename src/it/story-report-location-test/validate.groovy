// Verify behavior report
report = new File("${basedir}/target/easyb/report.xml")
assert report.exists()

def results = new XmlParser().parse(report)
assert '1' == results.'@totalbehaviors'
assert '0' == results.'@totalfailedbehaviors'

// Verify story printing
storyReport = new File("${basedir}/target/easyb-stories.txt")
assert storyReport.text ==
        ''' 1 scenario (including 0 pending) executed successfully


  Story: simple

    scenario some cool feature
      given some context
      when some event occurs
      then some outcome is triggered
'''