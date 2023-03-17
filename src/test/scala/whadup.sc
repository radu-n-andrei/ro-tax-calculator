import java.time.{Instant, LocalDateTime}
import java.time.temporal.{ChronoField, IsoFields}

def f(i: Int): Int = {
  println(s"INSIDE f")
  println(i)
  i * 2
}

def g(i: => Int): Int = {
  lazy val in = {
    println("EVAL Lazy")
    i
  }
  println(s"INSIDE g")
  if (LocalDateTime.now().get(ChronoField.MONTH_OF_YEAR) == 4) {
    println(in)
    in * 3
  } else 2
}

g(f(2)) // INSIDE G => 2