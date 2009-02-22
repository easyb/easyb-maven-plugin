package org.easyb.maven

import groovy.xml.MarkupBuilder

class StoryReportWriter {
    void write(Story story, OutputStream output) {
        output.withWriter {Writer writer ->
            def report = new MarkupBuilder(writer)
            writeReport(report, story)
        }
    }

    private def writeReport(MarkupBuilder report, Story story) {
        report.html {
            head {
                style type: 'text/css', '''
                    .success {color: green}
                    .pending {color: gold}
                    .failure {color: red}'''
            }
            body {
                h1 'Story: ' + story.name
                if (story.narrative != null) {
                    h3 {
                        div {
                            div 'as a ' + story.narrative.as_a
                            div 'i want ' + story.narrative.i_want
                            div 'so that ' + story.narrative.so_that
                        }
                    }
                }
                story.scenarios.each {scenario ->
                    h3 'class': scenario.result, 'Scenario: ' + scenario.name
                    div {
                        scenario.givens.each {div 'given ' + it}
                        scenario.whens.each {div 'when ' + it}
                        scenario.thens.each {div 'then ' + it}
                    }
                }
            }
        }
    }
}