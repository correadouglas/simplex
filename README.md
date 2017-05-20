# simplex
Implementação do Simplex

Exemplo de JSON a ser passado para a API do Simplex
FO(x) -> Max Z = 80x1 + 60x2
4x1 + 6x2 >= 24
4x1 + 2x2 <= 16
1x2 <= 3

legendas 

objetivo: 1 - Minimizar / 2 - Maximizar

sinaisRestricoes: 1 - Igual (=) / 2 - Maior Igual (>=) / 3 - Menor Igual (<=)

{

"objetivo":2,

"funcaoObjetivo":[80.0,60.0],

"restricoes":[[4.0,6.0],[4.0,2.0],[0.0,1.0]],

"sinaisRestricoes":[2.0,3.0,3.0],

"b":[24.0,16.0,3.0]

}
