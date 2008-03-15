package org.disco.easyb.maven

import org.apache.maven.project.MavenProject
import org.codehaus.mojo.groovy.GroovyMojo
import org.codehaus.plexus.util.FileUtils

/**
 * Execute story specifications defined with easyb
 *
 * @goal test
 * @phase test
 */
public class EasybMojo extends GroovyMojo {
  /**
  * @parameter expression="${project}"
  * @required
  * @readonly
  */
  MavenProject project

  /**
  * @parameter expression="${project.build.directory}/easyb/report.xml"
  * @required
  */
  String xmlReport

  /**
  * @parameter expression="${project.build.directory}/easyb/stories.txt"
  * @required
  */
  String storyReport

  /**
  * @parameter expression="${project.basedir}/src/test/easyb"
  * @required
  */
  File easybTestDirectory

  /**
  * Ant path-style expression of files to run as story tests.
  * Defaults to <code>**\/*Story.groovy **\/*Behavior.groovy</code>.
  * @parameter
  */
  List<String> includes

  void execute() {
    defaultParameters()

    if (!easybTestDirectory.exists()) {
      log.info("$easybTestDirectory does not exists.  Skipping easyb testing")
      return
    }
    if (includedSpecs().isEmpty()) {
      log.info("No specifications discovered in easyb test directory.  Skipping easyb testing")
      return
    }

    makeReportDirectories()

    ant.java(classname: 'org.disco.easyb.SpecificationRunner', fork: true) {
      classpath() {
        project.getTestClasspathElements().each {element ->
          pathelement(location: element)
        }
      }
      includedSpecs().each {File spec ->
        arg(value: spec.getAbsolutePath())
      }
      arg(value: '-xmleasyb')
      arg(value: xmlReport)
      arg(value: '-txtstory')
      arg(value: storyReport)
    }

    def totalfailed = new XmlParser().parse(xmlReport).'@totalfailedspecifications'
    if ('0' != totalfailed)
      fail("${totalfailed} specifications failed")
  }

  def defaultParameters() {
    if (includes == null)
      includes = ['**/*Story.groovy', '**/*Behavior.groovy']
  }

  def makeReportDirectories() {
    new File(xmlReport).parentFile.mkdirs()
    new File(storyReport).parentFile.mkdirs()
  }

  def includedSpecs() {
    def includedFiles = []
    includes.each {include ->
      includedFiles += FileUtils.getFiles(easybTestDirectory, include, '')
    }
    return includedFiles
  }
}