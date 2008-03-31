scenario "null is pushed onto empty stack", {
    given "an empty stack", {}
    when "null is pushed", {}
    then "an exception should be thrown", {}
    and
    then "the stack should still be empty", {}
}

scenario "pop is called on empty stack", {
    given "an empty stack"
    when "pop is called"
    then "an exception should be thrown"
    and
    then "the stack should still be empty"
}
