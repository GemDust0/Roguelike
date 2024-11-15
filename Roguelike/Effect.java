public class Effect {
    
    public static enum EffectEnum {
        DAMAGE,
        HEAL,
        STRENGTH,
        WEAK,
        ARMOR,
        BRITTLE,
        DRAW,
        REGENERATION,
        POISON,
        HEALHALFMAX,
        PERCENTDAMAGE,
        STUN,
        STUNIMMUNE,
        ENERGY,
        TRUEDAMAGE
    }
    
    public static Effect.EffectEnum[] stackables = new Effect.EffectEnum[] {
        Effect.EffectEnum.POISON,
        Effect.EffectEnum.REGENERATION,
        Effect.EffectEnum.BRITTLE,
        Effect.EffectEnum.ARMOR,
        Effect.EffectEnum.WEAK,
        Effect.EffectEnum.STRENGTH
    };
    
    private EffectEnum effect;
    private boolean countdownMessage;
    private Integer duration;
    private int strength;
    private int chance;
    
    public Effect(EffectEnum effect, Integer duration, int strength){
        this.effect = effect;
        this.duration = duration;
        this.strength = strength;
        this.chance = 100;
        countdownMessage();
    }
    
    public Effect(EffectEnum effect, Integer duration, int strength, int chance){
        this.effect = effect;
        this.duration = duration;
        this.strength = strength;
        this.chance = chance;
        countdownMessage();
    }
    
    public void countdownMessage(){
        switch (effect){
            case DAMAGE:
                countdownMessage = true;
                break;
            case HEAL:
                countdownMessage = true;
                break;
            case POISON:
                countdownMessage = true;
                break;
            case REGENERATION:
                countdownMessage = true;
                break;
            case DRAW:
                countdownMessage = true;
                break;
            case HEALHALFMAX:
                countdownMessage = true;
                break;
            case PERCENTDAMAGE:
                countdownMessage = true;
                break;
            case ENERGY:
                countdownMessage = true;
                break;
            case TRUEDAMAGE:
                countdownMessage = true;
                break;
            default:
                countdownMessage = false;
        }
    }
    
    public void damage(Fighter opponent, Fighter target, BattleManager battle, int amount){
        amount = Math.max(amount, 0);
        if (opponent.hasRelic(19)){ // Pacifist
            amount = 0;
        }
        if (opponent.hasRelic(25)){
            amount *= Math.pow(2, opponent.countRelic(25));
        } else {
            amount *= Math.pow(2, target.countRelic(25));
        }
        target.damage(amount);
        battle.addMessage(target.getName() + " took " + amount + " damage");
        if (amount >= target.getMaxHealth()*3){
            battle.addAchievement(1);
        }
    }
    
    public void heal(Fighter opponent, Fighter target, BattleManager battle, int amount){
        target.heal(amount);
        battle.addMessage(target.getName() + " healed " + amount + " hp");
    }
    
    public boolean countdown(Fighter opponent, Fighter target, BattleManager battle){
        int amount;
        double decamount;
        switch (effect){
            case DAMAGE:
                amount = strength;
                if (target.hasEffect(EffectEnum.BRITTLE)){
                    Effect brittle = target.getEffect(EffectEnum.BRITTLE);
                    amount += brittle.getStrength();
                    brittle.setStrength(brittle.getStrength()-1);
                    if (brittle.getStrength() <= 0){
                        target.removeEffect(brittle);
                    }
                }
                if (target.hasEffect(EffectEnum.ARMOR)){
                    Effect armor = target.getEffect(EffectEnum.ARMOR);
                    amount -= armor.getStrength();
                    armor.setStrength(armor.getStrength()-1);
                    if (armor.getStrength() <= 0){
                        target.removeEffect(armor);
                    }
                }
                damage(opponent, target, battle, amount);
                break;
            case HEAL:
                amount = strength;
                heal(opponent, target, battle, amount);
                break;
            case POISON:
                amount = strength--;
                target.damage(amount);
                battle.addMessage(target.getName() + " took " + amount + " damge from the poison");
                if (strength <= 0){
                    target.removeEffect(this);
                }
                break;
            case REGENERATION:
                amount = strength;
                heal(opponent, target, battle, amount);
                break;
            case DRAW:
                amount = strength;
                amount = target.addAvailable(amount);
                if (amount == 1){
                    battle.addMessage(target.getName() + " drew 1 attack");
                } else {
                    battle.addMessage(target.getName() + " drew " + amount + " attacks");
                }
                if (opponent.hasRelic(1)){
                    new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2*amount).apply(opponent, target, battle);
                }
                break;
            case HEALHALFMAX:
                amount = (int)Math.ceil(target.getMaxHealth()/2.0);
                heal(opponent, target, battle, amount);
                break;
            case PERCENTDAMAGE:
                amount = Math.max((int)(target.getMaxHealth()*strength/100.0), 1);
                damage(opponent, target, battle, amount);
                break;
            case ENERGY:
                amount = strength;
                target.restoreEnergy(amount);
                battle.addMessage(target.getName() + " restored " + amount + " energy");
                break;
            case TRUEDAMAGE:
                amount = strength;
                damage(opponent, target, battle, amount);
                break;
        }
        if (duration != null){
            duration--;
            if (duration <= 0){
                target.removeEffect(this); // Fail to remove on enemy attack???
                if (effect == EffectEnum.STUN){
                    target.addEffect(new Effect(EffectEnum.STUNIMMUNE, 3, 1));
                }
                return true;
            }
        }
        return false;
    }
    
    public void apply(Fighter user, Fighter target, BattleManager battle){
        if (effect == EffectEnum.STUN && (target.hasEffect(EffectEnum.STUNIMMUNE) || target.hasEffect(EffectEnum.STUN))){
            return;
        }
        if (lib.randint(100) < chance){
            Effect newEffect = new Effect(effect, duration, strength);
            switch (effect){
                case DAMAGE:
                    if (user.hasEffect(EffectEnum.WEAK)){
                        Effect weak = user.getEffect(EffectEnum.WEAK);
                        newEffect.setStrength(newEffect.getStrength()-weak.getStrength());
                        weak.setStrength(weak.getStrength()-1);
                        if (weak.getStrength() <= 0){
                            user.removeEffect(weak);
                        }
                    }
                    if (user.hasEffect(EffectEnum.STRENGTH)){
                        Effect strength = user.getEffect(EffectEnum.STRENGTH);
                        newEffect.setStrength(newEffect.getStrength()+strength.getStrength());
                        strength.setStrength(strength.getStrength()-1);
                        if (strength.getStrength() <= 0){
                            user.removeEffect(strength);
                        }
                    }
                    break;
                case POISON:
                    if (target.getName().equals("You")){
                        battle.addMessage("You were inflicted with poison");
                    } else {
                        battle.addMessage(target.getName() + " was inflicted with poison");
                    }
                    break;
                case REGENERATION:
                    battle.addMessage(target.getName() + " received regeneration");
                    break;
                case BRITTLE:
                    battle.addMessage(target.getName() + " became brittle");
                    break;
                case WEAK:
                    battle.addMessage(target.getName() + " became weak");
                    break;
                case ARMOR:
                    battle.addMessage(target.getName() + " received armor");
                    break;
                case STRENGTH:
                    battle.addMessage(target.getName() + " received strength");
                    break;
                case STUN:
                    battle.addMessage(target.getName() + " became stunned");
                    break;
            }
            target.addEffect(newEffect);
            switch (effect){
                case DAMAGE:
                    newEffect.countdown(user, target, battle);
                    break;
                case HEAL:
                    newEffect.countdown(user, target, battle);
                    break;
                case DRAW:
                    newEffect.countdown(user, target, battle);
                    break;
                case HEALHALFMAX:
                    newEffect.countdown(user, target, battle);
                    break;
                case PERCENTDAMAGE:
                    newEffect.countdown(user, target, battle);
                    break;
                case ENERGY:
                    newEffect.countdown(user, target, battle);
                    break;
                case TRUEDAMAGE:
                    newEffect.countdown(user, target, battle);
                    break;
            }
        }
    }
    
    public void addEffect(Effect effect){
        strength += effect.getStrength();
    }
    
    public void setStrength(int strength){
        this.strength = strength;
    }
    
    public EffectEnum getEffect(){
        return effect;
    }
    
    public int getDuration(){
        return duration;
    }
    
    public int getStrength(){
        return strength;
    }
    
    public boolean hasMessage(){
        return countdownMessage;
    }
    
    public String toString(){
        switch (effect){
            case POISON:
                return "Poison " + strength;
            case REGENERATION:
                return "Regeneration " + strength;
            case BRITTLE:
                return "Brittle " + strength;
            case WEAK:
                return "Weak " + strength;
            case ARMOR:
                return "Armor " + strength;
            case STRENGTH:
                return "Strength " + strength;
            case STUN:
                return "Stun: " + duration + " turns";
            case STUNIMMUNE:
                return "Stun Immunity: " + duration + " turns";
        }
        return null;
    }
}
