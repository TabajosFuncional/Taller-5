import Matrices ._
import Benchmark._

// Para cada versión, se ejecutó 5 veces su respectivo for.

// Comparaciones versión estándar y estándar paralelizada.
/*
for {
  i <- 1 to 10
  m1 = matrizAlAzar(math.pow(2,i).toInt, 2)
  m2 = matrizAlAzar(math.pow(2,i).toInt, 2)
} yield (compararAlgoritmos(multMatriz,multMatrizPar)(m1,m2), math.pow(2,i).toInt)
*/

// Comparaciones versión recursiva y recursiva paralelizada.
/*
for {
  i <- 1 to 10
  m1 = matrizAlAzar(math.pow(2,i).toInt, 2)
  m2 = matrizAlAzar(math.pow(2,i).toInt, 2)
} yield (compararAlgoritmos(multMatrizRec,multMatrizRecPar)(m1,m2), math.pow(2,i).toInt)
*/

// Comparaciones versión Strassen y Strassen paralelizada.
/*
for {
  i <- 1 to 8
  m1 = matrizAlAzar(math.pow(2,i).toInt, 2)
  m2 = matrizAlAzar(math.pow(2,i).toInt, 2)
} yield (compararAlgoritmos(multStrassen,multStrassenPar)(m1,m2), math.pow(2,i).toInt)
*/