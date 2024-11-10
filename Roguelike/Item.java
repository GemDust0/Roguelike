import java.util.ArrayList;

public class Item {
    private String name;
    private String description;
    private ArrayList<Effect> attackTargetEffects;
    private ArrayList<Effect> attackSelfEffects;
    private boolean usesTurn;
    private Integer uses;
    private Shop.Rarity rarity;
    private int requirement;
    
    public Item(String name, boolean usesTurn, Integer uses, Effect[] attackTargetEffects, Effect[] attackSelfEffects, Shop.Rarity rarity, String description, int requirement){
        this.name = name;
        this.description = description;
        this.usesTurn = usesTurn;
        this.uses = uses;
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
    
    public Item(String name, boolean usesTurn, Integer uses, ArrayList<Effect> attackTargetEffects, ArrayList<Effect> attackSelfEffects, Shop.Rarity rarity, String description, int requirement){
        this.name = name;
        this.description = description;
        this.usesTurn = usesTurn;
        this.uses = uses;
        this.attackTargetEffects = attackTargetEffects;
        this.attackSelfEffects = attackSelfEffects;
        this.rarity = rarity;
        this.requirement = requirement;
    }
    
    public void addTargetEffect(Effect effect){
        attackTargetEffects.add(effect);
    }
    
    public void addSelfEffect(Effect effect){
        attackSelfEffects.add(effect);
    }
    
    public boolean use(Fighter user, Fighter target, BattleManager battle){
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
        if (uses != null){
            uses--;
            if (uses <= 0){
                return true;
            }
        }
        return false;
    }
    
    public String getName(){
        return name;
    }
    
    public ArrayList<Effect> getSelfEffects(){
        return attackSelfEffects;
    }
    
    public ArrayList<Effect> getTargetEffects(){
        return attackTargetEffects;
    }
    
    public int getUses(){
        return uses;
    }
    
    public void setUses(int amount){
        this.uses = amount;
    }
    
    public Shop.Rarity getRarity(){
        return rarity;
    }
    
    public int getRequirement(){
        return requirement;
    }
    
    public boolean equals(Item item){
        return name.equals(item.getName());
    }
    
    public String getDescription(){
        return description;
    }
    
    public boolean getUsesTurn(){
        return usesTurn;
    }
    
    public String toString(){
        return name + " - " + description + " Uses left: " + uses ;
    }
}
