package com.mygdx.game

class LootEntry(val item: Item, val weight: Double)

// Das ist die Grundsätzliche Loot funktion.
// Hier wird eine Liste von Loot erstellt.
// die mit Gewichten belegt werden, die Gewichte beziehen sich auf die Drop Wahrscheinlichkeit.
// Im Running Total wird ein Boolean mit 0.0 Festgelegt.
// Die Gewichte werde nin de Main Definiert.
open class LootTable(private val entries: List<LootEntry>) {
    fun roll(): Item {
        val totalWeight = entries.sumOf { it.weight }
        val roll = (Math.random() * totalWeight).toInt()
        var runningTotal = 0.0
        for (entry in entries) {
            runningTotal += entry.weight
            if (roll < runningTotal) {
                return entry.item
            }
        }
        throw RuntimeException("Invalid loot table")
    }
}