import java.util.ArrayList;

public class Map {
    public Shop[] shops;
    
    private static int[] choiceArray = {
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        4,
        5
    };
    
    private ArrayList<Integer> map;
    private int curPos;
    private int depth;
    private String[] icons = {
        "☐",
        "⚔",
        "☠",
        "☾",
        "⍚",
        "?"
    };
    
    public Map(int size, Player player){
        depth = -1;
        generateMap(size);
        shops = new Shop[] {
            new Shop(player, "Dojo", 5, 2, 1, this),
            new Shop(player, "Museum", 2, 1, 5, this),
            new Shop(player, "Market", 3, 3, 2, this)};
    }
    
    public Map(ArrayList<Integer> map, Player player){
        this.map = map;
        shops = new Shop[] {new Shop(player, "Dojo", 5, 2, 1, this), new Shop(player, "Museum", 2, 1, 5, this), new Shop(player, "Market", 3, 3, 2, this)};
    }
    
    public void generateMap(int size){
        map = new ArrayList<Integer>();
        curPos = 0;
        map.add(0);
        for (int i=0; i<size; i++){
            if ((i+1)%6==0 && i!=0){
                map.add(3);
            } else {
                map.add(choiceArray[lib.randint(choiceArray.length)]);
            }
        }
        map.add(3);
        map.add(2);
        depth++;
        if (depth >= 20){
            Achievements.giveAchievement(6);
        }
    }
    
    public int moveNext(){
        curPos++;
        if (curPos >= map.size()){
            generateMap(map.size());
            return -1;
        }
        map.set(curPos-1, 0);
        return map.get(curPos);
    }
    
    public int getDepth(){
        return depth;
    }
    
    public int getCur(){
        return curPos;
    }
    
    public void setDepth(int depth){
        this.depth = depth;
    }
    
    public void setCur(int cur){
        curPos = cur;
    }
    
    public ArrayList<Integer> getMap(){
        return map;
    }
    
    public void displayMap(int displayRange, int offset){
        // Displays the map until the current iteration exceeds the displayRange by checking if the current iteration => startingValue-displayRange
        int startVal = lib.clamp(map.size()-1+offset, 0, curPos+displayRange);
        for (int i=Math.max(startVal-displayRange-offset, 0)+displayRange; i>=Math.max(startVal-displayRange-offset, 0); i--){
            if (i==curPos){
                System.out.println(lib.fore(255, 255, 0) + lib.bold + "★" + lib.reset);
            } else {
                System.out.println(icons[map.get(i)]);
            }
        }
    }
}
