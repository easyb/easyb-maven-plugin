report = new File('src/it/stack-stories-test/target/easyb/report.xml')
assert report.exists()

def results = new XmlParser().parse(report)
assert '19' == results.'@totalrun'
assert '0' == results.'@totalfailed'