// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert behaviorReport.exists()

def results = new XmlParser().parse(behaviorReport)
assert '10' == results.'@totalspecifications'
assert '0' == results.'@totalfailedspecifications'

// Verify story printing
storyReport = new File("${basedir}/target/easyb/stories.txt")
assert storyReport.text ==
        '''8 specifications (including 0 pending) executed successfully


  Story: empty stack

    scenario null is pushed onto empty stack
      given an empty stack
      when null is pushed
      then an exception should be thrown
      then the stack should still be empty

    scenario pop is called on empty stack
      given an empty stack
      when pop is called
      then an exception should be thrown
      then the stack should still be empty

  Story: single value stack

    scenario pop is called
      given a stack containing an item
      when peek is called
      then it should provide the value of the most recent pushed value
      then the stack should not be empty
      then calling pop should also return the peeked value
      then the stack should be empty
'''