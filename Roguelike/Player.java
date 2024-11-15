import java.util.ArrayList;

public class Player extends Fighter{
    
    // Takes place in Lunalion; currency: Lunas
    private int coins; // Symbol: ☾
    private ItemManager items;
    private int itemsUsed;
    
    public Player(int maxHealth, int maxEnergy){
        super("You", maxHealth, maxEnergy);
        items = new ItemManager();
    }
    
    public Player(int maxHealth, int curHealth, ArrayList<Attack> attacks, ArrayList<Item> items, ArrayList<Relic> relics, int coins, int maxEnergy){
        super("You", maxHealth, maxEnergy);
        setCurHealth(curHealth);
        for (Attack attack : attacks){
            addAttack(attack);
        }
        this.items = new ItemManager();
        for (Item item : items){
            addItem(item);
        }
        for (Relic relic : relics){
            addRelic(relic);
        }
        this.coins = coins;
    }
    
    public void addCoins(int coins){
        if (hasRelic(24)){
            coins *= 2;
            lib.getInput("Economist doubled the amount of coins you earned");
        }
        this.coins += coins;
    }
    
    public void removeCoins(int amount){
        this.coins -= amount;
    }
    
    public int getCoins(){
        return coins;
    }
    
    public void addItem(Item item){
        items.addItem(item);
    }
    
    public ArrayList<Item> getItems(){
        return items.getItems();
    }
    
    public void resetItemCount(){
        itemsUsed = 0;
    }
    
    public boolean useItem(int index, Fighter target, BattleManager battle){
        if (items.useItem(index, this, target, battle)){
            itemsUsed++;
        }
        reduceEnergy();
        return itemsUsed > countRelic(0) || getEnergy() == 0;
    }
    
    public boolean chooseItem(Fighter target, BattleManager battle){
        if (getCurHealth() == 0 || target.getCurHealth() == 0){
            return true;
        }
        if (items.getItems().size() == 0){
            return false;
        }
        String c = null;
        battle.clearMessages();
        while (true){
            lib.clear();
            battle.renderBattle();
            System.out.println("Pick an item, enter nothing to return to the menu:");
            for (int i=0; i<items.getItems().size(); i++){
                System.out.println((i+1) + ". " + items.getItems().get(i));
            }
            try {
                c = lib.getInput("\n=> ");
                if (Integer.parseInt(c) > 0 && Integer.parseInt(c) <= items.getItems().size()){
                    return useItem(Integer.parseInt(c)-1, target, battle) ? true : chooseItem(target, battle);
                }
            } catch (Exception e){
                if (c.equals("")){
                    return false;
                }
            }
        }
    }
    
    public String getSprite(){
        return Sprites.humanoids[0];
    }
}
