API desarrollada en Spring Boot - Servicio simple provisto por una plataforma de venta de productos

MODO DE USO

Tiene dos controladores diferenciados e independientes: "Productos" y "Clientes"

EN EL SERVICIO DE PRODUCTOS SE PUEDE:

1- Consultar el listado de todos los productos disponibles (GET: api/v1/articles)

2- La consulta anterior admite parámetros opcionales de filtrado y ordenamiento:

  a- Filtrar con "category", "brand", "price", "freeShipping", "prestige" (el filtro de precio indica el precio máximo de búsqueda)
  Se admite un máximo de dos filtros en simultáneo
  
  b- Ordenar los productos por nombre (0 y 1) y por precio (2 y 3) con el parámetro "orden"
  
3- Efectuar una solictud de compra a (POST: api/v1/purchase-request)

  a- Requiere un objeto tipo JSON en el body, como por ejemplo:
  
      {
        "articles": [
            {
                "productId": 1,
                "name": "Desmalezadora",
                "brand": "Makita",
                "quantity": 3
            },
            {
                "productId": 5,
                "name": "Zapatillas Deportivas",
                "brand": "Nike",
                "quantity": 2
            },
            {
                "productId": 9,
                "name": "remera",
                "brand": "taverniti",
                "quantity": 2
            }
        ]
    }
    
4- Agregar artículos al carrito de compras a (POST: api/v1/carrito)

  a- Requiere un número de id como parámetro, identificando el usuario y su carrito.
  
  b- En caso de ser la primera operación del usuario, su id será creada con el parámetro provisto
  
  c- Requiere además un objeto de tipo JSON en el body, de igual formato que en el punto 3
  
5- Comprar todo los artículos agregados al carrito con (POST: api/v1/carrito/comprar)

  a- Requiere el número de id como parámetro, creado en el punto 4
  
En este servicio no se persisten los datos, toda la información se trabaja en memoria

EN EL SERVICIO DE CLIENTES SE PUEDE:

1- Crear un cliente (POST: api/v2/client-create)

  a- Requiere la contraseña en el header como parámetro "password". Admite letras minúsculas, mayúsculas y números, entre 6 y 12 caracteres (por ej: "Ab1234")
  
  b- Requiere el resto de la información en un objeto tipo JSON en el body, como por ejemplo:
  
      {
        "dni": 56920,
        "nombre": "Alejandro",
        "email": "alejandro@yahoo.com.ar",
        "provincia": "Mendoza"
      }
      
2- Solicitar la lista de clientes (GET: api/v2/client-list)

  a- Admite como parámetro opcional "provincia" para filtar durante la búsqueda

En este servicio se persisten los datos, toda la información se guarda en un archivo .csv


  
