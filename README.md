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

## Recetas y obtención
- **Rubí:** se encuentra como **mena de rubí** (y su variante de pizarra abisal) bajo tierra. Se concentra donde empieza la pizarra abisal (y = 0) y es cada vez menos común al bajar hacia el fondo; no aparece por encima de y = 7. Los bloques que tocan aire no se descartan, así que también se ven desde las cuevas. Necesita pico de hierro o mejor y soporta fortuna y toque de seda. Las herramientas y armadura se craftean con la gema, con la forma clásica de vanilla.
- **Cobre:** 2 lingotes de cobre + 1 redstone dan 1 *lingote de cobre cargado*.
- **Lapislázuli:** 4 botellas de experiencia + 4 lapislázuli dan 1 *lapislázuli cargado*.
- **Amatista:** 3 fragmentos de amatista + 4 brotes pequeños de amatista (solo se obtienen con toque de seda) dan 1 *amatista cargada*.
- Las 8 herramientas de cobre, lapislázuli y amatista se craftean y reparan con su material cargado (mismas formas que las del rubí). Las armaduras de cobre, lapislázuli y amatista aún no tienen receta.

## Materiales conceptuales (oscuridad, luz, electricidad, música)
Cada uno tiene su ítem base (esencia de oscuridad, cristal de luz, núcleo de tormenta, nota resonante), que sirve para reparar sus 8 herramientas. Las recetas están pendientes. La habilidad especial está en la **espada** (clic derecho):
- **Oscuridad:** stats y resistencia al fuego idénticos a la netherite. Su habilidad (que dañará a quien la usa) está pendiente.
- **Luz:** tras 0,75 s bloqueado (no puedes moverte, atacar, interactuar ni usar ítems), te teletransporta al bloque que miras (hasta 30 bloques). Enfriamiento de 3 s.
- **Electricidad:** dash de 10 bloques en 5 ticks que daña una vez, con x1,5 del ataque, a todo lo que atraviesa; se detiene ante los bloques. Enfriamiento de 4 s.
- **Música:** une a los jugadores a 12 bloques y, tras 3 s (sin bloquear a nadie), les da Velocidad II y Fuerza I durante 15 s. Enfriamiento de 20 s.

Las habilidades de clic derecho funcionan en la espada, el pico, la guadaña y la sierra (clase `AbilityItem`); el hacha, la pala, la azada y la lanza ya usan el clic derecho en vanilla. Para darle una a otro ítem, `Abilities.attach(...)` en `ModAbilities`.

## Notas
- Las texturas son marcadores de posición generados; reemplázalas con tu arte.
- La lanza usa los parámetros de la lanza de diamante de vanilla.
