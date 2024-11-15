import java.util.ArrayList;

public class Enemy extends Fighter{
    
    public static enum Type {
        ATTACK,
        TANK
    }
    
    public void add(ArrayList<Integer> arraylist, int[] array){
        for (int i : array){
            arraylist.add(i);
        }
    }
    
    public Enemy(Type type, int depth, boolean boss, Player player){
        super(null, 0, 0);
        double healthMult = Math.pow(1.2, depth);
        if (player.hasRelic(24)){
            healthMult *= 1.5;
        }
        int relicCount = depth;
        int extraAttackCount = depth*2;
        int energy = 5;
        
        if (boss){
            healthMult *= 3;
            relicCount = (relicCount+1)*2;
            extraAttackCount = (extraAttackCount+1)*2;
            energy *= 2;
        }
        
        String[] availableNames = new String[] {};
        ArrayList<Integer> availableAttacks = new ArrayList<Integer>();
        ArrayList<Integer> availableRelics = new ArrayList<Integer>();
        
        switch (type){
            case ATTACK:
                availableNames = new String[] {"Atta"};
                
                add(availableAttacks, new int[] {0, 0, 3, 3, 5, 6, 7});
                availableRelics.add(1);
                
                if (depth >= 2){
                    availableRelics.add(5);
                }
                
                if (depth >= 3){
                    add(availableAttacks, new int[] {5, 5, 8});
                }
                
                if (depth >= 5){
                    add(availableAttacks, new int[] {8, 8});
                    availableAttacks.remove(0); // Remove the 2 strikes
                    availableAttacks.remove(0);
                    add(availableRelics, new int[] {5, 8});
                }
                
                if (depth >= 8) {
                    add(availableRelics, new int[] {8, 8, 8, 8});
                }
                
                setMaxHealth((int)(15*healthMult));
                addAttack(AttackList.attacks[0]);
                addAttack(AttackList.attacks[0]);
                addAttack(AttackList.attacks[5]);
                break;
            
            case TANK:
                availableNames = new String[] {"Amor"};
                
                add(availableAttacks, new int[] {0, 0, 1, 2, 2, 2, 3, 9, 9, 9});
                add(availableRelics, new int[] {1, 4, 4, 4, 20, 20, 21});
                
                if (depth >= 2){
                    add(availableRelics, new int[] {6, 6, 6, 7, 21, 21, 22});
                }
                
                if (depth >= 5){
                    add(availableAttacks, new int[] {8, 8});
                    availableAttacks.remove(0); // Remove Strikes
                    availableAttacks.remove(0);
                    add(availableRelics, new int[] {9, 9, 10, 22, 22});
                    availableRelics.remove(1); // Remove Chicken Wings
                    availableRelics.remove(1);
                    availableRelics.remove(1);
                }
                
                if (depth >= 9){
                    add(availableRelics, new int[] {23, 23, 23, 23});
                    availableRelics.remove(1); // Remove Hot Dogs
                    availableRelics.remove(1);
                }
                
                setMaxHealth((int)(20*healthMult));
                addAttack(AttackList.attacks[0]);
                addAttack(AttackList.attacks[2]);
                addAttack(AttackList.attacks[2]);
                addAttack(AttackList.attacks[9]);
                break;
            
            default:
                availableNames = new String[] {"Null"};
                setMaxHealth((int)(9999*healthMult));
        }
        
        setName(availableNames[lib.randint(availableNames.length)]);
        
        if (boss){
            addAttack(AttackList.attacks[8]);
        }
        
        for (int i=0; i<extraAttackCount; i++){
            addAttack(AttackList.attacks[availableAttacks.get(lib.randint(availableAttacks.size()))]);
        }
        
        for (int i=0; i<relicCount; i++){
            ArrayList<Relic> tempRelics = new ArrayList<Relic>();
            for (int id : availableRelics){
                Relic relic = RelicList.relics[id];
                if (relic.getRarity() != Shop.Rarity.UNIQUE || !hasRelic(relic.getId())){
                    tempRelics.add(relic);
                }
            }
            if (tempRelics.size() > 0){
                        addRelic(tempRelics.get(lib.randint(tempRelics.size())));
            } else {
                break;
            }
        }
        
        heal();
        setMaxEnergy(energy);
        restoreEnergy();
    }
    
    public String getSprite(){
        return Sprites.humanoids[1];
    }
}
