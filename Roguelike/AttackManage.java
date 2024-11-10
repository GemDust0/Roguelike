import java.util.ArrayList;

public class AttackManager {
    
    public ArrayList<Attack> attacks;
    public ArrayList<Attack> unavailableAttacks;
    public ArrayList<Attack> availableAttacks;
    
    public AttackManager() {
        attacks = new ArrayList<Attack>();
        unavailableAttacks = new ArrayList<Attack>();
        availableAttacks = new ArrayList<Attack>();
    }
    
    public ArrayList<Attack> getAttacks(){
        return attacks;
    }
    
    public void removeAttack(int index){
        attacks.remove(index);
    }
    
    public void addAttack(Attack attack){
        attacks.add(attack);
    }
    
    public void removeAvailableAttack(int index){
        availableAttacks.remove(index);
    }
    
    public Attack getAvailableAttack(int index){
        return availableAttacks.get(index);
    }
    
    public ArrayList<Attack> getAvailable(){
        return availableAttacks;
    }
    
    public void resetAvailable(int maxAmount){
        // Clear availableAttacks
        while (availableAttacks.size() > 0){
            availableAttacks.remove(0);
        }
        
        // Reset unavailableAttacks
        while (unavailableAttacks.size() > 0){
            unavailableAttacks.remove(0);
        }
        
        for (int i=0; i<attacks.size(); i++){
            unavailableAttacks.add(attacks.get(i));
        }
        
        for (int i=0; i<maxAmount; i++){
            try {
                availableAttacks.add(unavailableAttacks.remove(lib.randint(unavailableAttacks.size())));
            } catch (Exception e) {
                return;
            }
        }
    }
    
    public int addAvailable(int amount){
        int i;
        for (i=0; i<amount; i++){
            try {
                availableAttacks.add(unavailableAttacks.remove(lib.randint(unavailableAttacks.size())));
            } catch (Exception e) {
                break;
            }
        }
        return i;
    }
    
    public boolean useAttack(int index, Fighter user, Fighter target, BattleManager battle){
        availableAttacks.get(index).use(user, target, battle);
        unavailableAttacks.add(availableAttacks.remove(index));
        if (!unavailableAttacks.get(unavailableAttacks.size()-1).usesTurn() && availableAttacks.size() == 0){
            return true;
        }
        return unavailableAttacks.get(unavailableAttacks.size()-1).usesTurn();
    }
}
