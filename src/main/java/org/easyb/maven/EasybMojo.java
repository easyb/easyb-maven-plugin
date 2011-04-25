package org.easyb.maven;

import java.io.File;
import java.util.ArrayList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singleton;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Executes easyb behaviors
 *
 * @goal test
 * @phase test
 * @requiresDependencyResolution test
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
     * @component
     */
    ArtifactFactory artifactFactory;

    /**
     * @component
     */
    ArtifactResolver resolver;

    /**
     * @readonly
     * @parameter expression="${plugin.version}"
     */
    private String pluginVersion;

    /**
     * @parameter expression="${localRepository}"
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * @component
     */
    private ArtifactMetadataSource metadataSource;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     */
    private List remoteRepositories;

    /**
     * List of all artifacts for this plugin provided by Maven. This is used internally to get a handle on
     * the Easyb JAR artifact.
     *
     * <p>Note: This is passed by Maven and must not be configured by the user.</p>
     *
     * @parameter expression="${plugin.artifacts}"
     * @readonly
     * @required
     */
    private List pluginArtifacts;

    /**
     * Full path to the file which should contain an XML representation of all behavior results
     *
     * @parameter expression="${project.build.directory}/easyb/report.xml"
     * @required
     */
    String xmlReport;

    /**
     * Full path to the directory in which JUnit XML reports will be generated
     *
     * @parameter expression="${project.build.directory}/surefire-reports"
     * @required
     */
    String junitReport;

    /**
     * Optional parameter used as matcher expression to restrict the tests to run. Can match any part of the name or the path.
     *
     * @parameter expression="${easyb.test}"
     * @since 0.8.4
     */
    String test;

    /**
     * Optional parameter to run only stories matching tags in the supplied list.  A single or comma-delimited list of tag names.
     *
     * @parameter expression="${easyb.tags}"
     */
    String tags;

    /**
     * Type of story reports to be written as html
     *
     * @parameter expression="txtstory"
     */
    String storyType;

    /**
     * Full path to the file where the story report should be written
     *
     * @parameter expression="${project.build.directory}/easyb/stories.txt"
     * @required
     */
    String storyReport;

    /**
     * The base URL of the issue tracking system.
     * If specified, any tags starting with '#' will be considered to be an issue id,
     * and a corresponding link will be included in the HTML report
     *
     * @parameter expression="issueSystemBaseUrl"
     */
    String issueSystemBaseUrl;

    /**
     * The (optional) heading to go on the issue system tag column
     *
     * @parameter expression="issueSystemHeading"
     */
    String issueSystemHeading;

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

    /**
     * Run easyb specs in parallel
     * 
     * @parameter
     */
    boolean parallel;

    public void execute() throws MojoExecutionException, MojoFailureException {
        buildExecutor().execute();
    }

    private EasybExecutor buildExecutor() {
        return new EasybExecutor(this);
    }

    private Artifact findEasybArtifact(List pluginArtifacts) throws MojoExecutionException {
        Artifact easybArtifact = null;
        Iterator artifacts = pluginArtifacts.iterator();
        while (artifacts.hasNext() && easybArtifact == null) {
            Artifact artifact = (Artifact) artifacts.next();

            if ("org.easyb".equals(artifact.getGroupId())
                && "easyb-core".equals(artifact.getArtifactId())) {
                easybArtifact = artifact;
            }
        }

        if (easybArtifact == null) {
            throw new MojoExecutionException(
                "Couldn't find [org.easyb:easyb] artifact in plugin dependencies");
        }

        return easybArtifact;
    }

    List<String> easybDependencies() throws ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException {
        Artifact mojoArtifact = artifactFactory.createBuildArtifact("org.easyb", "maven-easyb-plugin", pluginVersion, "jar");

        Artifact easybArtifact = findEasybArtifact(this.pluginArtifacts);
        easybArtifact = artifactFactory.createArtifact(easybArtifact.getGroupId(), easybArtifact.getArtifactId(),
            easybArtifact.getVersion(), Artifact.SCOPE_COMPILE, easybArtifact.getType());

        ArtifactResolutionResult resolutionResult =
            resolver.resolveTransitively(singleton(easybArtifact), mojoArtifact, EMPTY_LIST, localRepository, metadataSource);

        getLog().debug("Using easyb " + easybArtifact);

        List<String> dependencies = new ArrayList<String>();

        for (Object each : resolutionResult.getArtifacts()) {
            Artifact artifact = (Artifact) each;
            getLog().info("Using easyb dependency " + artifact);
            dependencies.add(artifact.getFile().getAbsolutePath());
        }

        return dependencies;
    }
}
