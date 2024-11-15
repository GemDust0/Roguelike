public class ItemList {
    public static Item[] items = {
        new Item("Potion of Health", true, 1, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.HEAL, 1, 25)}, Shop.Rarity.COMMON, "Heal 25 health.", -1),
        new Item("Sword", true, 5, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 30)}, new Effect[] {}, Shop.Rarity.RARE, "Deal 30 damage.", -1),
        new Item("Stun Bomb", true, 1, new Effect[] {new Effect(Effect.EffectEnum.DAMAGE, 1, 5), new Effect(Effect.EffectEnum.STUN, 2, 1)}, new Effect[] {}, Shop.Rarity.UNCOMMON, "Deal 5 damage and stun the enemy for 2 turns.", -1),
        new Item("Energy Drink", false, 1, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.ENERGY, 1, 3)}, Shop.Rarity.RARE, "Gain 3 energy.", -1),
        new Item("Shield", true, 5, new Effect[] {}, new Effect[] {new Effect(Effect.EffectEnum.ARMOR, 1, 5)}, Shop.Rarity.UNCOMMON, "Gain 5 shield.", -1)
    };
}
