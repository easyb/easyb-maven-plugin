package org.easyb.maven

class EasybReportReader {
    List<Story> stories

    EasybReportReader(InputStream reportStream) {
        Node parser = new XmlParser().parse(reportStream)
        stories = parser.stories.story.collect {story ->
            def narrative = null
            if (story.narrative.size() > 0) {
                narrative = new Narrative(as_a: story.narrative.as_a[0].@name, i_want: story.narrative.i_want[0].@name,
                        so_that: story.narrative.so_that[0].@name)
            }
            def scenarios = story.scenario.collect {scenario ->
                new Scenario(name: scenario.@name, result: scenario.@result,
                        givens: parse(scenario.given), whens: parse(scenario.when), thens: parse(scenario.then))
            }
            return new Story(name: story.@name, narrative: narrative, scenarios: scenarios)
        }
    }

    List parse(List elements) {
        return elements.collect {it.@name}
    }

    List<Story> getStories() {
        return stories
    }
}