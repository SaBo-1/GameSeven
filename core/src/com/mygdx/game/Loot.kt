package com.mygdx.game

public final data class LootEntry(val item: Item, val weight: Int)

open class LootTable(val entries: List<LootEntry>) {
    fun roll(): Item {
        val totalWeight = entries.sumOf { it.weight }
        val roll = (Math.random() * totalWeight).toInt()
        var runningTotal = 0
        for (entry in entries) {
            runningTotal += entry.weight
            if (roll < runningTotal) {
                return entry.item
            }
        }
        throw RuntimeException("Invalid loot table")
    }
}