package org.easyb.maven

class Story {
    String name
    Narrative narrative
    List scenarios = []

    String getName() {
        return name
    }
}