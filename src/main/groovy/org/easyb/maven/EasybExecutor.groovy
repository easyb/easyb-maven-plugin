package org.easyb.maven

import org.codehaus.plexus.util.FileUtils

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

        new AntBuilder().java(classname: 'org.disco.easyb.BehaviorRunner', fork: true) {
            classpath() {
                mojo.project.getTestClasspathElements().each {element ->
                    pathelement(location: element)
                }
            }
            includedSpecs().each {File spec ->
                arg(value: spec.getAbsolutePath())
            }
            arg(value: '-xmleasyb')
            arg(value: mojo.xmlReport)
            arg(value: '-txtstory')
            arg(value: mojo.storyReport)
        }

        def totalfailed = new XmlParser().parse(mojo.xmlReport).'@totalfailedbehaviors'
        if ('0' != totalfailed)
            fail("${totalfailed} behaviors failed")
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
        mojo.includes.each {include ->
            includedFiles += FileUtils.getFiles(mojo.easybTestDirectory, include, '')
        }
        return includedFiles
    }
}