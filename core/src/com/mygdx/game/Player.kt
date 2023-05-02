package com.mygdx.game

class Player (
    var name: String,
    var health: Double,
    var commonAttack: Double,
    var commonDefense: Double,
    var money: Double,
    var criticalChance: Int,
    var blockChance: Int,
    var level: Int,
    var experience: Int,
) {

    // Anstatt Null Schaden kommt immer einer durch.
    fun attack (target: Player) {
        val damage = commonAttack - target.commonDefense
        val trueDamage = if (damage > 0) damage else 1.0
    }

    // Level Aufstieg mit Werten.
    fun levelUp () {
        level++
        health += 25.0
        commonAttack += 1
        commonDefense += 1
        experience = 0
    }

    // erhöht
    fun gainExperience (exp: Int) {
        experience += exp
        if (experience >= 100 * (level*0.2)) {
            levelUp()
        }
    }

    fun criticalRoll () {}
    fun blockRoll() {}
    fun getItem () {}
}