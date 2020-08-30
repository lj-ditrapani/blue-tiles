package info.ditrapani.factory

import info.ditrapani.game.Supply
import info.ditrapani.game.Trash
import info.ditrapani.game.getTile
import info.ditrapani.model.Color
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Display(
    var slot1: Color?,
    var slot2: Color?,
    var slot3: Color?,
    var slot4: Color?
) {
    fun isEmpty(): Boolean =
        slot1 == null &&
            slot2 == null &&
            slot3 == null &&
            slot4 == null

    fun reload(supply: Supply, trash: Trash) {
        slot1 = supply.getTile(trash)
        slot2 = supply.getTile(trash)
        slot3 = supply.getTile(trash)
        slot4 = supply.getTile(trash)
    }

    fun take(color: Color): Int {
        var count = 0
        if (slot1 != null && slot1 == color) {
            count += 1
            slot1 = null
        }
        if (slot2 != null && slot2 == color) {
            count += 1
            slot2 = null
        }
        if (slot3 != null && slot3 == color) {
            count += 1
            slot3 = null
        }
        if (slot4 != null && slot4 == color) {
            count += 1
            slot4 = null
        }
        return count
    }

    fun toJson(): JsonObject =
        json {
            obj(
                "slot1" to slot1?.toString(),
                "slot2" to slot2?.toString(),
                "slot3" to slot3?.toString(),
                "slot4" to slot4?.toString()
            )
        }
}

fun newDisplay(supply: Supply, trash: Trash) =
    Display(
        supply.getTile(trash),
        supply.getTile(trash),
        supply.getTile(trash),
        supply.getTile(trash)
    )
