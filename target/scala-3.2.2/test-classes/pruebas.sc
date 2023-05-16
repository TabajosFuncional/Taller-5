import Matrices ._
import Benchmark._
val v1 = Vector(Vector(1,0), Vector(0,1))
val v2 = Vector(Vector(1,0), Vector(1,0))
val v3 = matrizAlAzar(22,2)
val v4 = matrizAlAzar(22,2)
val prueba = multMatriz(v1,v2)
val comparar = compararAlgoritmos(multMatriz, multMatrizPar)(v3,v4)