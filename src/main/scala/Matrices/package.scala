/*
 * Taller 5: Paralelización de tareas
 * Integrantes: John Freddy Belalcázar - 2182464
 *              Santiago González Gálvez - 2183392
 */

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
  def prodEscalar(v1: Vector[Int], v2: Vector[Int]): Int = {
    (v1 zip v2).map({case (i,j) => i*j}).sum
  }

  //Ejercicio 1.1.1
  def multMatriz(m1: Matriz, m2: Matriz):Matriz = {
    val n = m1.length
    val m2T = transpuesta(m2)

    Vector.tabulate(n, n) { (i, j) =>
      val filaM1 = m1(i)
      val filaM2T = m2T(j)
      prodEscalar(filaM1, filaM2T)
    }
  }

  //Ejercicio 1.1.2
  def multMatrizPar(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val m2T = transpuesta(m2)
    val mitad = n / 2
    val umbral = 16
    if (n <= umbral) multMatriz(m1,m2)
    else {
      val t1 = task(Vector.tabulate(mitad, mitad) { (i, j) => prodEscalar(m1(i), m2T(j))})
      val t2 = task(Vector.tabulate(mitad, mitad) { (i, j) => prodEscalar(m1(i), m2T(j+mitad))})
      val t3 = task(Vector.tabulate(mitad, mitad) { (i, j) => prodEscalar(m1(i+mitad), m2T(j))})
      val t4 = task(Vector.tabulate(mitad, mitad) { (i, j) => prodEscalar(m1(i+mitad), m2T(j+mitad))})

      val matrizSuperior = task((t1.join() zip t2.join()).map({ case (filat1, filat2) => filat1 ++ filat2 }))
      val matrizInferior = (t3.join() zip t4.join()).map({ case (filat3, filat4) => filat3 ++ filat4 })
      matrizSuperior.join() ++ matrizInferior
    }
  }

  //Ejercicio 1.2.1
  def subMatriz(m: Matriz, i: Int, j: Int, l:Int): Matriz = {
    Vector.tabulate(l,l) { (fila,columna) =>
        m(i + fila)(j + columna)
    }
  }

  //Ejercicio 1.2.2
  def sumMatriz(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    Vector.tabulate(n,n){ (i,j) =>
      m1(i)(j) + m2(i)(j)
    }
  }

  //Ejercicio 1.2.3
  def multMatrizRec(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val m = n/2
    if (m == 0) {
      Vector(Vector(m1(0)(0) * m2(0)(0)))
    } else {
      val a11 = subMatriz(m1, 0, 0, m)
      val a12 = subMatriz(m1, 0, m, m)
      val a21 = subMatriz(m1, m, 0, m)
      val a22 = subMatriz(m1, m, m, m)

      val b11 = subMatriz(m2, 0, 0, m)
      val b12 = subMatriz(m2, 0, m, m)
      val b21 = subMatriz(m2, m, 0, m)
      val b22 = subMatriz(m2, m, m, m)

      val c11 = sumMatriz(multMatrizRec(a11,b11), multMatrizRec(a12,b21))
      val c12 = sumMatriz(multMatrizRec(a11,b12), multMatrizRec(a12,b22))
      val c21 = sumMatriz(multMatrizRec(a21,b11), multMatrizRec(a22,b21))
      val c22 = sumMatriz(multMatrizRec(a21,b12), multMatrizRec(a22,b22))

      val matrizSuperior = (c11 zip c12).map({ case (filaC11, filaC12) => filaC11 ++ filaC12 })
      val matrizInferior = (c21 zip c22).map({ case (filaC21, filaC22) => filaC21 ++ filaC22 })
      matrizSuperior ++ matrizInferior
    }
  }

  //Ejercicio 1.2.4
  def multMatrizRecPar(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val umbral = 16
    val m = n / 2
    if (m == 0) {
      Vector(Vector(m1(0)(0) * m2(0)(0)))
    } else {
      if (n <= umbral) multMatrizRec(m1,m2)
      else {
        val (a11, a12, a21, a22) = parallel(subMatriz(m1, 0, 0, m),subMatriz(m1, 0, m, m),subMatriz(m1, m, 0, m),subMatriz(m1, m, m, m))
        val (b11, b12, b21, b22) = parallel(subMatriz(m2, 0, 0, m),subMatriz(m2, 0, m, m),subMatriz(m2, m, 0, m),subMatriz(m2, m, m, m))
        val (c11, c12, c21, c22) = parallel(sumMatriz(multMatrizRecPar(a11, b11), multMatrizRecPar(a12, b21)),
                                            sumMatriz(multMatrizRecPar(a11, b12), multMatrizRecPar(a12, b22)),
                                            sumMatriz(multMatrizRecPar(a21, b11), multMatrizRecPar(a22, b21)),
                                            sumMatriz(multMatrizRecPar(a21, b12), multMatrizRecPar(a22, b22)))
        val matrizSuperior = task((c11 zip c12).map({ case (filaC11, filaC12) => filaC11 ++ filaC12 }))
        val matrizInferior = (c21 zip c22).map({ case (filaC21, filaC22) => filaC21 ++ filaC22 })
        matrizSuperior.join() ++ matrizInferior
      }
    }
  }

  //Ejercicio 1.3.1
  def restaMatriz(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    Vector.tabulate(n, n) { (i, j) =>
      m1(i)(j) - m2(i)(j)
    }
  }

  //Ejercicio 1.3.2
  def multStrassen(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val m = n / 2
    if (m == 0) {
      Vector(Vector(m1(0)(0) * m2(0)(0)))
    } else {
      val a11 = subMatriz(m1, 0, 0, m)
      val a12 = subMatriz(m1, 0, m, m)
      val a21 = subMatriz(m1, m, 0, m)
      val a22 = subMatriz(m1, m, m, m)

      val b11 = subMatriz(m2, 0, 0, m)
      val b12 = subMatriz(m2, 0, m, m)
      val b21 = subMatriz(m2, m, 0, m)
      val b22 = subMatriz(m2, m, m, m)

      val s1 = restaMatriz(b12, b22)
      val s2 = sumMatriz(a11, a12)
      val s3 = sumMatriz(a21, a22)
      val s4 = restaMatriz(b21, b11)
      val s5 = sumMatriz(a11, a22)
      val s6 = sumMatriz(b11, b22)
      val s7 = restaMatriz(a12, a22)
      val s8 = sumMatriz(b21, b22)
      val s9 = restaMatriz(a11, a21)
      val s10 = sumMatriz(b11, b12)

      val p1 = multStrassen(a11, s1)
      val p2 = multStrassen(s2, b22)
      val p3 = multStrassen(s3, b11)
      val p4 = multStrassen(a22, s4)
      val p5 = multStrassen(s5, s6)
      val p6 = multStrassen(s7, s8)
      val p7 = multStrassen(s9, s10)

      val c11 = sumMatriz(restaMatriz(sumMatriz(p5, p4), p2), p6)
      val c12 = sumMatriz(p1, p2)
      val c21 = sumMatriz(p3, p4)
      val c22 = restaMatriz(restaMatriz(sumMatriz(p5, p1), p3), p7)

      val matrizSuperior = (c11 zip c12).map({ case (filaC11, filaC12) => filaC11 ++ filaC12 })
      val matrizInferior = (c21 zip c22).map({ case (filaC21, filaC22) => filaC21 ++ filaC22 })
      matrizSuperior ++ matrizInferior
    }
  }

  //Ejercicio 1.3.3
  def multStrassenPar(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    val umbral = 16
    val m = n / 2
    if (m == 0) {
      Vector(Vector(m1(0)(0) * m2(0)(0)))
    } else {
      if (n <= umbral) multStrassen(m1, m2)
      else {
        val a11 = subMatriz(m1, 0, 0, m)
        val a12 = subMatriz(m1, 0, m, m)
        val a21 = subMatriz(m1, m, 0, m)
        val a22 = subMatriz(m1, m, m, m)

        val b11 = subMatriz(m2, 0, 0, m)
        val b12 = subMatriz(m2, 0, m, m)
        val b21 = subMatriz(m2, m, 0, m)
        val b22 = subMatriz(m2, m, m, m)

        val s1 = task(restaMatriz(b12, b22))
        val s2 = task(sumMatriz(a11, a12))
        val s3 = task(sumMatriz(a21, a22))
        val s4 = task(restaMatriz(b21, b11))
        val s5 = task(sumMatriz(a11, a22))
        val s6 = task(sumMatriz(b11, b22))
        val s7 = task(restaMatriz(a12, a22))
        val s8 = task(sumMatriz(b21, b22))
        val s9 = restaMatriz(a11, a21)
        val s10 = sumMatriz(b11, b12)

        val p1 = task(multStrassenPar(a11, s1.join()))
        val p2 = task(multStrassenPar(s2.join(), b22))
        val p3 = task(multStrassenPar(s3.join(), b11))
        val p4 = multStrassenPar(a22, s4.join())
        val p5 = multStrassenPar(s5.join(), s6.join())
        val p6 = multStrassenPar(s7.join(), s8.join())
        val p7 = multStrassenPar(s9, s10)

        val (c11, c12, c21, c22) = parallel(sumMatriz(restaMatriz(sumMatriz(p5, p4), p2.join()), p6),
          sumMatriz(p1.join(), p2.join()),
          sumMatriz(p3.join(), p4),
          restaMatriz(restaMatriz(sumMatriz(p5, p1.join()), p3.join()), p7))

        val matrizSuperior = task((c11 zip c12).map({ case (filaC11, filaC12) => filaC11 ++ filaC12 }))
        val matrizInferior = (c21 zip c22).map({ case (filaC21, filaC22) => filaC21 ++ filaC22 })
        matrizSuperior.join() ++ matrizInferior
      }
    }
  }
}
