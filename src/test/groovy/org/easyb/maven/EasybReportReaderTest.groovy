package org.easyb.maven

import static org.junit.Assert.*
import org.junit.Test

class EasybReportReaderTest {
    @Test
    public void shouldProvideStories() {
        EasybReportReader reader = new EasybReportReader(getClass().getResourceAsStream('/report.xml'))

        Story story = reader.stories[0]
        assertEquals('empty stack', story.name)

        Scenario scenario = story.scenarios[0]
        assertEquals('success', scenario.result)
        assertEquals('null is pushed onto empty stack', scenario.name)
        assertEquals(['an empty stack'], scenario.givens)
        assertEquals(['null is pushed'], scenario.whens)
        assertEquals(['an exception should be thrown', 'the stack should still be empty'], scenario.thens)

        scenario = story.scenarios[1]
        assertEquals('pending', scenario.result)
        assertEquals('pop is called on empty stack', scenario.name)
        assertEquals(['an empty stack'], scenario.givens)
        assertEquals(['pop is called'], scenario.whens)
        assertEquals(['an exception should be thrown', 'the stack should still be empty'], scenario.thens)

        story = reader.stories[1]
        assertEquals('zip code', story.name)

        scenario = story.scenarios[0]
        assertEquals('failure', scenario.result)
        assertEquals('invalid zip code', scenario.name)
        assertEquals(['an invalid zip code', 'the zipcodevalidator is initialized'], scenario.givens)
        assertEquals(['validate is invoked with the invalid zip code'], scenario.whens)
        assertEquals(['the validator instance should return false'], scenario.thens)
    }
}