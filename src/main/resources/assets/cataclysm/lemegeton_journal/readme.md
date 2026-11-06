# Cómo editar el Lemegeton

Cada página del libro es un archivo de esta carpeta. No hace falta saber programar.

## Cambiar un texto

1. Abre el archivo de la página, por ejemplo `030_twisted_warden.json`.
2. Cambia las frases que están entre comillas.
3. Guarda el archivo.
4. En el juego, cierra el libro y vuelve a abrirlo. Ya está.

Esto funciona con el juego abierto desde el proyecto. No hay que reiniciar nada.

## Añadir una página

1. Copia `_plantilla.json`.
2. Ponle un nombre con número, por ejemplo `090_mi_pagina.json`.
3. Escribe tu texto, guarda y abre el libro.

## El orden de las páginas

El número del principio del nombre decide el orden. Van de 10 en 10 para dejar huecos.
¿Quieres una página entre la `030` y la `040`? Llámala `035_lo_que_sea.json`.

El libro enseña dos páginas a la vez, una a cada lado. Si añades una sola página en
medio, todas las siguientes cambian de lado. Por eso conviene añadirlas de dos en dos,
sobre todo antes de `050_pale_king`, `060_void_lord` y `070_torn_page`, que están
pensadas para verse juntas.

La página `020_en_blanco` está ahí justo para eso: mantiene cada página en su lado.
Puedes escribir en ella sin mover nada.

## Página y entrada no son lo mismo

- La **página** es el numerito de abajo. Se cuenta sola, según el orden de los archivos.
- La **entrada** es lo que tú escribes en `subtitle`, como `Entrada I`. No se cuenta sola.

Así, la Entrada I puede estar en la página 3, y no pasa nada.

## Qué significa cada cosa en un archivo

| Palabra | Para qué sirve |
|---|---|
| `art` | La imagen de fondo. Si no la pones, la hoja queda vacía. |
| `title` | El título rojo. |
| `subtitle` | La línea gris de debajo del título. |
| `title_size` | Pon `2` para un título el doble de grande. |
| `dark` | Pon `true` para una página negra con letras claras. |
| `behind` | Otra página que se ve debajo de esta, como en la hoja arrancada. |
| `notes` | Los trozos de texto de la página. |

Cada trozo de texto de `notes` tiene:

| Palabra | Para qué sirve |
|---|---|
| `y` | A qué altura empieza. `0` es arriba del todo y `158` es casi abajo. |
| `x` | A qué distancia del borde izquierdo. Si no la pones, es `8`. |
| `width` | Cuánto ocupa de ancho. Si no la pones, es `144`, la hoja entera. |
| `centered` | Pon `true` para centrar el texto. |
| `lines` | Las frases, de arriba abajo. |

## Trucos para escribir

- Una frase que empieza por `!` sale en rojo.
- Una frase con `:` sale en letra pequeña y lo que hay antes de `:` en rojo.
  Sirve para los ataques, así que no uses `:` en una frase normal.
- No uses mayúsculas con tilde, como `Á`. Las fuentes no las tienen.

## Si algo sale mal

Si un archivo tiene un error, el libro enseña una página roja que dice qué archivo es.
Casi siempre es una de estas tres cosas:

- Falta una coma entre dos frases.
- Sobra una coma después de la última frase.
- Falta una comilla.

## Poner una imagen nueva

1. Dibuja una imagen de **312 × 392** píxeles con fondo transparente.
   Lo transparente deja ver el papel del libro. Deja libres 24 píxeles arriba y abajo:
   son los márgenes de la hoja.
2. Guárdala en `textures/gui/lemegeton/journal/`, con el nombre en minúsculas.
3. Escribe ese nombre sin `.png` en `art`.

Las imágenes nuevas sí necesitan reiniciar el juego.

Ojo: el texto de `torn_page.png` está dibujado dentro de la imagen, para que nadie
pueda leerlo en los archivos del mod.

## Cambiar los sellos

Esto ya es código, pero son pocas líneas. Después hay que reiniciar el juego.

**Añadir un sello.** Abre `LemegetonComponent.java` y añade una línea a la lista `Upgrade`:

```java
LUCK(MobEffects.LUCK, 0, 1),
```

Primero va el efecto. Luego, un número por cada nivel: `0, 1` son dos niveles.
El orden de la lista es el orden en el círculo, y el círculo se reparte solo.
El dibujo del sello sale solo del icono del efecto.

Después abre `CataclysmLanguageProvider.java`, añade su nombre y su descripción, y
ejecuta la tarea `runDatagen` de Gradle:

```java
this.addUpgrade(LemegetonComponent.Upgrade.LUCK, "Luck", "Better loot.");
```

**Cambiar las reglas.** Están al principio de `LemegetonProcessor.java`:

| Qué quieres cambiar | Dónde |
|---|---|
| Cuántos pactos da cada fase | `LEVELS_PER_PHASE` |
| En qué fase se pide el pago de grado 2 | `SECOND_TIER_PHASE` |
| En qué fase se pide el pago de grado 3 | `THIRD_TIER_PHASE` |

**Cambiar la velocidad al pasar página.** Es `TURN_MILLIS` en `LemegetonJournal.java`.
Son milésimas de segundo: un número más bajo pasa más rápido.
