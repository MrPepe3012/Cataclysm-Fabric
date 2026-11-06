# Páginas del diario del Lemegeton

Cada archivo `.json` de esta carpeta es una página. Se ordenan por nombre, así que
el número del principio decide su lugar en el libro. Para añadir una página basta
con crear un archivo nuevo; el libro hace sitio solo, dos páginas por doble hoja.

Estas páginas van después de los sellos, que son la primera doble página del libro.
El texto de `05_torn_page` está dibujado en su imagen y no se edita aquí, para que
los archivos del mod no digan de qué trata esa página.

## Página de ejemplo

```json
{
  "art": "twisted_warden",
  "title": "Twisted Warden",
  "subtitle": "God of Beasts. Twisted Nest.",
  "notes": [
    {
      "x": 8, "y": 29, "width": 70,
      "lines": [
        "Una nota normal.",
        "!Una nota en rojo.",
        "NOMBRE: un ataque, en letra pequeña y con el nombre en rojo."
      ]
    }
  ]
}
```

## Campos de la página

| Campo | Obligatorio | Qué hace |
|---|---|---|
| `art` | sí | Imagen de `textures/gui/lemegeton/journal/`, sin `.png`. Mide 156×172. |
| `title` | no | Título centrado, siempre en mayúsculas. |
| `subtitle` | no | Línea gris centrada bajo el título. |
| `title_size` | no | Tamaño del título: `1` por defecto, `2` para el doble. |
| `dark` | no | `true` para una página negra: la tinta pasa a ser clara. |
| `behind` | no | Nombre de otra página sin `.json`. Se ve debajo, y `art` se dibuja encima de ella. |
| `notes` | no | Lista de bloques de texto. |

## Campos de cada bloque de `notes`

| Campo | Por defecto | Qué hace |
|---|---|---|
| `y` | obligatorio | Altura donde empieza el bloque. |
| `x` | `8` | Distancia desde el borde izquierdo de la página. |
| `width` | `144` | Ancho del bloque. El texto salta de línea solo. |
| `centered` | `false` | Centra cada línea dentro del bloque. |
| `lines` | obligatorio | Las notas, de arriba abajo. No hace falta colocarlas una a una. |

## Cómo se escribe cada línea

- Una línea normal va en tinta negra.
- Una línea que empieza por `!` va en rojo.
- Una línea con `:` es un ataque: va en letra pequeña y lo que hay antes de `:` va en rojo.
  Hoy el diario solo lleva notas de un superviviente, así que no uses `:` en una nota normal.
  El formato sigue disponible para cuando quieras mostrar los ataques.
- Una línea también puede ser una clave de traducción del lang.

## Probar un cambio sin reiniciar el juego

1. Guarda el archivo y ejecuta la tarea `processResources` de Gradle.
2. Pulsa F3+T en el juego.
3. Vuelve a abrir el libro.

Si un archivo tiene un error, esa página no aparece y el log lo avisa con
`Could not read journal page`. El resto del libro sigue funcionando.
