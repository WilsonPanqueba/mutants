# mutants

**TABLA DE CONTENIDO**

[TOCM]

[TOC]

#Descripción

Servicio que verifica mutaciones en el ADN, basado en la premisa que una mutacion es cuando una molecula de ADN, contiene seguidas horizontal, vertical o diagonalmente 4 o mas moleculas del mismo tipo (AAAA, TTTT, CCCC, GGGG).

#Compilación

La version de java es 1.8.

Se debe ajustar los parametros de mutants/src/main/resources/application.yml para que correspondan a [Base de datos][Base de Datos]

Se debe ubicar en la carpeta mutants y ejecutar el siguiente comando.
`./gradlew clean build`

En este caso se obtendra en la carpeta mutants/build/libs los jar correspondientes.

#Despliegue

##Base de Datos

En la carpeta **mutants/database** se encuentra el script para la creacion de la base de datos, el motor debe ser postgresql
-**dml.sql**: Creacion de la tabla para el almancenamiento de los test de ADN.

#Ejecución

Desde la carpeta donde alije los jar, se debe ejecutar el comando.
`java -jar mutant-0.0.1-SNAPSHOT.jar`

El puerto de ejecucion es el 8080.

Para la ejecución en local se puede hacer uso curl con el siguiente comando.
`curl -X POST /
  http://localhost:8080/mutant /
  -H 'content-type: application/json' /
  -d '{
"dna":["ATCG","ATCG","ATCG","ATCG"]
}'`

Para verificar la ejecucion de las estadisticas
`curl -X GET \
  http://localhost:8080/stats`

Para verificar que el servicio esta ejecutando puedes llamar al metodo de health
`curl -X GET \
  http://localhost:8080/health`
[Base de Datos]: #Base%20de%20Datos "Base de datos"