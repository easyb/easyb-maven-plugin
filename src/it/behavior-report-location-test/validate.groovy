// Verify behavior report
report = new File("${basedir}/target/easyb-report.xml")
assert report.exists()

def results = new XmlParser().parse(report)
assert '5' == results.'@totalrun'
assert '0' == results.'@totalfailed'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")
assert storyReport.text ==
'''5 behavior steps executed successfully
  Story: simple
    scenario some cool feature
      given some context
      when some event occurs
      then some outcome is triggered
'''