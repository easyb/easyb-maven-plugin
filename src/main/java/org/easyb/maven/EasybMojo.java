package org.easyb.maven;

import java.util.List;
import java.io.File;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Executes easyb behaviors
 *
 * @goal test
 * @phase test
 */
@SuppressWarnings("UnusedDeclaration")
public class EasybMojo extends AbstractMojo {
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * Full path to the file which should contain an XML representation of all behavior results
     *
     * @parameter expression="${project.build.directory}/easyb/report.xml"
     * @required
     */
    String xmlReport;

    /**
     * Full path to the file which should contain textual story descriptions
     *
     * @parameter expression="${project.build.directory}/easyb/stories.txt"
     * @required
     */
    String storyReport;

    /**
     * The directory to be scanned for easyb behaviors
     *
     * @parameter expression="${project.basedir}/src/test/easyb"
     * @required
     */
    File easybTestDirectory;

    /**
     * Ant path-style expression of files to run as story tests.
     * Defaults to <code>**\/*Story.groovy **\/*.story **\/*Specification.groovy **\/*.specification</code>.
     *
     * @parameter
     */
    List<String> includes;

    public void execute() throws MojoExecutionException, MojoFailureException {
        buildExecutor().execute();
    }

    private EasybExecutor buildExecutor() {
        return new EasybExecutor(this);
    }
}
