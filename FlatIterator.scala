package iterator

import scala.annotation.tailrec

object FlatIterator extends App {
  trait CustomIterator[T] {
    def next(): T
    def hasNext: Boolean
  }

  type IntOrIterInt = Either[Int, Iterator[Int]]

  final class FlatIterator(inner: CustomIterator[IntOrIterInt]) extends CustomIterator[Int] {
    private var currentInner: Option[Iterator[Int]] = None

    def hasNext: Boolean =
      currentInner match {
        case Some(it) => if (it.hasNext) true else inner.hasNext
        case None     => inner.hasNext
      }

    @tailrec
    def next(): Int =
      currentInner match {
        case Some(it) =>
          if (it.hasNext) it.next()
          else {
            currentInner = None
            next()
          }
        case None =>
          inner.next() match {
            case Left(value) => value
            case Right(it) =>
              currentInner = Some(it)
              next()
          }
      }
  }

  val innerIterator: CustomIterator[IntOrIterInt] = new CustomIterator[IntOrIterInt] {
    private val list = List[IntOrIterInt](Left(1), Left(2), Left(3), Left(4), Right(Iterator(5, 6, 7)), Left(8), Left(9))
    private var index = 0

    def next(): IntOrIterInt = {
      val result = list(index)
      index += 1
      result
    }

    def hasNext: Boolean = index < list.length
  }

  val flat = new FlatIterator(innerIterator)

  while (flat.hasNext) {
    println(flat.next()) // Should print: 1,2,3,4,5,6,7,8,9
  }
}
