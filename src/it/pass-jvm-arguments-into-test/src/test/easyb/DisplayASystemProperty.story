scenario "display a system property", {
  given "a system property is passed to easyb from the Maven plugin configuration"
  when "the story looks for the system property", {
      systemPropertyValue = System.getProperty("some.system.value")
  }
  then "the value should be there", {
      systemPropertyValue.shouldBe "someValue"
  }
}
