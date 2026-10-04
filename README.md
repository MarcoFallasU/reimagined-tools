# Reimagined Tools

Mod de Minecraft **Forge 1.21.11** que añade espadas, herramientas y armaduras nuevas. Incluye un set completo de **Rubí** como ejemplo: espada, pico, hacha, pala, azada, lanza, guadaña, sierra y armadura completa.

## Requisitos
- JDK 21 (el mod), y además JDK 25 y JDK 8 instalados (los usa ForgeGradle 7 internamente).
- `./gradlew build` genera el jar en `build/libs/`; `./gradlew runClient` / `runServer` para probarlo.

## Estructura
```
src/main/java/com/marcofallasu/reimaginedtools/
  ReimaginedTools.java        punto de entrada
  material/ModToolMaterials   ToolMaterial (durabilidad, velocidad, daño, reparación)
  material/ModArmorMaterials  ArmorMaterial (defensa, dureza, asset de equipo)
  material/ModTags            tags propios (reparación, bloques no minables)
  item/ToolKind               enum de TIPOS de herramienta (espada, lanza, guadaña, sierra...)
  item/ModItems               registro; un material = un set completo
  item/ScytheItem             ejemplo de herramienta con comportamiento propio (área 3x3)
src/main/resources/assets/reimaginedtools/   items, models, textures, lang, equipment
src/main/resources/data/                     recipe, tags
```

## Añadir un nuevo material (ej. "obsidiana")
1. `ModToolMaterials` / `ModArmorMaterials`: define el material (y sus tags en `ModTags` + `data/.../tags`).
2. `ModItems`: `registerToolSet("obsidian", ...)` y `registerArmorSet("obsidian", ...)`.
3. Texturas `obsidian_<tipo>.png`, modelo + `items/<id>.json`, lang y recetas (copia los de `ruby_*`).
4. Armadura: `assets/.../equipment/obsidian.json` y capas en `textures/entity/equipment/`.
5. Añade los ítems a los tags `minecraft:swords`, `axes`, etc. (los encantamientos dependen de ellos).

## Añadir un nuevo tipo de herramienta (ej. "martillo")
Añade una entrada a `ToolKind` (con su fábrica, y una clase propia si tiene comportamiento especial como `ScytheItem`). Todos los materiales registrados con `registerToolSet` lo obtendrán; solo falta su textura, modelo, lang, receta y tag.

## Notas
- Las texturas son marcadores de posición generados; reemplázalas con tu arte.
- El rubí se obtiene con una receta provisional (diamante + bloque de redstone); añade un mineral y su generación cuando quieras.
- La lanza usa los parámetros de la lanza de diamante de vanilla.
