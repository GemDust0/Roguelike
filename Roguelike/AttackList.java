public class AttackList {
    public static Attack[] attacks = {
        new Attack("Strike", true, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 15)}, new Effect[] {}, Shop.Rarity.COMMON, "Do 15 damage", -1),
        new Attack("Heal", true, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.HEAL, 1, 10)}, Shop.Rarity.COMMON, "Heal 10 health", -1),
        new Attack("Armor Up", true, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.ARMOR, null, 4)}, Shop.Rarity.COMMON, "Gain 4 armor", -1),
        new Attack("Bash", true, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 5), new Effect(Effect.EffectEnum.BRITTLE, null, 3)}, new Effect[] {}, Shop.Rarity.COMMON, "Deal 5 damage and inflict 3 brittle", -1),
        new Attack("Pot of Greed", false, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.DRAW, 1, 3)}, Shop.Rarity.COMMON, "Draw 3 attacks", -1),
        new Attack("War Cry", true, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.STRENGTH, null, 4)}, Shop.Rarity.COMMON, "Gain strength 4", -1),
        new Attack("Quick Jab", false, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 10)}, new Effect[] {}, Shop.Rarity.UNCOMMON, "Deal 10 damage, does not use turn", -1),
        new Attack("Poison Jab", true, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 15), new Effect(Effect.EffectEnum.POISON, 1, 5, 50)}, new Effect[] {}, Shop.Rarity.UNCOMMON, "Deal 15 damage, may poison", -1),
        new Attack("Megastrike", true, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 30)}, new Effect[] {}, Shop.Rarity.RARE, "Deal 30 damage", 0),
        new Attack("Sacrificial Strike", true, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 20)}, new Effect[] {new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 25)}, Shop.Rarity.UNCOMMON, "Sacrifice 25% of your health to deal 20 damage", -1),
        new Attack("Fireball", true, new Effect[] {new Effect(Effect.EffectEnum.TRUEDAMAGE, 1, 10)}, new Effect[] {}, Shop.Rarity.COMMON, "Deal 10 true damage", -1),
        new Attack("Thunderbolt", true, new Effect[] {new Effect(Effect.EffectEnum.TRUEDAMAGE, 1, 20)}, new Effect[] {}, Shop.Rarity.RARE, "Deal 30 true damage", -1),
        new Attack("Thunder", true, new Effect[] {new Effect(Effect.EffectEnum.TRUEDAMAGE, 1, 40)}, new Effect[] {}, Shop.Rarity.LEGENDARY, "Deal 50 true damage", -1)
    };
}
