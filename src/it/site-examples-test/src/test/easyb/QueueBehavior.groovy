import org.disco.easyb.bdd.Queue

before "initialize the queue for each spec", {
  queue = new Queue()
}

it "should dequeue gives item just enqueued", {
  queue.enqueue(2)
  ensure(queue.dequeue()) {
    isEqualTo2
  }
}

it "should throw an exception when null is enqueued", {
  ensureThrows(RuntimeException.class) {
    queue.enqueue(null)
  }
}