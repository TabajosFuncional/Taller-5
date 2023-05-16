import common . _
import scala.util.Random
package object Matrices {
  val random = new Random()
  type Matriz = Vector[Vector[Int]]

  def matrizAlAzar(long: Int, vals: Int): Matriz = {
    val v = Vector.fill(long, long){random.nextInt(vals)}
    v
  }
  def transpuesta(m: Matriz): Matriz = {
    val l = m.length
    Vector.tabulate(l,l)((i,j) => m(j)(i))
  }
  def prodPunto(v1: Vector[Int], v2: Vector[Int]): Int = {
    (v1 zip v2).map({case (i,j) => i*j}).sum
  }

  //Ejercicio 1.1.1
  def multMatriz(m1: Matriz, m2: Matriz):Matriz = {
    val n = m1.length
    val m2Transpuesta = transpuesta(m2)

    Vector.tabulate(n, n) { (i, j) =>
      val filaM1 = m1(i)
      val columnaM2Transpuesta = m2Transpuesta(j)
      prodPunto(filaM1, columnaM2Transpuesta)
    }
  }

  //Ejercicio 1.1.2
  def multMatrizPar(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val m2Transpuesta = transpuesta(m2)
    //val (t1,t2) = parallel(m1.length,transpuesta(m2))
    Vector.tabulate(n, n) { (i, j) =>
      val (filaM1, columnaM2Transpuesta) = parallel(m1(i),m2Transpuesta(j))
      //val filaM1 = m1(i)
      //val columnaM2Transpuesta = t2(j)
      prodPunto(filaM1, columnaM2Transpuesta)
    }
  }
}
