public class RelicList {
    public static Relic[] relics = new Relic[] {
        new Relic("Gluttonous Bag", 0, Shop.Rarity.COMMON, "Allows one free item use", -1),
        new Relic("Cursed Hand", 1, Shop.Rarity.UNIQUE, "Hurts the enemy for 2% of their max hp when they draw a card", -1),
        new Relic("Pickpocketing 101", 2, Shop.Rarity.COMMON, "Grants 5 bonus coins at the end of a battle", -1),
        new Relic("Lucky Coin", 3, Shop.Rarity.UNCOMMON, "Grants 10% bonus coins at the end of a battle", -1),
        new Relic("Chicken Wing", 4, Shop.Rarity.COMMON, "Increases max hp by 10.", -1),
        new Relic("Heirloom S", 5, Shop.Rarity.RARE, "Start each battle with 5 extra strength.", -1),
        new Relic("Heirloom A", 6, Shop.Rarity.RARE, "Start each battle with 5 extra armor.", -1),
        new Relic("Heirloom R", 7, Shop.Rarity.RARE, "Start each battle with 5 extra regeneration.", -1),
        new Relic("Heirloom S+", 8, Shop.Rarity.LEGENDARY, "Start each battle with 10 extra strength.", 9),
        new Relic("Heirloom A+", 9, Shop.Rarity.LEGENDARY, "Start each battle with 10 extra armor.", 9),
        new Relic("Heirloom R+", 10, Shop.Rarity.LEGENDARY, "Start each battle with 10 extra regeneration.", 9),
        new Relic("Heirloom U", 11, Shop.Rarity.STELLAR, "Start each battle with 20 extra strength, 20 extra armor, and 20 extra regeneration", 10),
        new Relic("Masochism", 12, Shop.Rarity.RARE, "Gain 1 strength whenever you take damage", -1),
        new Relic("Quick Recovery", 13, Shop.Rarity.UNIQUE, "Heal 10% of the damage whenever you take damage", -1),
        new Relic("Torture Chamber", 14, Shop.Rarity.UNIQUE, "Reduce your max health by 50", -2),
        new Relic("Energized", 15, Shop.Rarity.EPIC, "Increase max energy by 1", -1),
        new Relic("Tiredness", 16, Shop.Rarity.EPIC, "Decrease max energy by 1", -2)
    };
}
