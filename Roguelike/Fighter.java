import java.util.ArrayList;

public class Fighter {
    
    private String name;
    private HealthComponent health;
    private EffectManager effects;
    private AttackManager attacks;
    private RelicManager relics;
    private int maxEnergy;
    private int energy;
    
    public Fighter(String name, int maxHealth, int maxEnergy){
        this.name = name;
        this.health = new HealthComponent(maxHealth);
        this.effects = new EffectManager();
        this.attacks = new AttackManager();
        this.relics = new RelicManager();
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }
    
    public void reduceEnergy(){
        energy--;
    }
    
    public void setMaxEnergy(int amount){
        this.maxEnergy = amount;
    }
    
    public void restoreEnergy(){
        this.energy = Math.max(maxEnergy, 1);
    }
    
    public int getEnergy(){
        return energy;
    }
    
    public int getMaxEnergy(){
        return maxEnergy;
    }
    
    public void restoreEnergy(int amount){
        energy += amount;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void damage(int amount){
        health.damage(amount);
        if (amount >= 0){
            if (hasRelic(12)){
                addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, countRelic(12)));
            }
            if (hasRelic(13) && getCurHealth() > 0){
                heal((int)Math.ceil(amount*0.1));
            }
        }
    }
    
    public void heal(int amount){
        health.heal(amount);
    }
    
    public void heal(){
        health.heal();
    }
    
    public int getCurHealth(){
        return health.getCurHealth();
    }
    
    public int getMaxHealth(){
        return health.getMaxHealth();
    }
    
    public void setCurHealth(int amount){
        health.setCurHealth(amount);
    }
    
    public void setMaxHealth(int health){
        this.health.setMaxHealth(health);
    }
    
    public ArrayList<Attack> getAttacks(){
        return attacks.getAttacks();
    }
    
    public int getAttackCount(){
        return attacks.getAttacks().size();
    }
    
    public void addAttack(Attack attack){
        attacks.addAttack(attack);
        if (name != null && name.equals("You")){
            if (getAttackCount() >= 50){
                Achievements.giveAchievement(12);
            }
        }
    }
    
    public boolean useAttack(int index, Fighter target){
        BattleManager.addMessage(name + " used " + attacks.getAvailableAttack(index).getName());
        return attacks.useAttack(index, this, target);
    }
    
    public void removeAttack(int index){
        attacks.removeAttack(index);
    }
    
    public void addEffect(Effect effect){
        effects.addEffect(effect);
    }
    
    public void removeEffect(Effect effect){
        effects.removeEffect(effect);
    }
    
    public void clearEffects(){
        effects.clearEffects();
    }
    
    public boolean hasEffect(Effect.EffectEnum effect){
        return effects.hasEffect(effect);
    }
    
    public Effect getEffect(Effect.EffectEnum effect){
        return effects.getEffect(effect);
    }
    
    public int getEffectStrength(Effect.EffectEnum effect){
        return effects.getEffectStrength(effect);
    }
    
    public void countdown(Fighter opponent){
        effects.countdown(opponent, this);
    }
    
    public boolean chooseAttack(Fighter target){
        String c = null;
        BattleManager.clearMessages();
        while (true){
            lib.clear();
            BattleManager.renderBattle();
            System.out.println("Pick a move, enter nothing to return to the menu:");
            for (int i=0; i<attacks.getAvailable().size(); i++){
                System.out.println((i+1) + ". " + attacks.getAvailable().get(i));
            }
            System.out.println(attacks.getAvailable().size()+1 + ". Pass");
            try {
                if (target.getCurHealth() == 0){
                    return true;
                }
                c = lib.getInput("\n=> ");
                if (Integer.parseInt(c) == attacks.getAvailable().size()+1){
                    BattleManager.addMessage("You skipped your turn");
                    return true;
                } else if (Integer.parseInt(c) > 0 && Integer.parseInt(c) <= attacks.getAvailable().size()){
                    energy--;
                    return useAttack(Integer.parseInt(c)-1, target) || energy == 0 || chooseAttack(target);
                }
            } catch (Exception e){
                if (c.equals("")){
                    return false;
                }
            }
        }
    }
    
    public boolean chooseRandomAttack(Fighter target){
        energy--;
        if (target.getCurHealth() == 0){
            return true;
        }
        return useAttack(lib.randint(attacks.getAvailable().size()), target) || energy == 0;
    }
    
    public void printEffects(){
        effects.printEffects();
    }
    
    public void resetAvailable(int maxAmount){
        attacks.resetAvailable(maxAmount);
    }
    
    public int addAvailable(int amount){
        if (hasRelic(34)){
            addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, amount));
        }
        return attacks.addAvailable(amount);
    }
    
    public boolean hasRelic(int id){
        return relics.hasRelic(id);
    }
    
    public int countRelic(int id){
        return relics.countRelic(id);
    }
    
    public void addRelic(Relic relic, boolean applyEffects){
        relics.addRelic(relic);
        if (applyEffects){
            switch (relic.getId()){
                case 4:
                    setMaxHealth(getMaxHealth()+10);
                    setCurHealth(getCurHealth()+10);
                    break;
                case 14:
                    setMaxHealth(getMaxHealth()-50);
                    setCurHealth(getCurHealth()-50);
                    break;
                case 15:
                    maxEnergy++;
                    energy++;
                    break;
                case 16:
                    maxEnergy--;
                    energy--;
                    break;
                case 20:
                    setMaxHealth(getMaxHealth()+15);
                setCurHealth(getCurHealth()+15);
                    break;
                case 21:
                    setMaxHealth(getMaxHealth()+25);
                    setCurHealth(getCurHealth()+25);
                    break;
                case 22:
                    setMaxHealth(getMaxHealth()+50);
                    setCurHealth(getCurHealth()+50);
                    break;
                case 23:
                    setMaxHealth(getMaxHealth()+100);
                    setCurHealth(getCurHealth()+100);
                    break;
            }
            if (name.equals("You")){
                if (countRelic(2) >= 3){ // Thief
                    Achievements.giveAchievement(7);
                }
                if (getMaxHealth() >= 300){ // Gluttony
                    Achievements.giveAchievement(5);
                }
                if (hasRelic(5) && hasRelic(6) && hasRelic(7)){ // Heirloom Collector
                    Achievements.giveAchievement(9);
                    if (hasRelic(8) && hasRelic(9) && hasRelic(10)){ // Heirloom Collector+
                        Achievements.giveAchievement(10);
                    }
                }
            }
        }
    }
    
    public void addRelic(Relic relic){
        addRelic(relic, true);
    }
    
    public void removeRelic(int index){
        relics.removeRelic(index);
    }
    
    public ArrayList<Relic> getRelics(){
        return relics.getRelics();
    }
    
    public void printRelics(){
        relics.printRelics();
    }
    
    public boolean isDead(){
        return getCurHealth() == 0;
    }
    
    public String getName(){
        return name;
    }
    
    public String toString(){
        return name + ": " + health + "\nEnergy: " + energy + "/" + maxEnergy;
    }
}