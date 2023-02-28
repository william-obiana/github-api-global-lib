//const assert = require('assert');
//
//function add(a, b) {
//  return a + b;
//}
//
//assert.strictEqual(add(1, 2), 3, 'add(1, 2) should equal 3');

const sum = require('./sum');

test('adds 1 + 2 to equal 3', () => {
  expect(sum(1, 2)).toBe(3);
});