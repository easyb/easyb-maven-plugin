package org.easyb.maven;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import org.apache.maven.project.MavenProject;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

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
     * @component
     */
    ArtifactFactory artifactFactory;

    /**
     * @component
     */
    ArtifactResolver resolver;

    /**
     * @parameter expression="${localRepository}"
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     */
    private List remoteRepositories;

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

    List<String> easybDependencies() throws ArtifactResolutionException, ArtifactNotFoundException {
        List<String> dependencies = new ArrayList<String>();
        dependencies.add(pathForDependency("org.easyb", "easyb", "0.8"));
        dependencies.add(pathForDependency("commons-cli", "commons-cli", "1.1"));
        dependencies.add(pathForDependency("org.codehaus.groovy", "groovy-all-minimal", "1.5.0"));
        return dependencies;
    }

    private String pathForDependency(String groupId, String artifactId, String version)
            throws ArtifactResolutionException, ArtifactNotFoundException {
        Artifact artifact = artifactFactory.createArtifact(groupId, artifactId, version, "test", "jar");
        resolver.resolve(artifact, remoteRepositories, localRepository);
        return artifact.getFile().getAbsolutePath();
    }
}
