`FlatIterator` stores the current inner iterator (if the current element is an Int iterator) in the currentInner variable.

The `hasNext` method checks whether the next element is in the current inner iterator or in the main iterator.

The `next()` method returns the next element, and if the current element is an iterator, then it will iterate over that iterator until it reaches the end, after which it will return to the main iterator inner.
