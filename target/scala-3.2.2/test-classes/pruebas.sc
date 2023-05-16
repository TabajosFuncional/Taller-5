import Matrices ._
import Benchmark._

val v3 = matrizAlAzar(12,2) //Muestra mejora a partir de 8x8
val v4 = matrizAlAzar(12,2) //De la nada, también es más rápido en +2x2
val comparar = compararAlgoritmos(multMatriz, multMatrizPar)(v3,v4)