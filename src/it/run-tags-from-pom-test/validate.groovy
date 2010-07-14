// Verify behavior report
report = new File("${basedir}/target/easyb-report.xml")
assert report.exists()

def results = new XmlParser().parse(report)
assert '2' == results.'@totalbehaviors'
assert '0' == results.'@totalfailedbehaviors'

def stories = ['1', '2']
def pattern = ~/Running long[12345] story/

// Verify ran in parallel
log = new File("${basedir}/build.log")
log.withReader { reader ->
    String line = reader.readLine()

    // allows us to read them if they complete out of order
    while ( line && !line.startsWith("ERR:") && stories.size() > 0 ) {
      def matcher = pattern.matcher(line)
      if ( matcher.find() ) {
        stories.remove( line.substring(matcher.start()+12, matcher.start()+13) )
      }
      line = reader.readLine()
    }

    assert stories.size() == 0
}
