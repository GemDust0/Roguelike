import java.util.ArrayList;

public class EffectManager {
    
    private ArrayList<Effect> effects;
    
    public EffectManager(){
        effects = new ArrayList<Effect>();
    }
    
    public void addEffect(Effect effect){
        for (Effect.EffectEnum checkEffect : Effect.stackables){
            if (effect.getEffect() == checkEffect && hasEffect(checkEffect)){
                getEffect(checkEffect).addEffect(effect);
                return;
            }    
        }
        effects.add(effect);
    }
    
    public void removeEffect(Effect removeEffect){
        for (int i=0; i<effects.size(); i++){
            if (effects.get(i) == removeEffect){
                effects.remove(i);
                return;
            }
        }
    }
    
    public void clearEffects(){
        effects = new ArrayList<Effect>();
    }
    
    public boolean hasEffect(Effect.EffectEnum searchEffect){
        for (Effect effect : effects){
            if (effect.getEffect() == searchEffect){
                return true;
            }
        }
        return false;
    }
    
    public Effect getEffect(Effect.EffectEnum searchEffect){
        for (Effect effect : effects){
            if (effect.getEffect() == searchEffect){
                return effect;
            }
        }
        return null;
    }
    
    public int getEffectStrength(Effect.EffectEnum searchEffect){
        int total = 0;
        for (Effect effect : effects){
            if (effect.getEffect() == searchEffect){
                total += effect.getStrength();
            }
        }
        return total;
    }
    
    public ArrayList<Effect> getAllEffects(){
        return effects;
    }
    
    public void printEffects(){
        if (effects.size() == 0){
            System.out.println("None");
        } else {
            for (Effect effect : effects){
                System.out.println(effect);
            }
        }
    }
    
    public void countdown(Fighter opponent, Fighter target, BattleManager battle){
        for (int i=0; i<effects.size(); i++){
            // If effect removed; decrease index by 1
            boolean hasMessage = effects.get(i).hasMessage();
            i -= effects.get(i).countdown(opponent, target, battle) ? 1 : 0;
            if (hasMessage){
                lib.clear();
                battle.renderBattle();
                lib.getInput();
            }
        }
    }
}
