package com.example.data

import com.example.model.*

object GameData {

    // --- SKILLS ---
    val ALL_SKILLS = listOf(
        // WARRIOR
        Skill("power_strike", "Power Strike", 1, 10, 1, SkillEffectType.DAMAGE_PHYSICAL, 1.5, "Deals 150% Attack physical damage.", HeroClass.WARRIOR),
        Skill("guard_stance", "Guard Stance", 1, 5, 2, SkillEffectType.BUFF_DEFENSE, 0.5, "+50% Defense for 2 turns.", HeroClass.WARRIOR),
        // Berserker
        Skill("reckless_swing", "Reckless Swing", 3, 15, 0, SkillEffectType.RECKLESS_SWING, 2.0, "200% Attack damage, but self-inflicts 10% of damage dealt.", HeroClass.WARRIOR, Subclass.BERSERKER),
        Skill("bloodlust", "Bloodlust", 5, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.0, "Passive: +5% Attack for each 10% HP missing (max +30%).", HeroClass.WARRIOR, Subclass.BERSERKER),
        Skill("unstoppable", "Unstoppable", 7, 25, 4, SkillEffectType.BUFF_DEFENSE, 0.0, "Immune to stun and status debuffs for 2 turns.", HeroClass.WARRIOR, Subclass.BERSERKER),
        Skill("execute", "Execute", 9, 20, 3, SkillEffectType.DAMAGE_PHYSICAL, 3.0, "300% Attack damage to enemies below 25% HP, otherwise 100%.", HeroClass.WARRIOR, Subclass.BERSERKER),
        Skill("frenzy", "Frenzy", 11, 40, 6, SkillEffectType.FRENZY, 0.0, "Take two full actions this turn.", HeroClass.WARRIOR, Subclass.BERSERKER),
        // Guardian
        Skill("taunt", "Taunt", 3, 10, 2, SkillEffectType.TAUNT, 0.0, "Forces the current enemy to target you next turn.", HeroClass.WARRIOR, Subclass.GUARDIAN),
        Skill("shield_wall", "Shield Wall", 5, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.2, "Passive: Damage taken reduced by 20% when HP is below 50%.", HeroClass.WARRIOR, Subclass.GUARDIAN),
        Skill("retaliate", "Retaliate", 7, 15, 3, SkillEffectType.RETALIATE, 0.5, "Next hit taken this turn is reflected at 50% back to attacker.", HeroClass.WARRIOR, Subclass.GUARDIAN),
        Skill("fortify", "Fortify", 9, 20, 4, SkillEffectType.FORTIFY, 0.4, "+40% Defense and +15% Max HP for 3 turns.", HeroClass.WARRIOR, Subclass.GUARDIAN),
        Skill("last_stand", "Last Stand", 11, 30, 0, SkillEffectType.LAST_STAND, 0.0, "Once per battle, survive a lethal hit with 1 HP instead.", HeroClass.WARRIOR, Subclass.GUARDIAN),

        // MAGE
        Skill("firebolt", "Firebolt", 1, 10, 1, SkillEffectType.DAMAGE_MAGIC, 1.4, "Deals 140% Magic magic damage.", HeroClass.MAGE),
        Skill("mana_shield", "Mana Shield", 1, 8, 3, SkillEffectType.BUFF_DEFENSE, 1.0, "Absorbs incoming damage up to 100% Magic stat.", HeroClass.MAGE),
        // Elementalist
        Skill("frost_bolt", "Frost Bolt", 3, 12, 1, SkillEffectType.FROST_BOLT, 1.3, "130% Magic damage + 30% chance to reduce enemy Speed by 20% for 2 turns.", HeroClass.MAGE, Subclass.ELEMENTALIST),
        Skill("chain_lightning", "Chain Lightning", 5, 20, 3, SkillEffectType.CHAIN_LIGHTNING, 1.0, "Strikes enemy with surging lightning for 100% Magic damage.", HeroClass.MAGE, Subclass.ELEMENTALIST),
        Skill("inferno", "Inferno", 7, 18, 3, SkillEffectType.INFERNO, 0.10, "Applies burn dealing 10% Magic damage per turn for 3 turns.", HeroClass.MAGE, Subclass.ELEMENTALIST),
        Skill("elemental_overload", "Elemental Overload", 9, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.05, "Passive: Each elemental skill used increases elemental damage by 5% (max +25%).", HeroClass.MAGE, Subclass.ELEMENTALIST),
        Skill("meteor", "Meteor", 11, 40, 5, SkillEffectType.METEOR, 3.5, "Summons a apocalyptic meteor dealing 350% Magic damage.", HeroClass.MAGE, Subclass.ELEMENTALIST),
        // Arcanist
        Skill("slow", "Slow", 3, 10, 2, SkillEffectType.SLOW, 0.3, "Reduces enemy Speed by 30% for 2 turns.", HeroClass.MAGE, Subclass.ARCANIST),
        Skill("mana_drain", "Mana Drain", 5, 10, 2, SkillEffectType.MANA_DRAIN, 1.0, "Deals 100% Magic damage and restores 50% of damage dealt as Mana.", HeroClass.MAGE, Subclass.ARCANIST),
        Skill("silence", "Silence", 7, 15, 4, SkillEffectType.SILENCE, 0.0, "Prevents enemy special abilities for 2 turns.", HeroClass.MAGE, Subclass.ARCANIST),
        Skill("arcane_insight", "Arcane Insight", 9, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.15, "Passive: 15% chance any Mage skill costs 0 Mana.", HeroClass.MAGE, Subclass.ARCANIST),
        Skill("time_warp", "Time Warp", 11, 35, 6, SkillEffectType.TIME_WARP, 0.0, "Manipulate time to take an extra turn immediately.", HeroClass.MAGE, Subclass.ARCANIST),

        // ROGUE
        Skill("quick_strike", "Quick Strike", 1, 8, 0, SkillEffectType.DAMAGE_PHYSICAL, 1.3, "Rapid blade strike dealing 130% Attack physical damage.", HeroClass.ROGUE),
        Skill("smoke_bomb", "Smoke Bomb", 1, 10, 3, SkillEffectType.BUFF_DODGE, 0.30, "+30% dodge chance for 2 turns.", HeroClass.ROGUE),
        // Assassin
        Skill("backstab", "Backstab", 3, 12, 2, SkillEffectType.BACKSTAB, 1.8, "180% Attack damage (+50% bonus if used as first action of combat).", HeroClass.ROGUE, Subclass.ASSASSIN),
        Skill("critical_focus", "Critical Focus", 5, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.15, "Passive: Crit chance +15% (Base 10% -> 25%).", HeroClass.ROGUE, Subclass.ASSASSIN),
        Skill("poison_blade", "Poison Blade", 7, 10, 3, SkillEffectType.POISON_BLADE, 1.2, "120% Attack damage + poison dealing 8% enemy max HP/turn for 3 turns.", HeroClass.ROGUE, Subclass.ASSASSIN),
        Skill("shadow_step", "Shadow Step", 9, 15, 4, SkillEffectType.SHADOW_STEP, 0.0, "Guarantees your next attack is a critical hit.", HeroClass.ROGUE, Subclass.ASSASSIN),
        Skill("death_mark", "Death Mark", 11, 30, 5, SkillEffectType.DEATH_MARK, 0.25, "Marks enemy: +25% damage from all attacks for 3 turns.", HeroClass.ROGUE, Subclass.ASSASSIN),
        // Trickster
        Skill("distract", "Distract", 3, 10, 4, SkillEffectType.DISTRACT, 0.0, "Forces enemy to skip their next turn (1 use per battle).", HeroClass.ROGUE, Subclass.TRICKSTER),
        Skill("steal", "Steal", 5, 8, 2, SkillEffectType.STEAL, 0.8, "80% Attack damage + chance to steal bonus gold this battle.", HeroClass.ROGUE, Subclass.TRICKSTER),
        Skill("lucky_streak", "Lucky Streak", 7, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.20, "Passive: +20% gold earned from all battle victories.", HeroClass.ROGUE, Subclass.TRICKSTER),
        Skill("vanish", "Vanish", 9, 15, 4, SkillEffectType.VANISH, 0.0, "Dissolve into shadows to become untargetable for 1 turn.", HeroClass.ROGUE, Subclass.TRICKSTER),
        Skill("ambush_master", "Ambush Master", 11, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.0, "Passive: Initiated combats grant +1 free action at battle start.", HeroClass.ROGUE, Subclass.TRICKSTER),

        // CLERIC
        Skill("heal", "Heal", 1, 10, 1, SkillEffectType.HEAL, 1.2, "Restores 120% Magic as HP.", HeroClass.CLERIC),
        Skill("smite", "Smite", 1, 12, 0, SkillEffectType.DAMAGE_MAGIC, 1.1, "Calls holy radiance dealing 110% Magic magic damage.", HeroClass.CLERIC),
        // Paladin
        Skill("holy_strike", "Holy Strike", 3, 12, 2, SkillEffectType.HOLY_STRIKE, 1.4, "140% Attack damage + heals self for 20% of damage dealt.", HeroClass.CLERIC, Subclass.PALADIN),
        Skill("divine_shield", "Divine Shield", 5, 15, 3, SkillEffectType.DIVINE_SHIELD, 0.0, "Negates the next instance of incoming damage entirely.", HeroClass.CLERIC, Subclass.PALADIN),
        Skill("righteous_fury", "Righteous Fury", 7, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.0, "Passive: When HP drops below 30%, next skill costs 0 Faith (1 per battle).", HeroClass.CLERIC, Subclass.PALADIN),
        Skill("consecrate", "Consecrate", 9, 20, 3, SkillEffectType.CONSECRATE, 1.0, "Deals 100% Magic damage & heals self 100% Magic simultaneously.", HeroClass.CLERIC, Subclass.PALADIN),
        Skill("divine_judgment", "Divine Judgment", 11, 35, 5, SkillEffectType.DIVINE_JUDGMENT, 2.5, "250% Magic damage, and fully heals self if this kills the enemy.", HeroClass.CLERIC, Subclass.PALADIN),
        // Priest
        Skill("greater_heal", "Greater Heal", 3, 18, 3, SkillEffectType.GREATER_HEAL, 2.0, "Restores 200% Magic as HP.", HeroClass.CLERIC, Subclass.PRIEST),
        Skill("regeneration", "Regeneration", 5, 12, 3, SkillEffectType.REGENERATION, 0.10, "Heals 10% max HP per turn for 3 turns.", HeroClass.CLERIC, Subclass.PRIEST),
        Skill("purify", "Purify", 10, 10, 3, SkillEffectType.PURIFY, 0.0, "Removes all negative status effects (poison, burn, slow, silence).", HeroClass.CLERIC, Subclass.PRIEST),
        Skill("faithful_recovery", "Faithful Recovery", 9, 0, 0, SkillEffectType.BUFF_DEFENSE, 0.20, "Passive: Faith regenerates 20% faster between turns.", HeroClass.CLERIC, Subclass.PRIEST),
        Skill("resurrection_prayer", "Resurrection Prayer", 11, 40, 0, SkillEffectType.RESURRECTION_PRAYER, 0.5, "Once per battle, if reduced below 1 HP, heal to 50% max HP.", HeroClass.CLERIC, Subclass.PRIEST)
    )

    // --- ITEMS ---
    val ITEMS = mapOf(
        "iron_sword" to Item("iron_sword", "Iron Sword", ItemType.WEAPON, attackBonus = 8, value = 40, description = "A sturdy steel-forged blade."),
        "desert_scimitar" to Item("desert_scimitar", "Desert Scimitar", ItemType.WEAPON, attackBonus = 18, magicBonus = 4, value = 120, description = "A curved blade forged with Sunfire ore."),
        "shadow_blade" to Item("shadow_blade", "Shadow Blade", ItemType.WEAPON, attackBonus = 32, magicBonus = 8, value = 250, description = "An obsidian dagger humming with shadowy energies."),
        "holy_mace" to Item("holy_mace", "Holy Mace", ItemType.WEAPON, attackBonus = 16, magicBonus = 14, value = 150, description = "A silver mace blessed by high priests."),

        "leather_armor" to Item("leather_armor", "Leather Armor", ItemType.ARMOR, defenseBonus = 6, value = 35, description = "Tanned hide providing light agility and basic protection."),
        "sandstone_plate" to Item("sandstone_plate", "Sandstone Plate", ItemType.ARMOR, defenseBonus = 16, value = 110, description = "Heavy desert armor enchanted with enduring stone."),
        "shadow_mail" to Item("shadow_mail", "Shadow Mail", ItemType.ARMOR, defenseBonus = 28, value = 220, description = "Forged in catacomb iron, absorbing dark blows."),

        "health_potion" to Item("health_potion", "Health Potion", ItemType.POTION, hpRestore = 50, value = 20, description = "Restores 50 HP."),
        "mana_potion" to Item("mana_potion", "Mana Potion", ItemType.POTION, resourceRestore = 40, value = 20, description = "Restores 40 Mana/Energy/Faith/Stamina."),
        "elixir_of_life" to Item("elixir_of_life", "Elixir of Life", ItemType.POTION, hpRestore = 100, resourceRestore = 60, value = 60, description = "Fully revitalizes body and spirit."),

        "sunstone_key" to Item("sunstone_key", "Sunstone Key", ItemType.QUEST, value = 0, description = "Ancient key carved with desert runes. Unlocks the Sunfire Tombs."),
        "ancient_scroll" to Item("ancient_scroll", "Ancient Scroll", ItemType.QUEST, value = 0, description = "Contains secret weakness of Shadow Lord Malakor."),
        "dragon_scale" to Item("dragon_scale", "Dragon Scale", ItemType.QUEST, value = 150, description = "Glistening red dragon scale of immense trade value.")
    )

    // --- ENEMIES ---
    val ENEMIES = mapOf(
        "dire_wolf" to Enemy("dire_wolf", "Dire Wolf", "whispering_woods", maxHp = 70, attack = 14, defense = 6, speed = 11, xpReward = 50, goldReward = 30, dropItemId = "leather_armor", description = "A ferocious predator with glowing crimson eyes."),
        "anubis_sentinel" to Enemy("anubis_sentinel", "Anubis Sentinel", "sunfire_desert", maxHp = 140, attack = 24, defense = 14, speed = 12, xpReward = 110, goldReward = 70, dropItemId = "sandstone_plate", description = "An ancient jackal guardian constructed of enchanted desert sandstone."),
        "skeleton_warrior" to Enemy("skeleton_warrior", "Skeleton Warrior", "obsidian_catacombs", maxHp = 180, attack = 30, defense = 18, speed = 10, xpReward = 150, goldReward = 90, dropItemId = "shadow_blade", description = "A skeletal knight clad in rusted armor wielding a phantom broadsword."),
        "malakor_boss" to Enemy("malakor_boss", "Shadow Lord Malakor", "obsidian_catacombs", maxHp = 360, attack = 40, defense = 22, speed = 15, xpReward = 350, goldReward = 300, dropItemId = "dragon_scale", description = "The tyrant ruler of shadows threatening to consume the realms in dark vortexes!")
    )

    // --- QUESTS ---
    val QUESTS = mapOf(
        "q_wolf_pack" to Quest(
            id = "q_wolf_pack",
            title = "The Wolf Pack Threat",
            description = "Eldrin asked you to cleanse the overgrown shrine of the dire wolf terrorizing travelers.",
            regionId = "whispering_woods",
            maxSteps = 3,
            stepDescriptions = mapOf(
                1 to "Talk to Eldrin in Verdant Grove.",
                2 to "Defeat the Dire Wolf at the Overgrown Shrine.",
                3 to "Report victory back to Eldrin for your bounty!"
            )
        ),
        "q_herbal_alchemy" to Quest(
            id = "q_herbal_alchemy",
            title = "Herbal Alchemy",
            description = "Maeve the Herbalist needs wolf tooth samples to distill rare medicinal tinctures.",
            regionId = "whispering_woods",
            maxSteps = 2,
            stepDescriptions = mapOf(
                1 to "Defeat a Dire Wolf to gather ingredients.",
                2 to "Return to Maeve at Sylvan Tavern for rare potions."
            )
        ),
        "q_sunstone_key" to Quest(
            id = "q_sunstone_key",
            title = "The Lost Sunstone Key",
            description = "Scholar Tariq needs the Sunstone Key to open the ancient Sunfire Tombs.",
            regionId = "sunfire_desert",
            maxSteps = 3,
            stepDescriptions = mapOf(
                1 to "Find the Sunstone Key in the ancient hollow.",
                2 to "Bring the Sunstone Key to Tariq at Caravan Bazaar.",
                3 to "Use the unlocked gate to access the Sunfire Tombs."
            )
        ),
        "q_caravan_supplies" to Quest(
            id = "q_caravan_supplies",
            title = "Caravan Supplies",
            description = "Merchant Rashid needs financial backing or rare goods to restock desert arms.",
            regionId = "sunfire_desert",
            maxSteps = 2,
            stepDescriptions = mapOf(
                1 to "Purchase or acquire a Sunfire weapon or trade 100 Gold.",
                2 to "Receive Rashid's special Merchant Master discount."
            )
        ),
        "q_fallen_knight" to Quest(
            id = "q_fallen_knight",
            title = "The Fallen Guardian",
            description = "The Ghost of Sir Gareth seeks peace by banishing the dark magic over the crypt.",
            regionId = "obsidian_catacombs",
            maxSteps = 2,
            stepDescriptions = mapOf(
                1 to "Defeat the Skeleton Warrior in Hall of Shadows.",
                2 to "Return to Sir Gareth's ghost to receive the Ancient Scroll."
            )
        ),
        "q_defeat_malakor" to Quest(
            id = "q_defeat_malakor",
            title = "Banish the Shadow Lord",
            description = "The main prophecy: Journey through the catacombs and vanquish Shadow Lord Malakor!",
            regionId = "obsidian_catacombs",
            maxSteps = 4,
            stepDescriptions = mapOf(
                1 to "Journey to the Sunfire Desert and enter the Catacombs.",
                2 to "Obtain the Ancient Scroll from Sir Gareth.",
                3 to "Confront Shadow Lord Malakor in his Sanctum.",
                4 to "Vanquish Malakor and save the realm of Valor!"
            )
        )
    )

    // --- REGIONS & LOCATIONS WITH DIALOGUE TREES ---
    val REGIONS = listOf(
        Region(
            id = "whispering_woods",
            name = "Whispering Woods",
            description = "Dense emerald canopy filled with ancient oak trees, glowing fireflies, and ancient ruins.",
            bgImageResName = "img_forest",
            locations = listOf(
                Location(
                    id = "verdant_grove",
                    name = "Verdant Grove",
                    regionId = "whispering_woods",
                    description = "A peaceful sunlit glade where ranger Eldrin keeps watch over the forest boundary.",
                    bgImageResName = "img_forest",
                    npcs = listOf(
                        Npc(
                            id = "eldrin",
                            name = "Eldrin",
                            title = "Ranger Sentinel",
                            regionId = "whispering_woods",
                            initialNodeId = "eldrin_start",
                            dialogueNodes = mapOf(
                                "eldrin_start" to DialogueNode(
                                    id = "eldrin_start",
                                    speaker = "Eldrin",
                                    text = "Welcome traveler. Dire beasts stalk the Overgrown Shrine to the east. Will you help protect the grove?",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "eldrin_accept_quest",
                                            text = "[Accept Quest] 'I will hunt down the beast.'",
                                            nextNodeId = "eldrin_accepted",
                                            setQuestStep = Pair("q_wolf_pack", 2),
                                            rewardGold = 20,
                                            rewardXp = 25,
                                            rewardFlagKey = "eldrin_accept_reward"
                                        ),
                                        DialogueChoice(
                                            id = "eldrin_ask_lore",
                                            text = "'Tell me about the realms beyond this forest.'",
                                            nextNodeId = "eldrin_lore"
                                        ),
                                        DialogueChoice(
                                            id = "eldrin_travel_tavern",
                                            text = "'Where can I rest and buy supplies?'",
                                            nextNodeId = "eldrin_tavern_info",
                                            changeLocationId = "sylvan_tavern"
                                        )
                                    )
                                ),
                                "eldrin_accepted" to DialogueNode(
                                    id = "eldrin_accepted",
                                    speaker = "Eldrin",
                                    text = "May the stars guide your blade! Head to the Overgrown Shrine and vanquish the Dire Wolf. Here is a small advance stipend.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "eldrin_to_shrine",
                                            text = "[Move] Travel to the Overgrown Shrine",
                                            changeLocationId = "overgrown_shrine"
                                        ),
                                        DialogueChoice(
                                            id = "eldrin_to_tavern_2",
                                            text = "[Move] Visit Sylvan Tavern first",
                                            changeLocationId = "sylvan_tavern"
                                        )
                                    )
                                ),
                                "eldrin_lore" to DialogueNode(
                                    id = "eldrin_lore",
                                    speaker = "Eldrin",
                                    text = "To the south lies the scorching Sunfire Desert, and beneath its dunes rest the Obsidian Catacombs where Lord Malakor gathers shadow armies.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "eldrin_lore_back",
                                            text = "'I am ready to help with the wolf.'",
                                            nextNodeId = "eldrin_accepted",
                                            setQuestStep = Pair("q_wolf_pack", 2),
                                            rewardGold = 20,
                                            rewardXp = 25,
                                            rewardFlagKey = "eldrin_accept_reward"
                                        )
                                    )
                                ),
                                "eldrin_tavern_info" to DialogueNode(
                                    id = "eldrin_tavern_info",
                                    speaker = "Eldrin",
                                    text = "Maeve runs the Sylvan Tavern nearby. She brews potent remedies and trades herbs.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "eldrin_go_tavern_direct",
                                            text = "Walk into Sylvan Tavern",
                                            changeLocationId = "sylvan_tavern"
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    connectingLocationIds = listOf("sylvan_tavern", "overgrown_shrine", "ancient_hollow", "sunstone_oasis")
                ),
                Location(
                    id = "sylvan_tavern",
                    name = "Sylvan Tavern",
                    regionId = "whispering_woods",
                    description = "A warm, candlelit wooden inn smelling of spiced ale and burning hearth timber.",
                    bgImageResName = "img_tavern",
                    npcs = listOf(
                        Npc(
                            id = "maeve",
                            name = "Maeve",
                            title = "Herbal Alchemist",
                            regionId = "whispering_woods",
                            initialNodeId = "maeve_start",
                            dialogueNodes = mapOf(
                                "maeve_start" to DialogueNode(
                                    id = "maeve_start",
                                    speaker = "Maeve",
                                    text = "Welcome to the Sylvan Tavern! Looking for health potions, sturdy leather gear, or a warm meal?",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "maeve_shop",
                                            text = "[Open Shop] 'Show me your wares, Maeve.'",
                                            openShop = true
                                        ),
                                        DialogueChoice(
                                            id = "maeve_quest",
                                            text = "'Do you need help with alchemy ingredients?'",
                                            nextNodeId = "maeve_quest_node",
                                            setQuestStep = Pair("q_herbal_alchemy", 1)
                                        ),
                                        DialogueChoice(
                                            id = "maeve_rest",
                                            text = "[Rest - 15 Gold] Fully restore HP and Mana",
                                            nextNodeId = "maeve_rested",
                                            rewardGold = -15,
                                            rewardFlagKey = "maeve_rest_heal"
                                        )
                                    )
                                ),
                                "maeve_quest_node" to DialogueNode(
                                    id = "maeve_quest_node",
                                    speaker = "Maeve",
                                    text = "Indeed! Bring me proof of defeating a dire wolf or skeleton, and I will reward you with 2 Health Potions and 50 XP!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "maeve_back",
                                            text = "'I will keep an eye out.'",
                                            nextNodeId = "maeve_start"
                                        )
                                    )
                                ),
                                "maeve_rested" to DialogueNode(
                                    id = "maeve_rested",
                                    speaker = "Maeve",
                                    text = "You sleep peacefully by the hearth. Your strength and magical energies are fully restored!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "maeve_rest_done",
                                            text = "'Thank you, Maeve.'",
                                            nextNodeId = "maeve_start"
                                        )
                                    )
                                )
                            ),
                            shopInventory = listOf(
                                ITEMS["health_potion"]!!,
                                ITEMS["mana_potion"]!!,
                                ITEMS["iron_sword"]!!,
                                ITEMS["leather_armor"]!!
                            )
                        )
                    ),
                    connectingLocationIds = listOf("verdant_grove", "overgrown_shrine")
                ),
                Location(
                    id = "overgrown_shrine",
                    name = "Overgrown Shrine",
                    regionId = "whispering_woods",
                    description = "Crumbling stone pillars wrapped in vines. Low growls echo from the shadow of the shrine.",
                    bgImageResName = "img_forest",
                    availableEnemyIds = listOf("dire_wolf"),
                    connectingLocationIds = listOf("verdant_grove", "ancient_hollow")
                ),
                Location(
                    id = "ancient_hollow",
                    name = "Ancient Hollow",
                    regionId = "whispering_woods",
                    description = "A cavern beneath a giant banyan tree. A glowing chest rests on an ancient stone pedestal.",
                    bgImageResName = "img_forest",
                    npcs = listOf(
                        Npc(
                            id = "ancient_chest_npc",
                            name = "Runed Chest",
                            title = "Ancient Artifact",
                            regionId = "whispering_woods",
                            initialNodeId = "chest_start",
                            dialogueNodes = mapOf(
                                "chest_start" to DialogueNode(
                                    id = "chest_start",
                                    speaker = "Runed Chest",
                                    text = "The ancient chest hums with golden desert energy.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "open_chest_sunstone",
                                            text = "[Open Chest] Take the Sunstone Key and 50 Gold",
                                            nextNodeId = "chest_opened",
                                            rewardItemId = "sunstone_key",
                                            rewardGold = 50,
                                            rewardXp = 40,
                                            rewardFlagKey = "chest_sunstone_opened",
                                            setQuestStep = Pair("q_sunstone_key", 2)
                                        )
                                    )
                                ),
                                "chest_opened" to DialogueNode(
                                    id = "chest_opened",
                                    speaker = "Runed Chest",
                                    text = "The chest stands empty. You hold the Sunstone Key!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "chest_leave",
                                            text = "Leave the hollow",
                                            changeLocationId = "verdant_grove"
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    connectingLocationIds = listOf("verdant_grove", "overgrown_shrine")
                )
            )
        ),

        // REGION 2: SUNFIRE DESERT
        Region(
            id = "sunfire_desert",
            name = "Sunfire Desert",
            description = "Vast golden dunes under a blazing sun, ancient sandstone temples, and bustling desert caravans.",
            bgImageResName = "img_desert",
            locations = listOf(
                Location(
                    id = "sunstone_oasis",
                    name = "Sunstone Oasis",
                    regionId = "sunfire_desert",
                    description = "A lush oasis surrounded by palm trees, sparkling waters, and desert merchants.",
                    bgImageResName = "img_desert",
                    npcs = listOf(
                        Npc(
                            id = "rashid",
                            name = "Merchant Rashid",
                            title = "Caravan Master",
                            regionId = "sunfire_desert",
                            initialNodeId = "rashid_start",
                            dialogueNodes = mapOf(
                                "rashid_start" to DialogueNode(
                                    id = "rashid_start",
                                    speaker = "Rashid",
                                    text = "Ahlan! I trade finest scimitars, sandstone armor, and rare elixirs in all the sands!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "rashid_shop",
                                            text = "[Open Shop] Browse Rashid's Rare Goods",
                                            openShop = true
                                        ),
                                        DialogueChoice(
                                            id = "rashid_quest",
                                            text = "'Are you looking for trade investments?'",
                                            nextNodeId = "rashid_invest",
                                            setQuestStep = Pair("q_caravan_supplies", 1)
                                        )
                                    )
                                ),
                                "rashid_invest" to DialogueNode(
                                    id = "rashid_invest",
                                    speaker = "Rashid",
                                    text = "Invest 100 gold in my caravan, and I shall gift you an Elixir of Life and 100 XP!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "rashid_pay_invest",
                                            text = "[Pay 100 Gold] Invest in Rashid's Trade",
                                            nextNodeId = "rashid_invested",
                                            rewardGold = -100,
                                            rewardItemId = "elixir_of_life",
                                            rewardXp = 100,
                                            rewardFlagKey = "rashid_invested_flag",
                                            setQuestStep = Pair("q_caravan_supplies", 2)
                                        ),
                                        DialogueChoice(
                                            id = "rashid_back",
                                            text = "'Not right now.'",
                                            nextNodeId = "rashid_start"
                                        )
                                    )
                                ),
                                "rashid_invested" to DialogueNode(
                                    id = "rashid_invested",
                                    speaker = "Rashid",
                                    text = "May fortune smile upon our alliance! Here is your Elixir of Life.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "rashid_done",
                                            text = "'Farewell, Rashid.'",
                                            nextNodeId = "rashid_start"
                                        )
                                    )
                                )
                            ),
                            shopInventory = listOf(
                                ITEMS["desert_scimitar"]!!,
                                ITEMS["sandstone_plate"]!!,
                                ITEMS["elixir_of_life"]!!,
                                ITEMS["health_potion"]!!,
                                ITEMS["mana_potion"]!!
                            )
                        )
                    ),
                    connectingLocationIds = listOf("caravan_bazaar", "sunfire_tombs", "verdant_grove")
                ),
                Location(
                    id = "caravan_bazaar",
                    name = "Caravan Bazaar",
                    regionId = "sunfire_desert",
                    description = "Merchant tents filled with silks, spice racks, and scholars translating ancient papyrus.",
                    bgImageResName = "img_desert",
                    npcs = listOf(
                        Npc(
                            id = "tariq",
                            name = "Scholar Tariq",
                            title = "Desert Historian",
                            regionId = "sunfire_desert",
                            initialNodeId = "tariq_start",
                            dialogueNodes = mapOf(
                                "tariq_start" to DialogueNode(
                                    id = "tariq_start",
                                    speaker = "Scholar Tariq",
                                    text = "The Sunfire Tombs hold the entryway to Malakor's catacombs, but require the Sunstone Key from the ancient hollow in Whispering Woods.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "tariq_give_key",
                                            text = "[Give Sunstone Key] 'I have found the key!'",
                                            nextNodeId = "tariq_key_received",
                                            requiredQuestStep = Pair("q_sunstone_key", 2),
                                            setQuestStep = Pair("q_sunstone_key", 3),
                                            rewardGold = 75,
                                            rewardXp = 80,
                                            rewardFlagKey = "tariq_key_given_reward",
                                            changeLocationId = "sunfire_tombs"
                                        ),
                                        DialogueChoice(
                                            id = "tariq_ask_catacombs",
                                            text = "'What waits inside the Catacombs?'",
                                            nextNodeId = "tariq_lore"
                                        )
                                    )
                                ),
                                "tariq_key_received" to DialogueNode(
                                    id = "tariq_key_received",
                                    speaker = "Scholar Tariq",
                                    text = "Astonishing! The Sunfire Tombs are now unsealed. Steel yourself against the Anubis Sentinels guarding the subterranean passageway!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "tariq_enter_tombs",
                                            text = "[Move] Enter the Sunfire Tombs",
                                            changeLocationId = "sunfire_tombs"
                                        )
                                    )
                                ),
                                "tariq_lore" to DialogueNode(
                                    id = "tariq_lore",
                                    speaker = "Scholar Tariq",
                                    text = "Lord Malakor was once a high paladin who succumbed to shadow arts. Only those holding the ancient scroll of light can break his protective shield.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "tariq_back",
                                            text = "'I understand.'",
                                            nextNodeId = "tariq_start"
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    connectingLocationIds = listOf("sunstone_oasis", "sunfire_tombs")
                ),
                Location(
                    id = "sunfire_tombs",
                    name = "Sunfire Tombs",
                    regionId = "sunfire_desert",
                    description = "Imposing sandstone burial halls. Anubis stone sentinels stand watch with glowing sunstone spears.",
                    bgImageResName = "img_desert",
                    availableEnemyIds = listOf("anubis_sentinel"),
                    connectingLocationIds = listOf("sunstone_oasis", "caravan_bazaar", "catacomb_entrance")
                )
            )
        ),

        // REGION 3: OBSIDIAN CATACOMBS
        Region(
            id = "obsidian_catacombs",
            name = "Obsidian Catacombs",
            description = "Subterranean crypts carved from volcanic obsidian stone, lit by eerie blue soulfire torches.",
            bgImageResName = "img_dungeon",
            locations = listOf(
                Location(
                    id = "catacomb_entrance",
                    name = "Catacomb Entrance",
                    regionId = "obsidian_catacombs",
                    description = "Cold stone stairways leading deep underground. A ghostly spirit floats beside a shattered sarcophagus.",
                    bgImageResName = "img_dungeon",
                    npcs = listOf(
                        Npc(
                            id = "sir_gareth",
                            name = "Sir Gareth",
                            title = "Ghostly Guardian",
                            regionId = "obsidian_catacombs",
                            initialNodeId = "gareth_start",
                            dialogueNodes = mapOf(
                                "gareth_start" to DialogueNode(
                                    id = "gareth_start",
                                    speaker = "Ghost of Sir Gareth",
                                    text = "Mortal... I was once Malakor's second in command before he betrayed the light. If you defeat his undead skeleton champion, I shall bestow the Ancient Scroll upon you.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "gareth_accept",
                                            text = "[Accept Quest] 'I will purge the undead champion.'",
                                            nextNodeId = "gareth_accepted",
                                            setQuestStep = Pair("q_fallen_knight", 1)
                                        ),
                                        DialogueChoice(
                                            id = "gareth_give_scroll",
                                            text = "[Report Defeat] 'The Skeleton Warrior is vanquished!'",
                                            nextNodeId = "gareth_scroll_given",
                                            requiredQuestStep = Pair("q_fallen_knight", 1),
                                            setQuestStep = Pair("q_fallen_knight", 2),
                                            rewardItemId = "ancient_scroll",
                                            rewardGold = 100,
                                            rewardXp = 150,
                                            rewardFlagKey = "gareth_scroll_reward"
                                        )
                                    )
                                ),
                                "gareth_accepted" to DialogueNode(
                                    id = "gareth_accepted",
                                    speaker = "Ghost of Sir Gareth",
                                    text = "Advance into the Hall of Shadows! Defeat the Skeleton Warrior and return to me.",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "gareth_to_hall",
                                            text = "[Move] Enter Hall of Shadows",
                                            changeLocationId = "hall_of_shadows"
                                        )
                                    )
                                ),
                                "gareth_scroll_given" to DialogueNode(
                                    id = "gareth_scroll_given",
                                    speaker = "Ghost of Sir Gareth",
                                    text = "You have granted my soul peace! Take this Ancient Scroll. It weakens Malakor's barrier in his Sanctum!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "gareth_to_sanctum",
                                            text = "[Move] Enter Sanctum of the Shadow Lord",
                                            changeLocationId = "sanctum_of_malakor",
                                            setQuestStep = Pair("q_defeat_malakor", 3)
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    connectingLocationIds = listOf("sunfire_tombs", "hall_of_shadows")
                ),
                Location(
                    id = "hall_of_shadows",
                    name = "Hall of Shadows",
                    regionId = "obsidian_catacombs",
                    description = "An ancient vaulted chamber filled with bone piles and flickering dark flames.",
                    bgImageResName = "img_dungeon",
                    availableEnemyIds = listOf("skeleton_warrior"),
                    connectingLocationIds = listOf("catacomb_entrance", "sanctum_of_malakor")
                ),
                Location(
                    id = "sanctum_of_malakor",
                    name = "Sanctum of the Shadow Lord",
                    regionId = "obsidian_catacombs",
                    description = "The throne chamber of Shadow Lord Malakor. Dark ethereal vortexes swirl around a throne of obsidian.",
                    bgImageResName = "img_dungeon",
                    npcs = listOf(
                        Npc(
                            id = "malakor_boss_npc",
                            name = "Shadow Lord Malakor",
                            title = "Tyrant of Darkness",
                            regionId = "obsidian_catacombs",
                            initialNodeId = "malakor_start",
                            dialogueNodes = mapOf(
                                "malakor_start" to DialogueNode(
                                    id = "malakor_start",
                                    speaker = "Shadow Lord Malakor",
                                    text = "Foolish mortal! You dare enter my sanctum? The realm of Valor will bow to eternal darkness!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "malakor_challenge",
                                            text = "[Attack] 'Your reign of darkness ends now!'",
                                            startCombatEnemyId = "malakor_boss"
                                        ),
                                        DialogueChoice(
                                            id = "malakor_scroll_use",
                                            text = "[Use Ancient Scroll] Unfurl Sir Gareth's scroll of holy light!",
                                            nextNodeId = "malakor_weakened",
                                            requiredQuestStep = Pair("q_defeat_malakor", 3)
                                        )
                                    )
                                ),
                                "malakor_weakened" to DialogueNode(
                                    id = "malakor_weakened",
                                    speaker = "Shadow Lord Malakor",
                                    text = "NO! The radiant light breaks my shadow barrier! Arghhh... even weakened, I will crush you!",
                                    choices = listOf(
                                        DialogueChoice(
                                            id = "malakor_fight_weakened",
                                            text = "[Attack Weakened Malakor] Engage in final battle!",
                                            startCombatEnemyId = "malakor_boss"
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    connectingLocationIds = listOf("hall_of_shadows")
                )
            )
        )
    )
}
