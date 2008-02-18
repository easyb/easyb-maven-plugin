// Verify behavior report
behaviorReport = new File("${basedir}/target/easyb/report.xml")
assert !behaviorReport.exists()
