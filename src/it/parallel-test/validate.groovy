// Verify behavior report
report = new File("${basedir}/target/easyb-report.xml")
assert report.exists()

def results = new XmlParser().parse(report)
assert '5' == results.'@totalbehaviors'
assert '0' == results.'@totalfailedbehaviors'

// Verify ran in parallel
log = new File("${basedir}/build.log")
log.withReader { reader ->
    String line = reader.readLine()
    while (!line.contains('Running long1 story')) {
        line = reader.readLine()
    }

    (2..5).each { n ->
        assert reader.readLine().contains("Running long$n story")
    }
}