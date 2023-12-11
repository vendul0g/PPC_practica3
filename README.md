# Práctica 3 PPC
[Boletín](https://aulavirtual.um.es/access/content/group/3880_G_2023_N_N/Pr%C3%A1cticas/PPC-Practica3-boletin.pdf) --> Tiene un dibujo muy chulo :)

<span style="color: lime;">Basándonos en la topología de la práctica 2</span>.

Vamos <span style="color: aqua;">a poner servicios WEB y de correo electrónico</span>.
Convertimos el cliente en un broker de servicios

Tendremos un cliente de los servidores que a su vez ofrece 
- a otros clientes una <span style="color: yellow;">interfaz WEB donde pueden ver los broadcast</span>.
- <span style="color: yellow;">API REST</span> desde la que se consultan cosas
- Interfaz <span style="color: yellow;">SMTP</span>. Que está conectado a un <span style="color: aqua;">proveedor de email</span> al que los clientes envían cosas.

# Topología
```
.                                    ·----------·-----------·         ·----·
.                  Firefox ----->    |   HTML   |           |     ·---| S1 |
.                                    ·----------·           |     |   ·----·
.                  Firefox ----->    | API REST |  CLIENTE  |-----·---| S2 |
.                                    ·----------·           |     |   ·----·
. Cliente correo ---> GMAIL ---->    |   SMTP   |           |     ·---| S3 |
.                                    ·----------·-----------·         ·----·
```

# Orden de programación
## Semana 1
Programar dentro de la propia clase del cliente la interfaz de consulta HTML.
--> Tenemos que <span style="color: aqua;">añadir un 3er hilo a la programación del cliente </span> donde <span style="color: yellow;">gestionamos HTML</span>.

Cuando el navegador nos recargue la página 'index.html' ésta debe mostrar las distintas funcionalidades que nos permite hacer el cliente de los servidores: apagar, encender, cambiar tiempo de refresco. <span style="color: lime;">Pero solo tienen que ser URLs (o botones), NO TIENE QUE HABER INTRODUCCIÓN DE TEXTO</span>. Y <span style="color: orange;">tampoco hay que poner toda la funcionalidad, solo una poca</span> (representativa --> 6 por ejemplo), una de ellas debe ser mostrar por pantalla el último dato recibido.

<span style="color: yellow;">Hay que hacer 2 hilos:</span>
- HTTP
- HTTPS

# Semana 2
Implementación del servidor de correo y la API
## Correo
Tenemos un hilo dentro del cliente que va comprobando el correo.

Hay que comprobar contínuamente si tenemos correos nuevos (descarga de buzón, recorrido y  comprobación). -->  La comprobación cada 30 segundos está bien.

Cuando llega un correo electrónico, analizamos su contenido para saber que es del tipo que queremos

### Configuración de correo

- <span style="color: orange;">POP</span>: Parámetros que se meten como propiedades 
```
mail.pop3.host = "pop.gmail.com"
mail.pop3.port = 995
mail.pop3.ssl trust = *
mail.pop3.ssl enable = true
```

- <span style="color: orange;">SMTP</span>: parámetros que se meten como propiedades
```
mail.smtp.host = "smtp.gmail.com"
mail.smtp.port = 587
mail.smtp.auth = true
mail.smtp.startls.enable = true
```

### Contenido del correo
```
TO <receptor> FROM <emisor>
SUBJECT <asunto>
<Datos deserializados de los 3 servidores>
<Datos serializados de los 3 servidores
```

Los  JSON que se van mandando de broadcast hay que almacenarlos en el cliente, para que se muestre por consola si se pide con el showlastentry, o por si nos lo piden del correo, porque hay que mandar los valores en claro (deserializados) y luego los valores serializados.
<span style="color: lime;">ShowLastEntry guarda el json del último valor recibido</span>.

Es decir. Tenemos que habilitar un "comando" para mostrar el último registro de mensaje broadcast obtenido. Este registro habrá que mandarlo en JSON cuando se pida por la API, o bien habrá que usarlo para construir el correo cuando se nos pida

## API
Lo mismo que teníamos hasta ahora con los enlaces pero en formato de api 
```
http://loclhost/apirest/camia_freq?servidor=1&valor=500
```
 <span style="color: yellow;">Nos devuelve un JSON</span>.

Tiene que ser capaz de realizar toda la funcionalidad que podría hacer un cliente por consola# Práctica 3 PPC
[Boletín](https://aulavirtual.um.es/access/content/group/3880_G_2023_N_N/Pr%C3%A1cticas/PPC-Practica3-boletin.pdf) --> Tiene un dibujo muy chulo :)

<span style="color: lime;">Basándonos en la topología de la práctica 2</span>.

Vamos <span style="color: aqua;">a poner servicios WEB y de correo electrónico</span>.
Convertimos el cliente en un broker de servicios

Tendremos un cliente de los servidores que a su vez ofrece 
- a otros clientes una <span style="color: yellow;">interfaz WEB donde pueden ver los broadcast</span>.
- <span style="color: yellow;">API REST</span> desde la que se consultan cosas
- Interfaz <span style="color: yellow;">SMTP</span>. Que está conectado a un <span style="color: aqua;">proveedor de email</span> al que los clientes envían cosas.

# Topología
```
.                                    ·----------·-----------·         ·----·
.                  Firefox ----->    |   HTML   |           |     ·---| S1 |
.                                    ·----------·           |     |   ·----·
.                  Firefox ----->    | API REST |  CLIENTE  |-----·---| S2 |
.                                    ·----------·           |     |   ·----·
. Cliente correo ---> GMAIL ---->    |   SMTP   |           |     ·---| S3 |
.                                    ·----------·-----------·         ·----·
```

# Orden de programación
## Semana 1
Programar dentro de la propia clase del cliente la interfaz de consulta HTML.
--> Tenemos que <span style="color: aqua;">añadir un 3er hilo a la programación del cliente </span> donde <span style="color: yellow;">gestionamos HTML</span>.

Cuando el navegador nos recargue la página 'index.html' ésta debe mostrar las distintas funcionalidades que nos permite hacer el cliente de los servidores: apagar, encender, cambiar tiempo de refresco. <span style="color: lime;">Pero solo tienen que ser URLs (o botones), NO TIENE QUE HABER INTRODUCCIÓN DE TEXTO</span>. Y <span style="color: orange;">tampoco hay que poner toda la funcionalidad, solo una poca</span> (representativa --> 6 por ejemplo), una de ellas debe ser mostrar por pantalla el último dato recibido.

<span style="color: yellow;">Hay que hacer 2 hilos:</span>
- HTTP
- HTTPS

# Semana 2
Implementación del servidor de correo y la API
## Correo
Tenemos un hilo dentro del cliente que va comprobando el correo.

Hay que comprobar contínuamente si tenemos correos nuevos (descarga de buzón, recorrido y  comprobación). -->  La comprobación cada 30 segundos está bien.

Cuando llega un correo electrónico, analizamos su contenido para saber que es del tipo que queremos

### Configuración de correo

- <span style="color: orange;">POP</span>: Parámetros que se meten como propiedades 
```
mail.pop3.host = "pop.gmail.com"
mail.pop3.port = 995
mail.pop3.ssl trust = *
mail.pop3.ssl enable = true
```

- <span style="color: orange;">SMTP</span>: parámetros que se meten como propiedades
```
mail.smtp.host = "smtp.gmail.com"
mail.smtp.port = 587
mail.smtp.auth = true
mail.smtp.startls.enable = true
```

### Contenido del correo
```
TO <receptor> FROM <emisor>
SUBJECT <asunto>
<Datos deserializados de los 3 servidores>
<Datos serializados de los 3 servidores
```

Los  JSON que se van mandando de broadcast hay que almacenarlos en el cliente, para que se muestre por consola si se pide con el showlastentry, o por si nos lo piden del correo, porque hay que mandar los valores en claro (deserializados) y luego los valores serializados.
<span style="color: lime;">ShowLastEntry guarda el json del último valor recibido</span>.

Es decir. Tenemos que habilitar un "comando" para mostrar el último registro de mensaje broadcast obtenido. Este registro habrá que mandarlo en JSON cuando se pida por la API, o bien habrá que usarlo para construir el correo cuando se nos pida

## API
Lo mismo que teníamos hasta ahora con los enlaces pero en formato de api 
```
http://loclhost/apirest/camia_freq?servidor=1&valor=500
```
 <span style="color: yellow;">Nos devuelve un JSON</span>.

Tiene que ser capaz de realizar toda la funcionalidad que podría hacer un cliente por consola
