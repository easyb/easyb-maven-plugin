package org.easyb.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.disco.easyb.util.CamelCaseConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Generate a html report for stories defined with easyb
 *
 * @goal storyReport
 * @phase test
 */
@SuppressWarnings("UnusedDeclaration")
public class StoryReportMojo extends AbstractMojo
{
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @SuppressWarnings("UnusedDeclaration")
     */
    private MavenProject project;

    /**
     * Directory to create html stories in
     *
     * @parameter expression="${project.build.directory}/easyb-stories"
     * @required
     */
    private File outputDirectory;

    /**
     * Path to the easyb report created by the test goal
     *
     * @parameter expression="${project.build.directory}/easyb/report.xml"
     * @required
     */
    private File xmlReport;

    @SuppressWarnings("RedundantCast")
    public void execute() throws MojoExecutionException
    {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        FileInputStream reportStream = null;
        StoryReportWriter writer = new StoryReportWriter();
        try {
            reportStream = new FileInputStream(xmlReport);
            EasybReportReader reader = new EasybReportReader(reportStream);
            // This redundant cast is required due to some weirdness with the joint compiler
            for (Story story : (List<Story>) reader.getStories()) {
                try {
                    CamelCaseConverter converter = new CamelCaseConverter(story.getName());
                    File report = new File(outputDirectory, converter.toCamelCase() + "Story.html");
                    report.createNewFile();
                    writer.write(story, new FileOutputStream(report));
                } catch (IOException e) {
                    throw new MojoExecutionException("Unable to create story file", e);
                }
            }
        }
        catch (FileNotFoundException e) {
            throw new MojoExecutionException("Unable to read easyb report", e);
        }
        finally {
            if (reportStream != null) {
                try {
                    reportStream.close();
                } catch (IOException e) {
                    getLog().error("Unable to close easyb report", e);
                }
            }
        }
    }
}