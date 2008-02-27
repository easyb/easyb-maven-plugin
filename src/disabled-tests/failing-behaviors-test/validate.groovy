// Verify build didn't produce any artifacts (since it should have failed)
File targetDir = new File("${basedir}/target/")
targetDir.eachFileMatch(~/.*jar/) {
  assert false
}

def results = new XmlParser().parse("${basedir}/target/easyb/report.xml")
assert '1' == results.'@totalrun'
assert '1' == results.'@totalfailed'