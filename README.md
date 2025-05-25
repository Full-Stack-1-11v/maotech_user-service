# maotech_user-service

USER-SERVICE-BACKEND

ADMIN

- POST/roles/create (Crear rol)

- GET/roles/list (Listar Roles)
- GET/roles/{id}/details (Detalles Rol)
- GET/roles/{id}/delete (Eliminar Rol)

- POST/users/create (Crear usuarios)
- POST/users/login (Conectarse al sistema)

- GET/users/list (Listar usuarios sin password)
- GET/users/list/admin (Listar usuarios con password)
- GET/users/{id}/details (Detalles usuario sin password)
- GET/users/{id}/details/admin (Detalles usuario con password)
- GET/users/inactives (Listar usuarios inactivos sin password)
- GET/users/inactives/admin (Listar usuarios inactivos con password)


- PUT/users/{id}/update (Actualización parcial de datos de usuario)
- PUT/users/{id}/full-update (Actualización completa de datos de usuario)
- PUT/users/{id}/deactivate (Desactivar cuenta)
- PUT/users/{id}/role/edit (Actualizar rol de usuario)

- DELETE/users/{id}/delete (Eliminar usuario)
- DELETE/users/inactives/delete (Eliminar usuarios inactivos)