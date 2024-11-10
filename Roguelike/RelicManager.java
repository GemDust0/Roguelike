import java.util.ArrayList;

public class RelicManager {
    private ArrayList<Relic> relics;
    
    public RelicManager(){
        relics = new ArrayList<Relic>();
    }
    
    public ArrayList<Relic> getRelics(){
        return relics;
    }
    
    public boolean hasRelic(int id){
        for (Relic relic : relics){
            if (relic.getId() == id){
                return true;
            }
        }
        return false;
    }
    
    public int countRelic(int id){
        int ret = 0;
        for (Relic relic : relics){
            if (relic.getId() == id){
                ret++;
            }
        }
        return ret;
    }
    
    public void addRelic(Relic relic){
        relics.add(new Relic(relic.getName(), relic.getId(), relic.getRarity(), relic.getDescription(), relic.getRequirement()));
    }
    
    public void printRelics(){
        if (relics.size() == 0){
            System.out.println("None");
        } else {
            for (int i=0; i<RelicList.relics.length; i++){
                if (hasRelic(i)){
                    System.out.println(RelicList.relics[i] + " x" + countRelic(i));
                }
            }
        }
    }
    
    public void removeRelic(int index){
        relics.remove(index);
    }
    
    public Relic getRelic(int index){
        return relics.get(index);
    }
}
