package org.easyb.maven

import org.apache.maven.plugin.MojoFailureException
import org.codehaus.plexus.util.FileUtils
import java.util.regex.Pattern


public class EasybExecutor {
    EasybMojo mojo

    EasybExecutor(EasybMojo mojo) {
        this.mojo = mojo
    }

    void execute() {
        defaultParameters()

        if (!mojo.easybTestDirectory.exists()) {
            mojo.log.info("$mojo.easybTestDirectory does not exists.  Skipping easyb testing")
            return
        }
        if (includedSpecs().isEmpty()) {
            mojo.log.info("No specifications discovered in easyb test directory.  Skipping easyb testing")
            return
        }

        makeReportDirectories()

        new AntBuilder(new AntProject()).java(classname: 'org.easyb.BehaviorRunner', fork: true) {
            classpath() {
                mojo.project.getTestClasspathElements().each {element ->
                    pathelement(location: element)
                }
                mojo.easybDependencies().each {dependency ->
                    pathelement(location: dependency)
                }
            }
            includedSpecs().each {File spec ->
                arg(value: spec.getAbsolutePath())
            }
            if (mojo.parallel) {
                arg(value: '-parallel')
            }

            if (mojo.tags) {
                arg(value: '-tags')
                arg(value: mojo.tags)
            }
            arg(value: '-xml')
            arg(value: mojo.xmlReport)
            arg(value: "-$mojo.storyType")
            arg(value: mojo.storyReport)
            arg(value: '-junit')
            arg(value: mojo.junitReport)
            if (mojo.jvmArguments) {
                mojo.jvmArguments.split().each {argument ->
                    jvmarg(value: argument)
                }
            }
        }

        if (! FileUtils.fileExists(mojo.xmlReport)) 
            throw new MojoFailureException("Missing XML report file: " + mojo.xmlReport + ". Build issue ?")

        def totalfailed = new XmlParser().parse(mojo.xmlReport).'@totalfailedbehaviors'
        if ('0' != totalfailed)
            throw new MojoFailureException("${totalfailed} behaviors failed")
    }

    def String encode(def raw) {
        URLEncoder encoder = new URLEncoder()
        return encoder.encode(raw, "utf-8")
    }

    def defaultParameters() {
        if (mojo.includes == null)
            mojo.includes = ['**/*Story.groovy', '**/*.story', '**/*Specification.groovy', '**/*.specification']
    }

    def makeReportDirectories() {
        new File(mojo.xmlReport).parentFile.mkdirs()
        new File(mojo.storyReport).parentFile.mkdirs()
    }

    def includedSpecs() {
        def includedFiles = []
        def matcher = areTestsFiltered() ? includeMatchingTests() : includeAllTests()

        mojo.includes.each {include ->
            def foundFiles = FileUtils.getFiles(mojo.easybTestDirectory, include, '')
            includedFiles += foundFiles.findAll(matcher)
        }
        return includedFiles
    }

    private boolean areTestsFiltered() {
        return mojo.test != null
    }

    private Closure includeMatchingTests() {
        Pattern p = Pattern.compile(mojo.test)
        return { p.matcher(it.getAbsolutePath()).find() }
    }

    private Closure includeAllTests() {
        return { true }
    }
}
