report = new File('src/it/report-location-test/target/easyb-report.xml')
assert report.exists()

def results = new XmlParser().parse(report)
assert '5' == results.'@totalrun'
assert '0' == results.'@totalfailed'