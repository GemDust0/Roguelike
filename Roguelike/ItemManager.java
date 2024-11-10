import java.util.ArrayList;

public class ItemManager {
    
    public ArrayList<Item> items;
    
    public ItemManager() {
        items = new ArrayList<Item>();
    }
    
    public void addItem(Item item){
        items.add(new Item(item.getName(), item.getUsesTurn(), item.getUses(), item.getTargetEffects(), item.getSelfEffects(), item.getRarity(), item.getDescription(), item.getRequirement()));
    }
    
    public Item getItem(int index){
        return items.get(index);
    }
    
    public ArrayList<Item> getItems(){
        return items;
    }
    
    public boolean useItem(int index, Fighter user, Fighter target, BattleManager battle){
        battle.addMessage(user.getName() + " used " + items.get(index).getName());
        lib.clear();
        battle.renderBattle();
        lib.getInput();
        if (items.get(index).use(user, target, battle)){
            return items.remove(index).getUsesTurn();
        } else {
            return items.get(index).getUsesTurn();
        }
    }
}
