package com.example.model

enum class CharacterBackground(
    val title: String,
    val description: String,
    val bonusStatText: String
) {
    NOBLE_EXILE(
        title = "Noble Exile",
        description = "Banishment stripped your titles, but royal academy combat and lore training remain.",
        bonusStatText = "+100 Starting Gold, +5 Defense"
    ),
    FOREST_HERMIT(
        title = "Forest Hermit",
        description = "Years living alone in wilderness deepened your reflexes and survival instinct.",
        bonusStatText = "+15 Max HP, +3 Speed"
    ),
    STREET_URCHIN(
        title = "Street Urchin",
        description = "Growing up in city alleys taught you cunning resourcefulness and quick wits.",
        bonusStatText = "+5 Attack, +5 Magic"
    )
}

enum class HeroClass(
    val title: String,
    val resourceName: String,
    val description: String,
    val passiveTrait: String,
    val baseHp: Int,
    val baseResource: Int,
    val baseAttack: Int,
    val baseDefense: Int,
    val baseMagic: Int,
    val baseSpeed: Int
) {
    WARRIOR(
        title = "Warrior",
        resourceName = "Stamina",
        description = "Frontline juggernaut relying on raw strength and heavy defensive prowess.",
        passiveTrait = "Heavy Armor Mastery: Defense +15% while wearing armor gear.",
        baseHp = 120,
        baseResource = 60,
        baseAttack = 18,
        baseDefense = 14,
        baseMagic = 5,
        baseSpeed = 10
    ),
    MAGE(
        title = "Mage",
        resourceName = "Mana",
        description = "Master of elemental forces and arcane manipulation.",
        passiveTrait = "Arcane Focus: Magic +15% while no heavy armor equipped.",
        baseHp = 80,
        baseResource = 100,
        baseAttack = 6,
        baseDefense = 6,
        baseMagic = 22,
        baseSpeed = 12
    ),
    ROGUE(
        title = "Rogue",
        resourceName = "Energy",
        description = "Lethal shadow skirmisher specializing in critical strikes and stealth.",
        passiveTrait = "Evasion: 15% chance to fully dodge incoming attacks.",
        baseHp = 95,
        baseResource = 75,
        baseAttack = 16,
        baseDefense = 8,
        baseMagic = 8,
        baseSpeed = 18
    ),
    CLERIC(
        title = "Cleric",
        resourceName = "Faith",
        description = "Devout holy warrior wielding divine restoration and righteous smites.",
        passiveTrait = "Blessed Vitality: Max HP +10%.",
        baseHp = 110,
        baseResource = 80,
        baseAttack = 12,
        baseDefense = 12,
        baseMagic = 16,
        baseSpeed = 9
    )
}

enum class Subclass(
    val title: String,
    val parentClass: HeroClass,
    val description: String
) {
    BERSERKER("Berserker", HeroClass.WARRIOR, "Rage and raw offense, trading defense for devastating blows."),
    GUARDIAN("Guardian", HeroClass.WARRIOR, "Impenetrable fortress focused on protection and retaliation."),
    ELEMENTALIST("Elementalist", HeroClass.MAGE, "Wields fire, ice, and thunder to scorch and freeze foes."),
    ARCANIST("Arcanist", HeroClass.MAGE, "Manipulates time, enemy mana, and battlefield control."),
    ASSASSIN("Assassin", HeroClass.ROGUE, "Deadly burst damage, guaranteed crits, and lethal poisons."),
    TRICKSTER("Trickster", HeroClass.ROGUE, "Utility expert, gold plunderer, and untargetable trickery."),
    PALADIN("Paladin", HeroClass.CLERIC, "Hybrid holy knight with smiting strikes and divine shields."),
    PRIEST("Priest", HeroClass.CLERIC, "Pure holy conduit delivering powerful sustained healing and resurrects.")
}

enum class SkillEffectType {
    DAMAGE_PHYSICAL,
    DAMAGE_MAGIC,
    HEAL,
    BUFF_DEFENSE,
    BUFF_DODGE,
    RECKLESS_SWING,
    TAUNT,
    RETALIATE,
    FORTIFY,
    LAST_STAND,
    FROST_BOLT,
    CHAIN_LIGHTNING,
    INFERNO,
    METEOR,
    SLOW,
    MANA_DRAIN,
    SILENCE,
    TIME_WARP,
    BACKSTAB,
    POISON_BLADE,
    SHADOW_STEP,
    DEATH_MARK,
    DISTRACT,
    STEAL,
    VANISH,
    HOLY_STRIKE,
    DIVINE_SHIELD,
    CONSECRATE,
    DIVINE_JUDGMENT,
    GREATER_HEAL,
    REGENERATION,
    PURIFY,
    RESURRECTION_PRAYER,
    FRENZY
}

data class Skill(
    val id: String,
    val name: String,
    val levelRequired: Int,
    val resourceCost: Int,
    val cooldown: Int,
    val effectType: SkillEffectType,
    val effectValue: Double,
    val description: String,
    val parentClass: HeroClass,
    val subclass: Subclass? = null
)

enum class ItemType {
    WEAPON,
    ARMOR,
    POTION,
    QUEST
}

data class Item(
    val id: String,
    val name: String,
    val type: ItemType,
    val attackBonus: Int = 0,
    val defenseBonus: Int = 0,
    val magicBonus: Int = 0,
    val hpRestore: Int = 0,
    val resourceRestore: Int = 0,
    val value: Int = 0,
    val description: String
)

data class ItemQuantity(
    val item: Item,
    val quantity: Int
)

data class Enemy(
    val id: String,
    val name: String,
    val regionId: String,
    val maxHp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val xpReward: Int,
    val goldReward: Int,
    val dropItemId: String? = null,
    val description: String,
    val specialAbilityName: String? = null
)

data class DialogueChoice(
    val id: String,
    val text: String,
    val nextNodeId: String? = null,
    val requiredQuestStep: Pair<String, Int>? = null, // questId -> minStep
    val setQuestStep: Pair<String, Int>? = null,      // questId -> newStep
    val rewardGold: Int = 0,
    val rewardItemId: String? = null,
    val rewardXp: Int = 0,
    val rewardFlagKey: String? = null,                // prevent duplicate reward check
    val openShop: Boolean = false,
    val startCombatEnemyId: String? = null,
    val changeLocationId: String? = null
)

data class DialogueNode(
    val id: String,
    val speaker: String,
    val text: String,
    val choices: List<DialogueChoice>
)

data class Npc(
    val id: String,
    val name: String,
    val title: String,
    val regionId: String,
    val initialNodeId: String,
    val dialogueNodes: Map<String, DialogueNode>,
    val shopInventory: List<Item> = emptyList()
)

data class Location(
    val id: String,
    val name: String,
    val regionId: String,
    val description: String,
    val bgImageResName: String,
    val npcs: List<Npc> = emptyList(),
    val availableEnemyIds: List<String> = emptyList(),
    val connectingLocationIds: List<String> = emptyList()
)

data class Region(
    val id: String,
    val name: String,
    val description: String,
    val bgImageResName: String,
    val locations: List<Location>
)

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val regionId: String,
    val maxSteps: Int,
    val stepDescriptions: Map<Int, String>
)

data class CombatStatus(
    val inCombat: Boolean = false,
    val enemy: Enemy? = null,
    val currentEnemyHp: Int = 0,
    val turnNumber: Int = 1,
    val playerDefending: Boolean = false,
    val guardStanceTurns: Int = 0,
    val enemyStunned: Boolean = false,
    val enemySlowedTurns: Int = 0,
    val enemyBurnTurns: Int = 0,
    val enemyPoisonTurns: Int = 0,
    val enemyMarkedTurns: Int = 0,
    val divineShieldActive: Boolean = false,
    val vanishTurns: Int = 0,
    val retaliateActive: Boolean = false,
    val elementalOverloadStack: Int = 0,
    val shadowStepActive: Boolean = false,
    val righteousFuryUsed: Boolean = false,
    val resurrectionUsed: Boolean = false,
    val lastStandUsed: Boolean = false,
    val combatLog: List<String> = emptyList()
)

data class GameState(
    val characterName: String = "",
    val background: CharacterBackground? = null,
    val heroClass: HeroClass? = null,
    val subclass: Subclass? = null,
    val level: Int = 1,
    val xp: Int = 0,
    val xpToNextLevel: Int = 100,
    val gold: Int = 50,
    val hp: Int = 100,
    val maxHp: Int = 100,
    val resource: Int = 50,
    val maxResource: Int = 50,
    val attack: Int = 10,
    val defense: Int = 10,
    val magic: Int = 10,
    val speed: Int = 10,
    val equippedWeapon: Item? = null,
    val equippedArmor: Item? = null,
    val inventory: List<ItemQuantity> = emptyList(),
    val currentLocationId: String = "verdant_grove",
    val currentDialogueNpcId: String? = null,
    val currentDialogueNodeId: String? = null,
    val questProgress: Map<String, Int> = emptyMap(), // questId -> step
    val grantedRewardFlags: Set<String> = emptySet(), // duplicate-prevention flags!
    val npcFlags: Map<String, Boolean> = emptyMap(),
    val skillCooldowns: Map<String, Int> = emptyMap(),
    val unlockedSkillIds: Set<String> = emptySet(),
    val combatState: CombatStatus = CombatStatus(),
    val narrativeLog: List<String> = emptyList(),
    val activeTab: String = "MAIN", // MAIN, INVENTORY, SKILLS, QUESTS, SHOP
    val isActionProcessing: Boolean = false,
    val pendingSubclassChoice: Boolean = false,
    val gameEnded: Boolean = false
)
