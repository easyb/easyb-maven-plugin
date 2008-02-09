package org.disco.easyb.maven

import org.apache.maven.project.MavenProject
import org.codehaus.mojo.groovy.GroovyMojo
import org.codehaus.plexus.util.FileUtils

/**
 * Execute story specifications defined with easyb
 *
 * @goal test
 */
public class EasybMojo extends GroovyMojo
{
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
    String behaviorReport

    /**
    * @parameter expression="${project.build.directory}/easyb/stories.txt"
    * @required
    */
    String storyReport

    /**
    * @parameter expression="${project.basedir}/src/test/story"
    * @required
    */
    File storyDirectory

    /**
    * Ant path-style expression of files to run as story tests.  Defaults to '**\/*Story.groovy'.
    * @parameter expression="${easyb.includes}" default-value="**\/*Story.groovy"
    */
    String includes

    void execute() {
        def files = FileUtils.getFiles(storyDirectory, includes, '')

        new File(behaviorReport).parentFile.mkdirs()
        ant.java(classname: 'org.disco.easyb.SpecificationRunner') {
            classpath() {
                project.getTestClasspathElements().each {element ->
                    pathelement(location: element)
                }
            }
            files.each {File story ->
                println story.getAbsolutePath()
                arg(value: story.getAbsolutePath())
            }
            arg(value: '-xmlbehavior')
            arg(value: behaviorReport)
            arg(value: '-txtstory')
            arg(value: storyReport)
        }
    }
}