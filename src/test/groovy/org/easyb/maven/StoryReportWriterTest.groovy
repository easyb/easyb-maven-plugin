package org.easyb.maven

import org.junit.Test

class StoryReportWriterTest {
    @Test
    void shouldWriteStory() {
        Story story = new Story(name: 'test story')
        story.narrative = new Narrative(as_a: 'role', i_want: 'behavior', so_that: 'purpose')
        story.scenarios += new Scenario(givens: ['something'], whens: ['something occurs'], thens: ['something happened'])
        story.scenarios += new Scenario(givens: ['something else'], whens: ['something else occurs'], thens: ['something else happened'])

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new StoryReportWriter().write(story, outputStream)

        String output = outputStream.toString()
        assert output.contains('test story')
        assert output.contains('as a ' + story.narrative.as_a)
        assert output.contains('i want ' + story.narrative.i_want)
        assert output.contains('so that ' + story.narrative.so_that)
        story.scenarios.each {scenario ->
            scenario.givens.each {assert output.contains('given ' + it)}
            scenario.whens.each {assert output.contains('when ' + it)}
            scenario.thens.each {assert output.contains('then ' + it)}
        }
    }
}