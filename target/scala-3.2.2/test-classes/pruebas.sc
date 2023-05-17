import Matrices ._
import Benchmark._

val v3 = matrizAlAzar(128,2)
val v4 = matrizAlAzar(128,2)

//Pruebas multMatriz
val multMatriz1 = multMatriz(v3,v4)
val comparar = compararAlgoritmos(multMatriz, multMatrizPar)(v3,v4)
//Muestra mejora a partir de 8x8
//De la nada, también es más rápido en +2x2

val multRec = multMatrizRec(v3,v4)
val multRecPar = multMatrizPar(v3,v4)

val v3 = matrizAlAzar(256,2) //Muestra mejora a partir de 8x8
val v4 = matrizAlAzar(256,2)
compararAlgoritmos(multMatrizRec,multMatrizRecPar)(v3,v4)
/*
for {
  i <- 1 to 8
  m1 = matrizAlAzar(math.pow(2,i).toInt, 2)
  m2 = matrizAlAzar(math.pow(2,i).toInt, 2)
} yield (compararAlgoritmos(multMatrizRec,multMatrizRecPar)(m1,m2), math.pow(2,i).toInt)
//val comparar2 = compararAlgoritmos(multMatrizRec,multMatrizRecPar)(v3,v4)
*/