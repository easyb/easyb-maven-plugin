storyReport = new File("${basedir}/target/easyb-stories")
assert storyReport.exists()

// Verify that the components of the empty stack story exists in the story report
emptyStackStory = new File(storyReport, "EmptyStackStory.html")
assert emptyStackStory.exists()

emptyStackElements = ['as a', 'i want', 'so that', 'empty stack', 'an empty stack', 'null is pushed',
        'an exception should be thrown', 'the stack should still be empty', 'an empty stack', 'pop is called']

emptyStackElements.each {element ->
    assert emptyStackStory.text.contains(element)
}

// Verify that the components of the zip code story exists in the story report

File zipCodeStory = new File(storyReport, "ZipCodeStory.html")
assert zipCodeStory.exists()

zipCodeElements = ['zip code', 'an invalid zip code', 'the zipcodevalidator is initialized',
        'validate is invoked with the invalid zip code', 'the validator instance should return false']

zipCodeElements.each {element ->
    assert zipCodeStory.text.contains(element)
}