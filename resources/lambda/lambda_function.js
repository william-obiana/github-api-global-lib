const assert = require('assert');

function add(a, b) {
  return a + b;
}

assert.strictEqual(add(1, 2), 3, 'add(1, 2) should equal 3');
