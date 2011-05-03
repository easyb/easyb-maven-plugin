tags "#123"

scenario "two numbers can be added together", {
  given "a number", {
    a = 1
  }

  when "I add another number", {
    b = 2
    c = a + b
  }

  then "the result is the sum of the two numbers", {
    c.shouldBe 3
  }
}

scenario "two numbers can be substracted", {
  given "a number", {
    a = 6
  }

  when "I subtract another number", {
    b = 2
    c = a - b
  }

  then "the result is the sum of the two numbers", {
    c.shouldBe 4
  }
}
