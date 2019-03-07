<p align="center">
  <img alt="Reactix" src="docs/img/reactix-logo-light-bg.png" width="500">
</p>

<p align="center">
Jegyvásárló alkalmazás Spring WebFlux alapokon.
</p>

---

## Diasor

A diasor megtalálható a `slides` mappában.

## Alkalmazás

### Előkövetelmények

Az alkalmazás futtatásához a következőkkel kell rendelkezned:

  * JDK 1.8+
  * Node.js v10+
  * Docker v18+

### Futtatás

#### 1. docker-compose

A futtatás első lépése a perzisztenciát szolgáltató service-ek (Kafka, MongoDB) elindítása. Ehhez szükséges a docker.

Az indításhoz adjuk ki a következő parancsot a repository gyökerében:

~~~~
docker-compose up
~~~~

Várjunk egy kicsit, amíg a Kafka (vele együtt a ZooKeeper) és a MongoDB is elindul.

#### 2. A MongoDB feltöltése

Az előadók, helyszínek és koncertek adatainak feltöltése egy JavaScriptben készült kis eszközzel történik. 

A következő parancsok segítségével tölthetjük fel a MongoDB-t:

~~~
cd scripts/db-populator
npm i
node src/index.js
~~~

#### 3. A Reactix elindítása

A futtatást Mavenen keresztül tehetjük meg, ehhez azonban nem szükséges lokális Maven telepítéssel rendelkezni, hiszen a repository tartalmaz egy úgynevezett Maven wrappert.

A futtatás a repository gyökeréből tehető meg:

~~~
./mvnw spring-boot:run
~~~

Alapértelmezés szerint az alkalmazás a `8080`-as porton fogja várni a kéréseket.

## Köszönetnyilvánítás

  * A logóban található jegy ikont [Dario Ferrando](https://www.flaticon.com/authors/dario-ferrando) készítette, elérhető a CC-BY 3.0 licenc alatt.
  * A diasor utolsó lapjain található figurákat a [Freepik](https://www.freepik.com/) készítette, elérhetők a CC-BY 3.0 licence alatt.
  * Az előadókról készült képek mind jogos tulajdonosaik birtokát képezik.
