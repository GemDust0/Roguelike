import java.util.ArrayList;

public class Enemy extends Fighter{
    
    public static enum Type {
        ATTACK,
        TANK
    }
    
    public Enemy(Type type, int depth, boolean boss){
        super(null, 0, 0);
        double healthMult = Math.pow(1.2, depth);
        int relicCount = depth;
        int extraAttackCount = depth*2;
        int energy = 5;
        
        if (boss){
            healthMult *= 3;
            relicCount = (relicCount+1)*2;
            extraAttackCount = (extraAttackCount+1)*2;
            addAttack(AttackList.attacks[8]);
            energy *= 2;
        }
        
        String[] availableNames = new String[] {};
        int[] availableAttacks = new int[] {};
        int[] availableRelics = new int[] {};
        
        switch (type){
            case ATTACK:
                availableNames = new String[] {"Atta"};
                availableAttacks = new int[] {0, 0, 3, 3, 5, 6, 7};
                availableRelics = new int[] {1};
                setMaxHealth((int)(15*healthMult));
                addAttack(AttackList.attacks[0]);
                addAttack(AttackList.attacks[0]);
                addAttack(AttackList.attacks[5]);
                break;
            case TANK:
                availableNames = new String[] {"Amor"};
                availableAttacks = new int[] {0, 0, 1, 2, 2, 2, 3, 9, 9, 9};
                availableRelics = new int[] {1, 4, 4, 4, 12, 13};
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
        
        for (int i=0; i<extraAttackCount; i++){
            addAttack(AttackList.attacks[availableAttacks[lib.randint(availableAttacks.length)]]);
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
