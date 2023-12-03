# Práctica 3 PPC
Consiste en una mezcla de ambas prácticas anteriores, pero añadiendo el uso de una API para el control de los servidores (de la práctica 2), y también añadiendo la notificación por correo electrónico.

<span style="color: lime;">Basándonos en la topología de la práctica 2</span>.

Vamos <span style="color: aqua;">a poner servicios WEB y de correo electrónico</span>.
Convertimos el cliente en un broker de servicios

Tendremos un cliente de los servidores que a su vez ofrece 
- a otros clientes una <span style="color: yellow;">interfaz WEB donde pueden ver los broadcast</span>.
- <span style="color: yellow;">API REST</span> desde la que se consultan cosas
- Interfaz <span style="color: yellow;">SMTP</span>. Que está conectado a un <span style="color: aqua;">proveedor de email</span> al que los clientes envían cosas.

# Topología
```
.                                    ·----------·-----------·
.                  Firefox ----->    |   HTML   |           |
.                                    ·----------·           |
.                  Firefox ----->    | API REST |  CLIENTE  |
.                                    ·----------·           |
. Cliente correo ---> GMAIL ---->    |   SMTP   |           |
.                                    ·----------·-----------·
```

# Orden de programación
## Semana 1
Programar dentro de la propia clase del cliente la interfaz de consulta HTML.
--> Tenemos que <span style="color: aqua;">añadir un 3er hilo a la programación del cliente </span> donde <span style="color: yellow;">gestionamos HTML</span>.

Cuando el navegador nos recargue la página 'index.html' ésta debe mostrar las distintas funcionalidades que nos permite hacer el cliente de los servidores: apagar, encender, cambiar tiempo de refresco. <span style="color: lime;">Pero solo tienen que ser URLs (o botones), NO TIENE QUE HABER INTRODUCCIÓN DE TEXTO</span>. Y <span style="color: orange;">tampoco hay que poner toda la funcionalidad, solo una poca</span> (representativa --> 6 por ejemplo), una de ellas debe ser mostrar por pantalla el último dato recibido.

<span style="color: yellow;">Hay que hacer 2 hilos:</span>
- HTTP
- HTTPS


