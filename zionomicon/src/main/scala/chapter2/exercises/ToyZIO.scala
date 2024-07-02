package chapter2.exercises

final case class ToyZIO[-R, +E, +A](run: R => Either[E, A])
