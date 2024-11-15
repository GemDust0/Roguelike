import java.util.ArrayList;

public class Attack {
    
    private String name;
    private String description;
    private ArrayList<Effect> attackTargetEffects;
    private ArrayList<Effect> attackSelfEffects;
    private boolean useTurn;
    private Shop.Rarity rarity;
    private int requirement;
    
    public Attack(String name, boolean useTurn, Effect[] attackTargetEffects, Effect[] attackSelfEffects, Shop.Rarity rarity, String description, int requirement){
        this.name = name;
        this.description = description;
        this.useTurn = useTurn;
        this.attackTargetEffects = new ArrayList<Effect>();
        for (Effect effect : attackTargetEffects){
            this.attackTargetEffects.add(effect);
        }
        this.attackSelfEffects = new ArrayList<Effect>();
        for (Effect effect : attackSelfEffects){
            this.attackSelfEffects.add(effect);
        }
        this.rarity = rarity;
        this.requirement = requirement;
    }
    
    public void addTargetEffect(Effect effect){
        attackTargetEffects.add(effect);
    }
    
    public void addSelfEffect(Effect effect){
        attackSelfEffects.add(effect);
    }
    
    public void use(Fighter user, Fighter target, BattleManager battle){
        for (Effect effect : attackTargetEffects){
            effect.apply(user, target, battle);
            lib.clear();
            battle.renderBattle();
            lib.getInput();
        }
        for (Effect effect : attackSelfEffects){
            effect.apply(user, user, battle);
            lib.clear();
            battle.renderBattle();
            lib.getInput();
        }
    }
    
    public boolean usesTurn(){
        return useTurn;
    }
    
    public Shop.Rarity getRarity(){
        return rarity;
    }
    
    public int getRequirement(){
        return requirement;
    }
    
    public String getName(){
        return name;
    }
    
    public boolean equals(Attack attack){
        return name.equals(attack.getName());
    }
    
    public String toString(){
        return name + " - " + description;
    }
}
