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
    val t1 = task(transpuesta(m2))
    Vector.tabulate(n, n) { (i, j) =>
      val filaM1 = m1(i)
      val columnaM2Transpuesta = t1.join()(j)
      prodPunto(filaM1, columnaM2Transpuesta)
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
    val m = n / 2
    if (m == 0) {
      Vector(Vector(m1(0)(0) * m2(0)(0)))
    } else {
      val (a11, a12, a21, a22) = parallel(subMatriz(m1, 0, 0, m),subMatriz(m1, 0, m, m),subMatriz(m1, m, 0, m),subMatriz(m1, m, m, m))
      val (b11, b12, b21, b22) = parallel(subMatriz(m2, 0, 0, m),subMatriz(m2, 0, m, m),subMatriz(m2, m, 0, m),subMatriz(m2, m, m, m))
      val (c11, c12, c21, c22) = parallel(sumMatriz(multMatrizRec(a11, b11), multMatrizRec(a12, b21)),
                                          sumMatriz(multMatrizRec(a11, b12), multMatrizRec(a12, b22)),
                                          sumMatriz(multMatrizRec(a21, b11), multMatrizRec(a22, b21)),
                                          sumMatriz(multMatrizRec(a21, b12), multMatrizRec(a22, b22)))
      val matrizSuperior = (c11 zip c12).map({ case (filaC11, filaC12) => filaC11 ++ filaC12 })
      val matrizInferior = (c21 zip c22).map({ case (filaC21, filaC22) => filaC21 ++ filaC22 })
      matrizSuperior ++ matrizInferior
    }
  }

  //Ejercicio 1.3.1
  def restaMatriz(m1: Matriz, m2: Matriz): Matriz = {
    val n = m1.length
    Vector.tabulate(n, n) { (i, j) =>
      m1(i)(j) - m2(i)(j)
    }
  }
/*
  //Ejercicio 1.3.2
  def multStrassen(m1: Matriz, m2: Matriz): Matriz = {

  }

  //Ejercicio 1.3.3
  def multStrassenPar(m1: Matriz, m2: Matriz): Matriz = {

  }*/
}
