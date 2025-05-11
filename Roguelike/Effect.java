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
    private Integer duration;
    private int strength;
    private int chance;
    
    public Effect(EffectEnum effect, Integer duration, int strength){
        this.effect = effect;
        this.duration = duration;
        this.strength = strength;
        this.chance = 100;
    }
    
    public Effect(EffectEnum effect, Integer duration, int strength, int chance){
        this.effect = effect;
        this.duration = duration;
        this.strength = strength;
        this.chance = chance;
    }
    public int damage(Fighter opponent, Fighter target, int amount){
        amount = Math.max(amount, 0);
        if (opponent.hasRelic(19)){ // Pacifist
            amount = 0;
        }
        if (opponent.hasRelic(25)){
            amount *= Math.pow(2, opponent.countRelic(25));
        } else {
            amount *= Math.pow(2, target.countRelic(25));
        }
        if (opponent.hasRelic(26) && lib.randint(3) == 0){
            BattleManager.addMessage("Cat doubled the damage dealt");
            amount *= 2;
        }
        if (target.hasRelic(26) && lib.randint(3) == 0){
            BattleManager.addMessage("Cat doubled the damage taken");
            amount *= 2;
        } else if (target.hasRelic(26) && lib.randint(6) == 0){
            BattleManager.addMessage("Cat nullified the damage");
            amount = 0;
        }
        target.damage(amount);
        if (amount >= target.getMaxHealth()*3){
            BattleManager.addAchievement(1);
        }
        if (opponent.hasRelic(27)){
            new Effect(Effect.EffectEnum.POISON, null, opponent.countRelic(27)).apply(opponent, target);
        }
        return amount;
    }
    
    public int heal(Fighter opponent, Fighter target, int amount){
        target.heal(amount);
        return amount;
    }
    
    public boolean countdown(Fighter opponent, Fighter target){
        int amount;
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
                BattleManager.addMessage(target.getName() + " took " + damage(opponent, target, amount) + " damage");
                break;
            case HEAL:
                amount = strength;
                BattleManager.addMessage(target.getName() + " healed " + heal(opponent, target, amount) + " hp");
                break;
            case POISON:
                amount = strength--;
                if (target.hasRelic(28)){
                    amount = heal(opponent, target, amount);
                    BattleManager.addMessage(target.getName() + " healed " + amount + " health from the poison");
                } else {
                    amount = damage(opponent, target, amount);
                    BattleManager.addMessage(target.getName() + " took " + amount + " damge from the poison");
                }
                if (target.hasRelic(30)){
                    strength++;
                    strength *= 2;
                }
                if (target instanceof Player && target.hasRelic(31)){
                    ((Player)target).addCoins(amount);
                    BattleManager.addMessage(target.getName() + " received " + amount + " coins for the poison");
                }
                if (strength <= 0){
                    target.removeEffect(this);
                }
                break;
            case REGENERATION:
                amount = strength--;
                BattleManager.addMessage(target.getName() + " healed " + heal(opponent, target, amount) + " hp");
                break;
            case DRAW:
                amount = strength;
                amount = target.addAvailable(amount);
                if (amount == 1){
                    BattleManager.addMessage(target.getName() + " drew 1 attack");
                } else {
                    BattleManager.addMessage(target.getName() + " drew " + amount + " attacks");
                }
                if (opponent.hasRelic(1) && amount > 0){
                    BattleManager.addMessage(target.getName() + " took damage from the Cursed Hand");
                    new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2*amount).apply(opponent, target);
                }
                break;
            case HEALHALFMAX:
                amount = (int)Math.ceil(target.getMaxHealth()/2.0);
                 BattleManager.addMessage(target.getName() + " healed " + heal(opponent, target, amount) + " hp");
                break;
            case PERCENTDAMAGE:
                amount = Math.max((int)(target.getMaxHealth()*strength/100.0), 1);
                BattleManager.addMessage(target.getName() + " took " + damage(opponent, target, amount) + " damage");
                break;
            case ENERGY:
                amount = strength;
                target.restoreEnergy(amount);
                BattleManager.addMessage(target.getName() + " restored " + amount + " energy");
                break;
            case TRUEDAMAGE:
                amount = strength;
                damage(opponent, target, amount);
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
    
    public void apply(Fighter user, Fighter target){
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
                    if (user.hasRelic(29) && user.hasEffect(EffectEnum.POISON)){
                        Effect strength = user.getEffect(EffectEnum.POISON);
                        newEffect.setStrength(newEffect.getStrength()+strength.getStrength());
                        strength.setStrength(strength.getStrength()-1);
                        if (strength.getStrength() <= 0){
                            user.removeEffect(strength);
                        }
                    }
                    break;
                case POISON:
                    if (target.getName().equals("You")){
                        BattleManager.addMessage("You were inflicted with poison");
                    } else {
                        BattleManager.addMessage(target.getName() + " was inflicted with poison");
                    }
                    break;
                case REGENERATION:
                    BattleManager.addMessage(target.getName() + " received regeneration");
                    break;
                case BRITTLE:
                    BattleManager.addMessage(target.getName() + " became brittle");
                    break;
                case WEAK:
                    BattleManager.addMessage(target.getName() + " became weak");
                    break;
                case ARMOR:
                    BattleManager.addMessage(target.getName() + " received armor");
                    break;
                case STRENGTH:
                    BattleManager.addMessage(target.getName() + " received strength");
                    break;
                case STUN:
                    BattleManager.addMessage(target.getName() + " became stunned");
                    break;
            }
            target.addEffect(newEffect);
            switch (effect){
                case DAMAGE:
                    newEffect.countdown(user, target);
                    break;
                case HEAL:
                    newEffect.countdown(user, target);
                    break;
                case DRAW:
                    newEffect.countdown(user, target);
                    break;
                case HEALHALFMAX:
                    newEffect.countdown(user, target);
                    break;
                case PERCENTDAMAGE:
                    newEffect.countdown(user, target);
                    break;
                case ENERGY:
                    newEffect.countdown(user, target);
                    break;
                case TRUEDAMAGE:
                    newEffect.countdown(user, target);
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