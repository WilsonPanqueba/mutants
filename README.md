# mutants

**TABLA DE CONTENIDO**

[TOCM]

[TOC]

#Descripción

Servicio que verifica mutaciones en el ADN, basado en la premisa que una mutacion es cuando una molecula de ADN, contiene seguidas horizontal, vertical o diagonalmente 4 o mas moleculas del mismo tipo (AAAA, TTTT, CCCC, GGGG).

#Compilación

La version de java es 1.8.

Se debe ubicar en la carpeta mutants y ejecutar el siguiente comando.
`./gradlew clean build`

En este caso se obtendra en la carpeta mutants/build/libs los jar correspondientes.

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

#Despliegue

En la carpeta mutants/compute encontrara los script de bash necesario para el despliegue de la aplicación en GCP, requiere usar gs y gsutil ya sea desde su cuenta o desde local:

- **mutant-startup.sh**: Script para la instancia de **Compute Engine** con el cual se instala java y se ejecuta el componente **mutant-0.0.1-SNAPSHOT.jar**

- **computeengineconfig.sh**: Script para el despliegue de la siguiente manera:

	- El componente **mutant-0.0.1-SNAPSHOT.jar** debe estar en la misma carpeta que estos script.
	- El componente **mutant-0.0.1-SNAPSHOT.jar** se lleva almacena en **Cloud Storage**
	- Crea la infraestructura de  **Compute Engine** necesaria para poder desplegar el servicio
	- Para la ejecución en **nube** se puede hacer uso curl con el siguiente comando, reemplazando HOST por la IP que se suministrata por el administrador de la plataforma una vez se desplieguen las instancias.
`curl -X POST /
  http://HOST/mutant /
  -H 'content-type: application/json' /
  -d '{
"dna":["ATCG","ATCG","ATCG","ATCG"]
}'`

Para verificar que el servicio esta ejecutando puedes llamar al metodo de health
`http://HOST/health`