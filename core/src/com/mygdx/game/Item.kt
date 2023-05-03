package com.mygdx.game

import java.util.UUID

class Item(
        val id: UUID = UUID.randomUUID(),
        val name: String,
        val type: ItemType,
        val rarity: ItemRarity,
        val statBoosts: Map<StatType, Float>,
        val effects: List<Effect>,
        // ...
)

enum class ItemType { WEAPON, ARMOR, POTION, ACCESSORY }
enum class ItemRarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }
enum class StatType { HEALTH, ATTACK, DEFENSE, CRITICAL_CHANCE, BLOCK_CHANCE }
enum class Effect { DAMAGE, HEAL, STUN, BUFF, DEBUFF }
