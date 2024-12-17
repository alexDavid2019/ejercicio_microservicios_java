
## Spring Boot Practica Registro de usuario

Proyecto de practica restful, permitiendo registrar datos de usuario y sus telefonos.
Utilizando H2 o hsqldb como repositorio. Mavem y Spring Boot.

Premisas:
- Retornar una estructura de respuesta comun para todos los endpoints.
```
Ejemplo: 
{
    "success": <Boolean>,
    "message": <String>,
    "data": {  ....  },
    "errors": <String>,
    "errorCode": <Number>,
    "timestamp": <Number>,
    "path": <String>
}
```
- Permitir utilizar swagger como herramienta de documentacion.
```
url: http://localhost:8080/swagger-ui.html#/
Screenshots
<p align="center">
<kbd>
<img src="doc/swagger_ui.png">
</kbd>
</p>

```
- Utilizar identificador de registro con UUID para los campos PK.
- Utilizar Roles (ADMIN, USER, OTHER) para identificar permisos. Por defecto utilizar role USER.
- Utilizar libreria Parent para centralizar dependencias comunes entre proyectos.

## Modo de uso

```
- 1) Post de un nuevo usuario mediante postman.

curl --location --request POST 'http://localhost:8080/v1.0/api/users/' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "Juan Rodriguez",
"email": "juan@rodriguez.org",
"password": "hunter2",
"phones": [{"number": "1234567","cityCode": "1","countryCode": "57"}]
}
'

output

{
    "success": true,
    "message": "User created successfully",
    "data": {
        "id": "0ad48689-93d4-14a2-8193-d424e8aa0003",
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "password": "$2a$10$w4zS.m/b0i7C1UvUV7dfZOeNGw.IQNb910nBaHwen2nPSNQ/2BB9G",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhci5leGFtcGxlLnJlZ2lzdHJhdGlvbi5wZXJzaXN0ZW5jZS5lbnRpdHkuVXNlckVudGl0eUA3M2JmOWZkNCIsImlhdCI6MTczNDQzMTAwOCwiZXhwIjoxNzM1MDM1ODA4fQ.waGTUGZVZEr8gl7rUuLmvm2WEZK_Qdt_5NZNEDOZBTusWS4OoOy21PDngeJg6Y0j8N_RqP9RWN20Tg_G_fqZaw",
        "created": 1734431011982,
        "modified": 1734431012003,
        "lastAccess": null,
        "roles": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424c0bf0001",
                "name": "USER"
            }
        ],
        "phones": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424e8ad0004",
                "number": "1234567",
                "cityCode": "1",
                "countryCode": "57"
            }
        ]
    },
    "errors": null,
    "errorCode": 0,
    "timestamp": 1734431012088,
    "path": "/v1.0/api/users/"
}
```
```
- 2) Get del usuario mediante ID utilizando postman.

curl --location --request GET 'http://localhost:8080/v1.0/api/users/0ad48689-93d4-14a2-8193-d424e8aa0003' \

output

{
    "success": true,
    "message": "User successfully",
    "data": {
        "id": "0ad48689-93d4-14a2-8193-d424e8aa0003",
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "password": "$2a$10$w4zS.m/b0i7C1UvUV7dfZOeNGw.IQNb910nBaHwen2nPSNQ/2BB9G",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhci5leGFtcGxlLnJlZ2lzdHJhdGlvbi5wZXJzaXN0ZW5jZS5lbnRpdHkuVXNlckVudGl0eUA3M2JmOWZkNCIsImlhdCI6MTczNDQzMTAwOCwiZXhwIjoxNzM1MDM1ODA4fQ.waGTUGZVZEr8gl7rUuLmvm2WEZK_Qdt_5NZNEDOZBTusWS4OoOy21PDngeJg6Y0j8N_RqP9RWN20Tg_G_fqZaw",
        "created": 1734431011982,
        "modified": 1734431012003,
        "lastAccess": null,
        "roles": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424c0bf0001",
                "name": "USER"
            }
        ],
        "phones": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424e8ad0004",
                "number": "1234567",
                "cityCode": "1",
                "countryCode": "57"
            }
        ]
    },
    "errors": null,
    "errorCode": 0,
    "timestamp": 1734431035485,
    "path": "/v1.0/api/users/0ad48689-93d4-14a2-8193-d424e8aa0003"
}
```
```
- 3) Post para el Login del usuario mediante usuario y password utilizando postman.


curl --location --request POST 'http://localhost:8080/v1.0/api/users/log-in' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "juan@rodriguez.org",
"password": "hunter2"
}
'

output 

{
    "success": true,
    "message": "User logged successfully",
    "data": {
        "id": "0ad48689-93d4-14a2-8193-d424e8aa0003",
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "password": "$2a$10$w4zS.m/b0i7C1UvUV7dfZOeNGw.IQNb910nBaHwen2nPSNQ/2BB9G",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJpYXQiOjE3MzQ0MzEwNDksImV4cCI6MTczNTAzNTg0OX0.VPMxgVyMSsPePv-zatqXJkXv5osqGJLTGDHp4nALJFmkDBq9rI_9SXKyKOoFo9Ejzt79D3xCm-__8Y5FVYL9lQ",
        "created": 1734431011982,
        "modified": 1734431049922,
        "lastAccess": 1734431049921,
        "roles": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424c0bf0001",
                "name": "USER"
            }
        ],
        "phones": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424e8ad0004",
                "number": "1234567",
                "cityCode": "1",
                "countryCode": "57"
            }
        ]
    },
    "errors": null,
    "errorCode": 0,
    "timestamp": 1734431049930,
    "path": "/v1.0/api/users/log-in"
}

```
```
- 4) Repetimos Get del usuario mediante ID utilizando postman, donde se observa el atributo lastAccess con la fecha del acceso.
curl --location --request GET 'http://localhost:8080/v1.0/api/users/0ad48689-93d4-14a2-8193-d424e8aa0003' \

output 

{
    "success": true,
    "message": "User successfully",
    "data": {
        "id": "0ad48689-93d4-14a2-8193-d424e8aa0003",
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "password": "$2a$10$w4zS.m/b0i7C1UvUV7dfZOeNGw.IQNb910nBaHwen2nPSNQ/2BB9G",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJpYXQiOjE3MzQ0MzEwNDksImV4cCI6MTczNTAzNTg0OX0.VPMxgVyMSsPePv-zatqXJkXv5osqGJLTGDHp4nALJFmkDBq9rI_9SXKyKOoFo9Ejzt79D3xCm-__8Y5FVYL9lQ",
        "created": 1734431011982,
        "modified": 1734431049922,
        "lastAccess": 1734431049921,
        "roles": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424c0bf0001",
                "name": "USER"
            }
        ],
        "phones": [
            {
                "id": "0ad48689-93d4-14a2-8193-d424e8ad0004",
                "number": "1234567",
                "cityCode": "1",
                "countryCode": "57"
            }
        ]
    },
    "errors": null,
    "errorCode": 0,
    "timestamp": 1734431174399,
    "path": "/v1.0/api/users/0ad48689-93d4-14a2-8193-d424e8aa0003"
}
```


