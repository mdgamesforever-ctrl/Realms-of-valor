package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameData
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        // Initialize with default location
        travelToLocation("verdant_grove")
    }

    // --- CHARACTER CREATION ---
    fun createCharacter(name: String, background: CharacterBackground, heroClass: HeroClass) {
        val trimmedName = name.ifBlank { "Hero of Valor" }

        var initialHp = heroClass.baseHp
        var initialResource = heroClass.baseResource
        var initialAttack = heroClass.baseAttack
        var initialDefense = heroClass.baseDefense
        var initialMagic = heroClass.baseMagic
        var initialSpeed = heroClass.baseSpeed
        var startingGold = 50

        when (background) {
            CharacterBackground.NOBLE_EXILE -> {
                startingGold += 100
                initialDefense += 5
            }
            CharacterBackground.FOREST_HERMIT -> {
                initialHp += 15
                initialSpeed += 3
            }
            CharacterBackground.STREET_URCHIN -> {
                initialAttack += 5
                initialMagic += 5
            }
        }

        // Cleric passive: Blessed Vitality (+10% max HP)
        if (heroClass == HeroClass.CLERIC) {
            initialHp = (initialHp * 1.10).toInt()
        }

        // Base unlocked skills (Level 1)
        val baseSkills = GameData.ALL_SKILLS.filter {
            it.parentClass == heroClass && it.levelRequired <= 1 && it.subclass == null
        }.map { it.id }.toSet()

        val initialInventory = listOf(
            ItemQuantity(GameData.ITEMS["health_potion"]!!, 2),
            ItemQuantity(GameData.ITEMS["mana_potion"]!!, 1)
        )

        _gameState.value = _gameState.value.copy(
            characterName = trimmedName,
            background = background,
            heroClass = heroClass,
            level = 1,
            xp = 0,
            xpToNextLevel = 100,
            gold = startingGold,
            hp = initialHp,
            maxHp = initialHp,
            resource = initialResource,
            maxResource = initialResource,
            attack = initialAttack,
            defense = initialDefense,
            magic = initialMagic,
            speed = initialSpeed,
            inventory = initialInventory,
            unlockedSkillIds = baseSkills,
            narrativeLog = listOf("Welcome, $trimmedName the ${heroClass.title}! Your journey begins in the Whispering Woods...")
        )
    }

    // --- SINGLE SOURCE OF TRUTH DEDICATED STATE MUTATION FUNCTIONS ---

    fun adjustGold(amount: Int): Boolean {
        val current = _gameState.value.gold
        val newGold = current + amount
        if (newGold < 0) return false
        _gameState.value = _gameState.value.copy(gold = newGold)
        return true
    }

    fun adjustHp(amount: Int) {
        val state = _gameState.value
        val newHp = (state.hp + amount).coerceIn(0, state.maxHp)
        _gameState.value = state.copy(hp = newHp)
    }

    fun adjustResource(amount: Int) {
        val state = _gameState.value
        val newResource = (state.resource + amount).coerceIn(0, state.maxResource)
        _gameState.value = state.copy(resource = newResource)
    }

    fun gainXp(amount: Int) {
        val state = _gameState.value
        var newXp = state.xp + amount
        var newLevel = state.level
        var newXpToNext = state.xpToNextLevel
        var hpBonus = 0
        var atkBonus = 0
        var defBonus = 0
        var magBonus = 0
        val logList = mutableListOf<String>()

        logList.add("Gained $amount XP!")

        while (newXp >= newXpToNext) {
            newXp -= newXpToNext
            newLevel++
            newXpToNext = (newXpToNext * 1.5).toInt()
            hpBonus += 15
            atkBonus += 3
            defBonus += 2
            magBonus += 3
            logList.add("★ LEVEL UP! You reached Level $newLevel!")
        }

        var pendingSubclass = state.pendingSubclassChoice
        if (newLevel >= 3 && state.subclass == null) {
            pendingSubclass = true
            logList.add("★ You have reached Level 3! Choose your Subclass in the Skills menu!")
        }

        // Unlock new skills for level
        val newSkills = GameData.ALL_SKILLS.filter { skill ->
            skill.parentClass == state.heroClass &&
                    skill.levelRequired <= newLevel &&
                    (skill.subclass == null || skill.subclass == state.subclass)
        }.map { it.id }.toSet()

        val updatedUnlocked = state.unlockedSkillIds + newSkills

        _gameState.value = state.copy(
            level = newLevel,
            xp = newXp,
            xpToNextLevel = newXpToNext,
            maxHp = state.maxHp + hpBonus,
            hp = (state.hp + hpBonus).coerceIn(0, state.maxHp + hpBonus),
            maxResource = state.maxResource + 10,
            resource = state.maxResource + 10,
            attack = state.attack + atkBonus,
            defense = state.defense + defBonus,
            magic = state.magic + magBonus,
            unlockedSkillIds = updatedUnlocked,
            pendingSubclassChoice = pendingSubclass,
            narrativeLog = state.narrativeLog + logList
        )
    }

    fun chooseSubclass(subclass: Subclass) {
        val state = _gameState.value
        if (state.subclass != null) return // Permanent choice!

        val newSkills = GameData.ALL_SKILLS.filter { skill ->
            skill.parentClass == state.heroClass &&
                    skill.levelRequired <= state.level &&
                    skill.subclass == subclass
        }.map { it.id }.toSet()

        _gameState.value = state.copy(
            subclass = subclass,
            pendingSubclassChoice = false,
            unlockedSkillIds = state.unlockedSkillIds + newSkills,
            narrativeLog = state.narrativeLog + "You have chosen the path of the ${subclass.title}!"
        )
    }

    fun addItem(itemId: String, quantity: Int = 1) {
        val item = GameData.ITEMS[itemId] ?: return
        val state = _gameState.value
        val existing = state.inventory.find { it.item.id == itemId }

        val newInventory = if (existing != null) {
            state.inventory.map {
                if (it.item.id == itemId) ItemQuantity(it.item, it.quantity + quantity)
                else it
            }
        } else {
            state.inventory + ItemQuantity(item, quantity)
        }

        _gameState.value = state.copy(inventory = newInventory)
    }

    fun removeItem(itemId: String, quantity: Int = 1): Boolean {
        val state = _gameState.value
        val existing = state.inventory.find { it.item.id == itemId } ?: return false
        if (existing.quantity < quantity) return false

        val newInventory = state.inventory.mapNotNull {
            if (it.item.id == itemId) {
                val rem = it.quantity - quantity
                if (rem > 0) ItemQuantity(it.item, rem) else null
            } else it
        }

        _gameState.value = state.copy(inventory = newInventory)
        return true
    }

    fun equipItem(item: Item) {
        val state = _gameState.value
        when (item.type) {
            ItemType.WEAPON -> _gameState.value = state.copy(equippedWeapon = item)
            ItemType.ARMOR -> _gameState.value = state.copy(equippedArmor = item)
            else -> {}
        }
        appendLog("Equipped ${item.name}.")
    }

    fun useItem(itemId: String) {
        val item = GameData.ITEMS[itemId] ?: return
        if (!removeItem(itemId, 1)) return

        if (item.hpRestore > 0) adjustHp(item.hpRestore)
        if (item.resourceRestore > 0) adjustResource(item.resourceRestore)

        appendLog("Used ${item.name}. Restored HP/Resource.")
    }

    // --- NAVIGATION & LOCATION ---
    fun travelToLocation(locationId: String) {
        val state = _gameState.value
        val targetLocation = GameData.REGIONS.flatMap { it.locations }.find { it.id == locationId }
            ?: return

        val logs = state.narrativeLog.takeLast(15) + "Arrived at ${targetLocation.name}."
        _gameState.value = state.copy(
            currentLocationId = locationId,
            currentDialogueNpcId = null,
            currentDialogueNodeId = null,
            activeTab = "MAIN",
            narrativeLog = logs
        )
    }

    fun startDialogueWithNpc(npc: Npc) {
        val state = _gameState.value
        val initialNode = npc.dialogueNodes[npc.initialNodeId] ?: return

        _gameState.value = state.copy(
            currentDialogueNpcId = npc.id,
            currentDialogueNodeId = initialNode.id,
            activeTab = "MAIN"
        )
    }

    fun selectDialogueChoice(choice: DialogueChoice) {
        val state = _gameState.value
        if (state.isActionProcessing) return // Double-tap lock!

        _gameState.value = state.copy(isActionProcessing = true)

        viewModelScope.launch {
            try {
                // Check required quest step
                choice.requiredQuestStep?.let { (qId, reqStep) ->
                    val currentStep = state.questProgress[qId] ?: 1
                    if (currentStep < reqStep) {
                        appendLog("You have not met the required quest progress yet.")
                        _gameState.value = _gameState.value.copy(isActionProcessing = false)
                        return@launch
                    }
                }

                // DUPLICATE-REWARD PREVENTION CHECK!
                choice.rewardFlagKey?.let { flag ->
                    if (!state.grantedRewardFlags.contains(flag)) {
                        val newGranted = state.grantedRewardFlags + flag
                        _gameState.value = _gameState.value.copy(grantedRewardFlags = newGranted)

                        if (choice.rewardGold != 0) adjustGold(choice.rewardGold)
                        if (choice.rewardXp > 0) gainXp(choice.rewardXp)
                        choice.rewardItemId?.let { addItem(it, 1) }
                    }
                } ?: run {
                    // No flag key, but still check standard rewards
                    if (choice.rewardGold != 0) adjustGold(choice.rewardGold)
                    if (choice.rewardXp > 0) gainXp(choice.rewardXp)
                    choice.rewardItemId?.let { addItem(it, 1) }
                }

                // Update quest step
                choice.setQuestStep?.let { (qId, newStep) ->
                    val currentStep = _gameState.value.questProgress[qId] ?: 1
                    if (newStep > currentStep) {
                        val newProgress = _gameState.value.questProgress + (qId to newStep)
                        _gameState.value = _gameState.value.copy(questProgress = newProgress)
                        val quest = GameData.QUESTS[qId]
                        quest?.let { appendLog("★ Quest Updated: ${it.title} (Step $newStep)") }
                    }
                }

                // Open shop
                if (choice.openShop) {
                    _gameState.value = _gameState.value.copy(activeTab = "SHOP")
                }

                // Start combat
                choice.startCombatEnemyId?.let { enemyId ->
                    startCombat(enemyId)
                }

                // Change location
                choice.changeLocationId?.let { locId ->
                    travelToLocation(locId)
                }

                // Advance dialogue node
                choice.nextNodeId?.let { nodeId ->
                    _gameState.value = _gameState.value.copy(currentDialogueNodeId = nodeId)
                }

            } finally {
                _gameState.value = _gameState.value.copy(isActionProcessing = false)
            }
        }
    }

    // --- MERCHANT SHOP SYSTEM ---
    fun buyItem(item: Item) {
        val state = _gameState.value
        if (state.gold < item.value) {
            appendLog("Not enough Gold to buy ${item.name}!")
            return
        }
        if (adjustGold(-item.value)) {
            addItem(item.id, 1)
            appendLog("Purchased ${item.name} for ${item.value} Gold.")
        }
    }

    fun sellItem(item: Item) {
        val sellPrice = max(1, item.value / 2)
        if (removeItem(item.id, 1)) {
            adjustGold(sellPrice)
            appendLog("Sold ${item.name} for $sellPrice Gold.")
        }
    }

    // --- COMBAT SYSTEM (TURN-BASED) ---
    fun startCombat(enemyId: String) {
        val enemy = GameData.ENEMIES[enemyId] ?: return
        val state = _gameState.value

        val initialLog = listOf(
            "⚔ COMBAT ENGAGED: ${enemy.name} emerges!",
            enemy.description
        )

        _gameState.value = state.copy(
            combatState = CombatStatus(
                inCombat = true,
                enemy = enemy,
                currentEnemyHp = enemy.maxHp,
                turnNumber = 1,
                combatLog = initialLog
            ),
            activeTab = "MAIN"
        )
    }

    fun performCombatAttack() {
        val state = _gameState.value
        val combat = state.combatState
        val enemy = combat.enemy ?: return
        if (state.isActionProcessing) return

        _gameState.value = state.copy(isActionProcessing = true)

        viewModelScope.launch {
            try {
                // Calculate Effective Stats
                val effectiveAtk = calculateEffectiveAttack(state)
                val effectiveDef = calculateEffectiveDefense(state)

                var damageDealt = max(1, effectiveAtk - enemy.defense / 2)

                // Rogue Assassin Critical Focus & Shadow Step
                val critChance = if (state.subclass == Subclass.ASSASSIN) 0.25 else 0.10
                val isCrit = combat.shadowStepActive || (Random.nextDouble() < critChance)
                if (isCrit) {
                    damageDealt = (damageDealt * 1.8).toInt()
                }

                // Death Mark bonus (+25%)
                if (combat.enemyMarkedTurns > 0) {
                    damageDealt = (damageDealt * 1.25).toInt()
                }

                val newEnemyHp = max(0, combat.currentEnemyHp - damageDealt)

                val log = mutableListOf<String>()
                if (isCrit) {
                    log.add("💥 CRITICAL HIT! You strike ${enemy.name} for $damageDealt physical damage!")
                } else {
                    log.add("⚔ You strike ${enemy.name} dealing $damageDealt damage.")
                }

                var updatedCombat = combat.copy(
                    currentEnemyHp = newEnemyHp,
                    shadowStepActive = false,
                    combatLog = combat.combatLog + log
                )

                if (newEnemyHp <= 0) {
                    handleCombatVictory(updatedCombat)
                } else {
                    // Enemy turn
                    executeEnemyTurn(updatedCombat)
                }
            } finally {
                _gameState.value = _gameState.value.copy(isActionProcessing = false)
            }
        }
    }

    fun performCombatDefend() {
        val state = _gameState.value
        val combat = state.combatState
        val enemy = combat.enemy ?: return
        if (state.isActionProcessing) return

        _gameState.value = state.copy(isActionProcessing = true)

        viewModelScope.launch {
            try {
                val updatedCombat = combat.copy(
                    playerDefending = true,
                    combatLog = combat.combatLog + "🛡 You raise your guard to absorb incoming damage."
                )
                executeEnemyTurn(updatedCombat)
            } finally {
                _gameState.value = _gameState.value.copy(isActionProcessing = false)
            }
        }
    }

    fun castSkillInCombat(skillId: String) {
        val state = _gameState.value
        val combat = state.combatState
        val enemy = combat.enemy ?: return
        val skill = GameData.ALL_SKILLS.find { it.id == skillId } ?: return

        if (state.isActionProcessing) return

        // Check resource cost & cooldown
        val cooldownRemaining = state.skillCooldowns[skillId] ?: 0
        if (cooldownRemaining > 0) {
            appendCombatLog("Skill ${skill.name} is on cooldown for $cooldownRemaining more turn(s).")
            return
        }

        // Mage Arcane Insight passive (15% chance 0 mana)
        val actualCost = if (state.heroClass == HeroClass.MAGE && state.subclass == Subclass.ARCANIST && Random.nextDouble() < 0.15) 0 else skill.resourceCost

        if (state.resource < actualCost) {
            appendCombatLog("Not enough ${state.heroClass?.resourceName ?: "Resource"} to cast ${skill.name}!")
            return
        }

        _gameState.value = state.copy(isActionProcessing = true)

        viewModelScope.launch {
            try {
                // Deduct resource & set cooldown
                adjustResource(-actualCost)
                val newCooldowns = state.skillCooldowns + (skillId to skill.cooldown)
                _gameState.value = _gameState.value.copy(skillCooldowns = newCooldowns)

                val effectiveAtk = calculateEffectiveAttack(state)
                val effectiveMag = calculateEffectiveMagic(state)
                val log = mutableListOf<String>()
                var newEnemyHp = combat.currentEnemyHp
                var updatedCombat = combat

                when (skill.effectType) {
                    SkillEffectType.DAMAGE_PHYSICAL -> {
                        val dmg = max(1, ((effectiveAtk * skill.effectValue) - enemy.defense / 2).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        log.add("✨ ${skill.name}! Dealt $dmg physical damage to ${enemy.name}.")
                    }
                    SkillEffectType.DAMAGE_MAGIC -> {
                        val dmg = max(1, ((effectiveMag * skill.effectValue) - enemy.defense / 3).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        log.add("🔥 ${skill.name}! Dealt $dmg magic damage to ${enemy.name}.")
                    }
                    SkillEffectType.HEAL -> {
                        val healVal = (effectiveMag * skill.effectValue).toInt()
                        adjustHp(healVal)
                        log.add("✨ ${skill.name}! Restored $healVal HP.")
                    }
                    SkillEffectType.BUFF_DEFENSE -> {
                        updatedCombat = updatedCombat.copy(guardStanceTurns = 2)
                        log.add("🛡 ${skill.name}! Defense sharply boosted for 2 turns.")
                    }
                    SkillEffectType.RECKLESS_SWING -> {
                        val dmg = max(1, ((effectiveAtk * skill.effectValue) - enemy.defense / 2).toInt())
                        val recoil = (dmg * 0.10).toInt()
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        adjustHp(-recoil)
                        log.add("💥 Reckless Swing dealt $dmg damage! Recoil inflicted $recoil damage on yourself.")
                    }
                    SkillEffectType.TAUNT -> {
                        log.add("😠 You taunted ${enemy.name}! Enemy focus locked onto you.")
                    }
                    SkillEffectType.RETALIATE -> {
                        updatedCombat = updatedCombat.copy(retaliateActive = true)
                        log.add("🛡 Retaliate active! Next hit taken will be reflected 50% back.")
                    }
                    SkillEffectType.FORTIFY -> {
                        updatedCombat = updatedCombat.copy(guardStanceTurns = 3)
                        log.add("🏰 Fortify active! Defense and Max HP empowered.")
                    }
                    SkillEffectType.FROST_BOLT -> {
                        val dmg = max(1, ((effectiveMag * skill.effectValue) - enemy.defense / 3).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        updatedCombat = updatedCombat.copy(enemySlowedTurns = 2)
                        log.add("❄ Frost Bolt dealt $dmg damage and chilled ${enemy.name}'s speed!")
                    }
                    SkillEffectType.INFERNO -> {
                        updatedCombat = updatedCombat.copy(enemyBurnTurns = 3)
                        log.add("🔥 Inferno! Engulfed ${enemy.name} in searing flames for 3 turns.")
                    }
                    SkillEffectType.METEOR -> {
                        val dmg = max(1, ((effectiveMag * skill.effectValue) - enemy.defense / 4).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        log.add("☄ METEOR STRIKE! Dealt a massive $dmg magic damage!")
                    }
                    SkillEffectType.SLOW -> {
                        updatedCombat = updatedCombat.copy(enemySlowedTurns = 2)
                        log.add("⏳ Slowed ${enemy.name}'s speed for 2 turns.")
                    }
                    SkillEffectType.MANA_DRAIN -> {
                        val dmg = max(1, ((effectiveMag * skill.effectValue) - enemy.defense / 3).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        val manaRestored = (dmg * 0.50).toInt()
                        adjustResource(manaRestored)
                        log.add("🌀 Mana Drain dealt $dmg damage and siphon $manaRestored Mana!")
                    }
                    SkillEffectType.SILENCE -> {
                        updatedCombat = updatedCombat.copy(enemyStunned = true)
                        log.add("🤫 Silenced ${enemy.name}! Enemy special abilities locked.")
                    }
                    SkillEffectType.TIME_WARP -> {
                        log.add("⏳ TIME WARP! You warp time and take an immediate extra action!")
                        updatedCombat = updatedCombat.copy(
                            currentEnemyHp = newEnemyHp,
                            combatLog = updatedCombat.combatLog + log
                        )
                        _gameState.value = _gameState.value.copy(combatState = updatedCombat)
                        return@launch
                    }
                    SkillEffectType.BACKSTAB -> {
                        val multiplier = if (combat.turnNumber == 1) 2.3 else 1.8
                        val dmg = max(1, ((effectiveAtk * multiplier) - enemy.defense / 2).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        log.add("🗡 Backstab dealt $dmg precision damage!")
                    }
                    SkillEffectType.POISON_BLADE -> {
                        val dmg = max(1, ((effectiveAtk * skill.effectValue) - enemy.defense / 2).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        updatedCombat = updatedCombat.copy(enemyPoisonTurns = 3)
                        log.add("☠ Poison Blade dealt $dmg damage and poisoned ${enemy.name}!")
                    }
                    SkillEffectType.SHADOW_STEP -> {
                        updatedCombat = updatedCombat.copy(shadowStepActive = true)
                        log.add("🥷 Shadow Step! Next attack is guaranteed CRITICAL!")
                    }
                    SkillEffectType.DEATH_MARK -> {
                        updatedCombat = updatedCombat.copy(enemyMarkedTurns = 3)
                        log.add("🎯 Death Mark applied! Target takes 25% extra damage.")
                    }
                    SkillEffectType.DISTRACT -> {
                        updatedCombat = updatedCombat.copy(enemyStunned = true)
                        log.add("🎭 Distracted ${enemy.name}! Enemy skips next turn.")
                    }
                    SkillEffectType.STEAL -> {
                        val dmg = max(1, ((effectiveAtk * skill.effectValue) - enemy.defense / 2).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        val stolenGold = Random.nextInt(15, 35)
                        adjustGold(stolenGold)
                        log.add("💰 Steal dealt $dmg damage and snatched $stolenGold Gold!")
                    }
                    SkillEffectType.VANISH -> {
                        updatedCombat = updatedCombat.copy(vanishTurns = 1)
                        log.add("🌫 Vanished into shadows! Untargetable for 1 turn.")
                    }
                    SkillEffectType.HOLY_STRIKE -> {
                        val dmg = max(1, ((effectiveAtk * skill.effectValue) - enemy.defense / 2).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        val healVal = (dmg * 0.20).toInt()
                        adjustHp(healVal)
                        log.add("✨ Holy Strike dealt $dmg damage and restored $healVal HP!")
                    }
                    SkillEffectType.DIVINE_SHIELD -> {
                        updatedCombat = updatedCombat.copy(divineShieldActive = true)
                        log.add("🛡 Divine Shield! Negates next incoming attack.")
                    }
                    SkillEffectType.CONSECRATE -> {
                        val dmg = max(1, ((effectiveMag * 1.0) - enemy.defense / 3).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        val healVal = effectiveMag
                        adjustHp(healVal)
                        log.add("✝ Consecrate dealt $dmg magic damage and healed $healVal HP!")
                    }
                    SkillEffectType.DIVINE_JUDGMENT -> {
                        val dmg = max(1, ((effectiveMag * skill.effectValue) - enemy.defense / 3).toInt())
                        newEnemyHp = max(0, newEnemyHp - dmg)
                        log.add("⚖ Divine Judgment dealt $dmg magic damage!")
                        if (newEnemyHp <= 0) {
                            adjustHp(state.maxHp)
                            log.add("✨ Divine Judgment slain the enemy! Fully restored HP!")
                        }
                    }
                    SkillEffectType.GREATER_HEAL -> {
                        val healVal = (effectiveMag * 2.0).toInt()
                        adjustHp(healVal)
                        log.add("✨ Greater Heal restored $healVal HP!")
                    }
                    SkillEffectType.REGENERATION -> {
                        val regenVal = (state.maxHp * 0.10).toInt()
                        adjustHp(regenVal)
                        log.add("🌱 Regeneration restored $regenVal HP.")
                    }
                    SkillEffectType.PURIFY -> {
                        updatedCombat = updatedCombat.copy(
                            enemyStunned = false,
                            enemyBurnTurns = 0,
                            enemyPoisonTurns = 0
                        )
                        log.add("✨ Purify cleansed all negative status effects!")
                    }
                    SkillEffectType.RESURRECTION_PRAYER -> {
                        log.add("✝ Resurrection Prayer active! Lethal hit will heal to 50% Max HP.")
                    }
                    else -> {}
                }

                updatedCombat = updatedCombat.copy(
                    currentEnemyHp = newEnemyHp,
                    combatLog = updatedCombat.combatLog + log
                )

                if (newEnemyHp <= 0) {
                    handleCombatVictory(updatedCombat)
                } else {
                    executeEnemyTurn(updatedCombat)
                }

            } finally {
                _gameState.value = _gameState.value.copy(isActionProcessing = false)
            }
        }
    }

    private fun executeEnemyTurn(combat: CombatStatus) {
        val state = _gameState.value
        val enemy = combat.enemy ?: return
        val log = mutableListOf<String>()

        // Check status debuffs on enemy
        if (combat.enemyStunned) {
            log.add("🌀 ${enemy.name} is stunned and skips their turn!")
            val updatedCombat = combat.copy(
                enemyStunned = false,
                turnNumber = combat.turnNumber + 1,
                combatLog = combat.combatLog + log
            )
            decrementCooldownsAndFinishTurn(updatedCombat)
            return
        }

        // Untargetable / Vanish
        if (combat.vanishTurns > 0) {
            log.add("🌫 ${enemy.name} swings blindly into empty shadows! You avoided the attack.")
            val updatedCombat = combat.copy(
                vanishTurns = 0,
                turnNumber = combat.turnNumber + 1,
                combatLog = combat.combatLog + log
            )
            decrementCooldownsAndFinishTurn(updatedCombat)
            return
        }

        // Divine Shield negation
        if (combat.divineShieldActive) {
            log.add("🛡 Divine Shield completely negated ${enemy.name}'s attack!")
            val updatedCombat = combat.copy(
                divineShieldActive = false,
                turnNumber = combat.turnNumber + 1,
                combatLog = combat.combatLog + log
            )
            decrementCooldownsAndFinishTurn(updatedCombat)
            return
        }

        // Evasion Rogue passive (15%)
        if (state.heroClass == HeroClass.ROGUE && Random.nextDouble() < 0.15) {
            log.add("💨 EVASION! You agilely dodged ${enemy.name}'s attack!")
            val updatedCombat = combat.copy(
                turnNumber = combat.turnNumber + 1,
                combatLog = combat.combatLog + log
            )
            decrementCooldownsAndFinishTurn(updatedCombat)
            return
        }

        // Calculate incoming damage
        val effectiveDef = calculateEffectiveDefense(state)
        var rawDamage = max(2, enemy.attack - effectiveDef / 2)

        if (combat.playerDefending) {
            rawDamage /= 2
        }

        if (combat.guardStanceTurns > 0) {
            rawDamage = (rawDamage * 0.7).toInt()
        }

        // Guardian Shield Wall passive
        if (state.subclass == Subclass.GUARDIAN && (state.hp.toDouble() / state.maxHp) < 0.50) {
            rawDamage = (rawDamage * 0.8).toInt()
        }

        // Apply incoming damage
        adjustHp(-rawDamage)
        log.add("💥 ${enemy.name} attacks you for $rawDamage damage!")

        // Retaliate
        if (combat.retaliateActive) {
            val reflectDmg = (rawDamage * 0.50).toInt()
            val newEnemyHp = max(0, combat.currentEnemyHp - reflectDmg)
            log.add("↩ Retaliate reflected $reflectDmg damage back at ${enemy.name}!")
            if (newEnemyHp <= 0) {
                handleCombatVictory(combat.copy(currentEnemyHp = newEnemyHp, combatLog = combat.combatLog + log))
                return
            }
        }

        // Check if player HP dropped <= 0 -> Check Last Stand or Resurrection Prayer
        val currentState = _gameState.value
        if (currentState.hp <= 0) {
            if (state.subclass == Subclass.GUARDIAN && !combat.lastStandUsed) {
                adjustHp(1)
                log.add("🛡 LAST STAND! You survived a fatal blow with 1 HP!")
            } else if (state.subclass == Subclass.PRIEST && !combat.resurrectionUsed) {
                adjustHp(state.maxHp / 2)
                log.add("✨ RESURRECTION PRAYER! You were restored to 50% Max HP!")
            } else {
                // Game Over
                log.add("💀 YOU HAVE BEEN DEFEATED IN COMBAT!")
                _gameState.value = currentState.copy(
                    combatState = combat.copy(
                        inCombat = false,
                        combatLog = combat.combatLog + log
                    ),
                    narrativeLog = currentState.narrativeLog + "You were struck down in battle. You awaken at the Verdant Grove shrine.",
                    hp = state.maxHp / 2,
                    currentLocationId = "verdant_grove"
                )
                return
            }
        }

        val updatedCombat = combat.copy(
            turnNumber = combat.turnNumber + 1,
            playerDefending = false,
            combatLog = combat.combatLog + log
        )

        decrementCooldownsAndFinishTurn(updatedCombat)
    }

    private fun decrementCooldownsAndFinishTurn(combat: CombatStatus) {
        val state = _gameState.value
        val updatedCooldowns = state.skillCooldowns.mapValues { max(0, it.value - 1) }

        // Cleric Faithful Recovery passive
        val regenAmount = if (state.heroClass == HeroClass.CLERIC && state.subclass == Subclass.PRIEST) 10 else 5
        adjustResource(regenAmount)

        _gameState.value = state.copy(
            skillCooldowns = updatedCooldowns,
            combatState = combat
        )
    }

    private fun handleCombatVictory(combat: CombatStatus) {
        val enemy = combat.enemy ?: return
        val state = _gameState.value

        // Prevent duplicate victory triggers!
        val victoryFlag = "victory_${enemy.id}_turn_${combat.turnNumber}"
        if (state.grantedRewardFlags.contains(victoryFlag)) return

        val newGranted = state.grantedRewardFlags + victoryFlag

        // Calculate Gold reward (Rogue Trickster Lucky Streak +20%)
        val bonusGoldRatio = if (state.subclass == Subclass.TRICKSTER) 1.20 else 1.0
        val finalGold = (enemy.goldReward * bonusGoldRatio).toInt()

        adjustGold(finalGold)
        gainXp(enemy.xpReward)

        enemy.dropItemId?.let { addItem(it, 1) }

        val victoryLog = listOf(
            "🏆 VICTORY! You defeated ${enemy.name}!",
            "Earned $finalGold Gold and ${enemy.xpReward} XP."
        )

        val updatedCombat = combat.copy(
            inCombat = false,
            combatLog = combat.combatLog + victoryLog
        )

        val isBossDefeat = enemy.id == "malakor_boss"

        _gameState.value = _gameState.value.copy(
            grantedRewardFlags = newGranted,
            combatState = updatedCombat,
            gameEnded = isBossDefeat,
            narrativeLog = _gameState.value.narrativeLog + "Defeated ${enemy.name} in combat!"
        )
    }

    private fun calculateEffectiveAttack(state: GameState): Int {
        var atk = state.attack + (state.equippedWeapon?.attackBonus ?: 0)
        // Warrior Berserker Bloodlust passive (+5% per 10% HP missing)
        if (state.subclass == Subclass.BERSERKER) {
            val missingHpRatio = (state.maxHp - state.hp).toDouble() / state.maxHp
            val bloodlustBonus = (missingHpRatio / 0.10 * 0.05).coerceAtMost(0.30)
            atk = (atk * (1.0 + bloodlustBonus)).toInt()
        }
        return atk
    }

    private fun calculateEffectiveDefense(state: GameState): Int {
        var def = state.defense + (state.equippedArmor?.defenseBonus ?: 0)
        // Warrior Heavy Armor Mastery (+15% if armor equipped)
        if (state.heroClass == HeroClass.WARRIOR && state.equippedArmor != null) {
            def = (def * 1.15).toInt()
        }
        return def
    }

    private fun calculateEffectiveMagic(state: GameState): Int {
        var mag = state.magic + (state.equippedWeapon?.magicBonus ?: 0)
        // Mage Arcane Focus (+15% if no heavy armor)
        if (state.heroClass == HeroClass.MAGE && state.equippedArmor == null) {
            mag = (mag * 1.15).toInt()
        }
        return mag
    }

    private fun appendLog(msg: String) {
        val state = _gameState.value
        _gameState.value = state.copy(narrativeLog = state.narrativeLog + msg)
    }

    private fun appendCombatLog(msg: String) {
        val state = _gameState.value
        val combat = state.combatState
        _gameState.value = state.copy(
            combatState = combat.copy(combatLog = combat.combatLog + msg)
        )
    }

    fun restartGame() {
        _gameState.value = GameState()
    }

    fun setTab(tab: String) {
        _gameState.value = _gameState.value.copy(activeTab = tab)
    }
}
