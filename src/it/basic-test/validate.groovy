report = new File('src/it/basic-test/target/easyb/report.xml')
assert report.exists()

def results = new XmlParser().parse(report)
assert '8' == results.'@totalrun'
assert '1' == results.'@totalfailed'