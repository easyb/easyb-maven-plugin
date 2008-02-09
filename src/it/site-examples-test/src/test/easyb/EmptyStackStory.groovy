import org.disco.easyb.bdd.Stack

scenario "null is pushed onto empty stack", {
  given "an empty stack", {
    stack = new Stack()
  }

  when "null is pushed", {
    pushnull = {
      stack.push(null)
    }
  }

  then "an exception should be thrown", {
    ensureThrows(RuntimeException.class) {
      pushnull()
    }
  }

  and

  then "the stack should still be empty", {
    ensure(stack.empty) {
      isTrue
    }
  }
}

scenario "pop is called on empty stack", {
  given "an empty stack", {
    stack = new Stack()
  }

  when "pop is called", {
    popnull = {
      stack.pop()
    }
  }

  then "an exception should be thrown", {
    ensureThrows(RuntimeException.class) {
      popnull()
    }
  }

  and

  then "the stack should still be empty", {
    ensure(stack.empty) {
      isTrue
    }
  }
}