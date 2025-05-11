import java.util.ArrayList;

public class ItemManager {
    
    public ArrayList<Item> items;
    
    public ItemManager() {
        items = new ArrayList<Item>();
    }
    
    public void addItem(Item item){
        items.add(new Item(item.getName(), item.getUsesTurn(), item.getUses(), item.getTargetEffects(), item.getSelfEffects(), item.getRarity(), item.getDescription(), item.getRequirement()));
        items.sort(null);
    }
    
    public Item getItem(int index){
        return items.get(index);
    }
    
    public ArrayList<Item> getItems(){
        return items;
    }
    
    public boolean useItem(int index, Fighter user, Fighter target){
        BattleManager.addMessage(user.getName() + " used " + items.get(index).getName());
        if (items.get(index).use(user, target)){
            return items.remove(index).getUsesTurn();
        } else {
            return items.get(index).getUsesTurn();
        }
    }
}