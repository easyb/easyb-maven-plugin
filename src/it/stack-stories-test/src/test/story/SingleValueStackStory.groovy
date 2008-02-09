import org.disco.easyb.bdd.Stack

scenario "pop is called", {
  given "a stack containing an item", {
    stack = new Stack()
    push1 = "foo"
    stack.push(push1)
  }

  when "peek is called", {
    peeked = stack.peek()
  }

  then "it should provide the value of the most recent pushed value", {
    ensure(peeked) {
      isEqualTo(push1)
    }
  }

  then "the stack should not be empty", {
    ensure(!stack.isEmpty())
  }

  then "calling pop should also return the peeked value", {
    ensure(stack.pop()) {
      isEqualTo push1
    }
  }

  then "the stack should be empty", {
    ensure(stack.isEmpty())
  }
}