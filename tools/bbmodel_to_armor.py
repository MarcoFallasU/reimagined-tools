#!/usr/bin/env python3
"""Convierte un .bbmodel de armadura (Blockbench, formato GeckoLib "Armor") en los recursos del mod.

Uso:
    python3 tools/bbmodel_to_armor.py --bbmodel Light_Armor.bbmodel --name light

Genera, bajo src/main/resources/assets/<modid>/:
  textures/item/<name>_armor.png          textura compartida (la que va incrustada en el .bbmodel)
  models/item/<name>_<pieza>_<hueso>.json un modelo por hueso, con coordenadas relativas al pivote del hueso
  models/item/<name>_<pieza>.json         modelo compuesto de la pieza (icono del inventario)
  items/<name>_<pieza>.json               elige el modelo según el dato personalizado que pone ModelArmorLayer
  equipment/<name>.json                   asset de equipo "vacío" (ver README)

El .bbmodel debe tener grupos "bipedXxx" que contengan un grupo "armorXxx" con los cubos (anidados a cualquier
profundidad). Los cubos con "export": false (los "dontTouch" de referencia) se omiten. Los cubos con rotación se
convierten a la rotación de elemento de Minecraft. Quedan por hacer a mano: textos (lang), tags, Java y receta.
"""
import argparse, base64, json, os

BONES = {  # grupo -> (pivote Blockbench, pieza, hueso que lo coloca en ModelArmorLayer)
    "armorHead": ((0, 24, 0), "helmet", "head"),
    "armorBody": ((0, 24, 0), "chestplate", "body"),
    "armorRightArm": ((5, 22, 0), "chestplate", "right_arm"),
    "armorLeftArm": ((-5, 22, 0), "chestplate", "left_arm"),
    "armorRightLeg": ((2, 12, 0), "leggings", "right_leg"),
    "armorLeftLeg": ((-2, 12, 0), "leggings", "left_leg"),
    "armorRightBoot": ((2, 12, 0), "boots", "right_leg"),
    "armorLeftBoot": ((-2, 12, 0), "boots", "left_leg"),
}
PIECE_ORDER = ["helmet", "chestplate", "leggings", "boots"]


def collect(bb):
    """hueso (grupo armorXxx) -> lista de cubos exportables, incluyendo los de subgrupos."""
    byuuid = {e["uuid"]: e for e in bb["elements"]}
    groups = {g["uuid"]: g for g in bb["groups"]}
    bones = {}

    def descend(node, bucket):
        for ch in node.get("children", []):
            if isinstance(ch, dict):
                descend(ch, bucket)
            elif byuuid[ch].get("export", True):
                bucket.append(byuuid[ch])

    def walk(node):
        name = groups[node["uuid"]]["name"]
        if name in BONES:
            descend(node, bones.setdefault(name, []))
            return
        for ch in node.get("children", []):
            if isinstance(ch, dict):
                walk(ch)

    for n in bb["outliner"]:
        if isinstance(n, dict):
            walk(n)
    return bones


def cube(e, pivot, sx, sy):
    f, t = e["from"], e["to"]
    ff = [round(f[i] - pivot[i] + 8, 4) for i in range(3)]
    tt = [round(t[i] - pivot[i] + 8, 4) for i in range(3)]
    for v in ff + tt:
        assert -16 <= v <= 32, f"{e['name']}: coordenada {v} fuera del rango de un modelo de Minecraft (-16..32)"
    faces = {}
    for d, fc in e["faces"].items():
        u1, v1, u2, v2 = fc["uv"]
        if u1 == u2 or v1 == v2:
            continue  # cara sin área (elementos planos)
        faces[d] = {"uv": [round(u1 * sx, 4), round(v1 * sy, 4), round(u2 * sx, 4), round(v2 * sy, 4)], "texture": "#0"}
    out = {"name": e["name"], "from": ff, "to": tt, "faces": faces}
    rot = e.get("rotation")
    if rot and any(rot):
        o = e.get("origin", [0, 0, 0])
        out["rotation"] = {"origin": [round(o[i] - pivot[i] + 8, 4) for i in range(3)], "x": rot[0], "y": rot[1], "z": rot[2]}
    return out


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--bbmodel", required=True)
    ap.add_argument("--name", required=True, help="prefijo de los ítems, p. ej. light -> light_helmet")
    ap.add_argument("--modid", default="reimaginedtools")
    ap.add_argument("--resources", default="src/main/resources")
    a = ap.parse_args()

    bb = json.load(open(a.bbmodel))
    assets = f"{a.resources}/assets/{a.modid}"
    W, H = bb["resolution"]["width"], bb["resolution"]["height"]
    sx, sy = 16.0 / W, 16.0 / H
    tex = f"{a.modid}:item/{a.name}_armor"
    os.makedirs(f"{assets}/textures/item", exist_ok=True)
    open(f"{assets}/textures/item/{a.name}_armor.png", "wb").write(base64.b64decode(bb["textures"][0]["source"].split(",", 1)[1]))

    def write(path, obj):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        open(path, "w").write(json.dumps(obj, indent=2) + "\n")

    bones = collect(bb)
    pieces = {}
    for group, (pivot, piece, bone) in BONES.items():
        if group not in bones:
            continue
        els = bones[group]
        write(f"{assets}/models/item/{a.name}_{piece}_{bone}.json",
              {"parent": "minecraft:block/block", "textures": {"0": tex, "particle": tex}, "elements": [cube(e, pivot, sx, sy) for e in els]})
        pieces.setdefault(piece, []).append((bone, els))
        print(f"{a.name}_{piece}_{bone}: {len(els)} cubos")

    for piece in PIECE_ORDER:
        if piece not in pieces:
            continue
        allels = [e for _, els in pieces[piece] for e in els]
        lo = [min(e["from"][i] for e in allels) for i in range(3)]
        hi = [max(e["to"][i] for e in allels) for i in range(3)]
        center = [(lo[i] + hi[i]) / 2 for i in range(3)]
        size = max(hi[i] - lo[i] for i in range(3))
        s = round(min(1.2, 10.5 / size), 3)
        write(f"{assets}/models/item/{a.name}_{piece}.json", {
            "parent": "minecraft:block/block", "textures": {"0": tex, "particle": tex},
            "elements": [cube(e, center, sx, sy) for e in allels],
            "display": {
                "gui": {"rotation": [30, 225, 0], "scale": [s, s, s]},
                "ground": {"translation": [0, 3, 0], "scale": [s * .5] * 3},
                "fixed": {"rotation": [0, 180, 0], "scale": [s * .8] * 3},
                "thirdperson_righthand": {"rotation": [75, 45, 0], "translation": [0, 2.5, 0], "scale": [s * .55] * 3},
                "firstperson_righthand": {"rotation": [0, 45, 0], "scale": [s * .6] * 3},
                "firstperson_lefthand": {"rotation": [0, 225, 0], "scale": [s * .6] * 3}}})
        cases = [b for b, _ in pieces[piece]]
        item_name = f"{a.name}_{piece}"
        write(f"{assets}/items/{item_name}.json", {"model": {
            "type": "minecraft:select", "property": "minecraft:custom_model_data", "index": 0,
            "cases": [{"when": b, "model": {"type": "minecraft:model", "model": f"{a.modid}:item/{item_name}_{b}"}} for b in cases],
            "fallback": {"type": "minecraft:model", "model": f"{a.modid}:item/{item_name}"}}})
        print(f"{item_name}: huesos {cases}, tamaño {round(size, 1)}, escala gui {s}")

    write(f"{assets}/equipment/{a.name}.json", {"layers": {"horse_body": [{"texture": f"{a.modid}:{a.name}"}]}})


if __name__ == "__main__":
    main()
