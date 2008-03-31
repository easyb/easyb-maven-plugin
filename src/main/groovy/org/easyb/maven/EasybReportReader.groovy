package org.easyb.maven

class EasybReportReader {
    List<Story> stories

    EasybReportReader(InputStream reportStream) {
        Node parser = new XmlParser().parse(reportStream)
        stories = parser.stories.story.collect {story ->
            def scenarios = story.scenario.collect {scenario ->
                new Scenario(name: scenario.@name, result: scenario.@result,
                        givens: parse(scenario.given), whens: parse(scenario.when), thens: parse(scenario.then))
            }
            return new Story(name: story.@name, scenarios: scenarios)
        }
    }

    List parse(List elements) {
        return elements.collect {it.@name}
    }

    List<Story> getStories() {
        return stories
    }
}