import Matrices ._
import Benchmark._

//Resultados de las multiplicaciones

val m1 = matrizAlAzar(512, 2)
val m2 = matrizAlAzar(512, 2)
multMatriz(m1,m2)
multMatrizPar(m1,m2)
multMatrizRec(m1,m2)
multMatrizRecPar(m1,m2)
multStrassen(m1,m2)
multStrassenPar(m1,m2)
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
  i <- 1 to 10
  m1 = matrizAlAzar(math.pow(2,i).toInt, 2)
  m2 = matrizAlAzar(math.pow(2,i).toInt, 2)
} yield (compararAlgoritmos(multStrassen,multStrassenPar)(m1,m2), math.pow(2,i).toInt)
*/