import java.util.ArrayList;

public class Shop {
    
    public static enum Rarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        STELLAR,
        UNIQUE
    }
    private static double priceFactor = 1.4;
    private static double depthFactor = 1.6;
    
    private Player player;
    private String name;
    private int attackCount;
    private int itemCount;
    private int relicCount;
    private ArrayList<Attack> attacks;
    private ArrayList<Item> items;
    private ArrayList<Relic> relics;
    private Map map;
    
    public Shop(Player player, String name, int attackCount, int itemCount, int relicCount, Map map){
        this.player = player;
        this.name = name;
        this.attackCount = attackCount;
        this.itemCount = itemCount;
        this.relicCount = relicCount;
        this.map = map;
    }
    
    public static Rarity generateRarity(){
        int num = lib.randint(100);
        if (num < 1){
            return Rarity.STELLAR;
        } else if (num < 6){
            return Rarity.LEGENDARY;
        } else if (num < 16) {
            return Rarity.EPIC;
        } else if (num < 31) {
            return Rarity.RARE;
        } else if (num < 46) {
            return Rarity.UNIQUE;
        } else if (num < 71) {
            return Rarity.UNCOMMON;
        } else {
            return Rarity.COMMON;
        }
    }
    
    public int getPriceMult(Rarity rarity){
        switch (rarity){
            case COMMON:
                return 1;
            case UNCOMMON:
                return 3;
            case RARE:
                return 5;
            case EPIC:
                return 10;
            case LEGENDARY:
                return 15;
            case STELLAR:
                return 25;
            case UNIQUE:
                return 15;
        }
        return 9999;
    }
    
    public static String getRenCol(Rarity rarity){
        switch (rarity){
            case COMMON:
                return lib.fore(255, 255, 255);
            case UNCOMMON:
                return lib.fore(0, 255, 0);
            case RARE:
                return lib.fore(0, 0, 255);
            case EPIC:
                return lib.fore(255, 0, 255);
            case LEGENDARY:
                return lib.fore(200, 200, 100);
            case STELLAR:
                return lib.fore(150, 30, 200);
            case UNIQUE:
                return lib.fore(255, 0, 0);
        }
        return lib.reset;
    }
    
    public int calculatePrice(){
        return (int)(25*Math.pow(priceFactor, Math.pow(depthFactor, map.getDepth())-1));
    }
    
    public void restock(){
        attacks = new ArrayList<Attack>();
        items = new ArrayList<Item>();
        relics = new ArrayList<Relic>();
        for (int i=0; i<attackCount; i++){
            try {
                Rarity rarity = generateRarity();
                ArrayList<Attack> tempAttacks = new ArrayList<Attack>();
                for (Attack attack : AttackList.attacks){
                    if (attack.getRarity() == rarity && Achievements.hasAchievement(attack.getRequirement())){
                        tempAttacks.add(attack);
                    }
                }
                attacks.add(tempAttacks.get(lib.randint(tempAttacks.size())));
            } catch (Exception e) {
                i--;
            }
        }
        for (int i=0; i<itemCount; i++){
            try {
                Rarity rarity = generateRarity();
                ArrayList<Item> tempItems = new ArrayList<Item>();
                for (Item item : ItemList.items){
                    if (item.getRarity() == rarity && Achievements.hasAchievement(item.getRequirement())){
                        tempItems.add(item);
                    }
                }
                items.add(tempItems.get(lib.randint(tempItems.size())));
            } catch (Exception e) {
                i--;
            }
        }
        for (int i=0; i<relicCount; i++){
            try {
                Rarity rarity = generateRarity();
                ArrayList<Relic> tempRelics = new ArrayList<Relic>();
                for (Relic relic : RelicList.relics){
                    if (relic.getRarity() == rarity  && Achievements.hasAchievement(relic.getRequirement())){
                        if (rarity == Rarity.UNIQUE){
                            if (!player.hasRelic(relic.getId())){
                                boolean alreadyIn = false;
                                for (Relic relicCheck : relics){
                                    if (relic.getId() == relicCheck.getId()){
                                        alreadyIn = true;
                                        break;
                                    }
                                }
                                if (!alreadyIn){
                                    tempRelics.add(relic);
                                }
                            }
                        } else{
                            tempRelics.add(relic);
                        }
                    }
                }
                relics.add(tempRelics.get(lib.randint(tempRelics.size())));
            } catch (Exception e) {
                i--;
            }
        }
    }
    
    public static void renderName(String name){
        // Don't question that which works.
            String[] display = new String[] {"|    ", "|    ", "|    ", "|    ", "|    "};
            
            for (int i=0; i<name.length(); i++){
                String curLet = Sprites.letters[(int)name.toLowerCase().charAt(i)-97];
                String[] letLines = curLet.split("\n");
                for (int j=0; j<5; j++){
                    display[j] += letLines[j] + " ";
                }
            }
            System.out.print("|");
            for (int i=0; i<display[0].length(); i++){
                System.out.print("-");
            }
            System.out.println("--|");
            for (String curLine : display){
                System.out.println(curLine + "   |");
            }
            System.out.print("|");
            for (int i=0; i<display[0].length(); i++){
                System.out.print("-");
            }
            System.out.println("--|");
            // Cuz it works..
    }
    
    public void open(){
        int restockCost = calculatePrice()*2;
        restock();
        while (true){
            try {
                lib.clear();
                renderName(name);
                ArrayList<Integer> menus = new ArrayList<Integer>();
                if (attacks.size() != 0){
                    menus.add(0);
                    System.out.println(menus.size() + ". Attacks");
                }
                if (items.size() != 0){
                    menus.add(1);
                    System.out.println(menus.size() + ". Items");
                }
                if (relics.size() != 0){
                    menus.add(2);
                    System.out.println(menus.size() + ". Relics");
                } else if (menus.size() == 0){
                    Achievements.giveAchievement(2);
                    lib.clear();
                    renderName(name);
                }
                if (player.getAttacks().size() > 1){
                    menus.add(3);
                    System.out.println(menus.size() + ". Remove attacks");
                }
                menus.add(4);
                System.out.println(menus.size() + ". Restock shop for " + restockCost + " coins");
                menus.add(5);
                System.out.println(menus.size() + ". Continue");
                System.out.println("\nCoins: "+ player.getCoins() +"\n");
                switch (menus.get(Integer.parseInt(lib.getInput("=> "))-1)){
                    case 0:
                        attackMenu();
                        break;
                    case 1:
                        itemMenu();
                        break;
                    case 2:
                        relicMenu();
                        break;
                    case 3:
                        removeMenu();
                        break;
                    case 4:
                        if (player.getCoins() >= restockCost){
                            restock();
                            player.removeCoins(restockCost);
                        }
                        break;
                    case 5:
                        return;
                }
            } catch (Exception e){}
        }
    }
    
    public void attackMenu(){
        int c = 0;
        while (true){
            try {
                lib.clear();
                renderName("Attacks");
                int[] prices = new int[attacks.size()];
                for (int i=0; i<attacks.size(); i++){
                    String renCol = getRenCol(attacks.get(i).getRarity());
                    int priceMult = getPriceMult(attacks.get(i).getRarity());
                    prices[i] = calculatePrice()*priceMult;
                    System.out.println(i+1 + ". " + renCol + attacks.get(i) + ": " + prices[i] + lib.reset);
                }
                System.out.println((prices.length+1) + ". Back");
                System.out.println("\nCoins: "+ player.getCoins() +"\n");
                c = 0; // Prevents exiting by entering an invalid string after purchasing the last option
                c = Integer.parseInt(lib.getInput("=> "))-1;
                if (player.getCoins() >= prices[c]){
                    player.addAttack(attacks.remove(c));
                    player.removeCoins(prices[c]);
                    if (attacks.size() == 0){
                        return;
                    }
                }
            } catch (Exception e){
                if (c==attacks.size()){
                    return;
                }
            }
        }
    }
    
    public void itemMenu(){
        int c = 0;
        while (true){
            try {
                lib.clear();
                renderName("Items");
                int[] prices = new int[items.size()];
                for (int i=0; i<items.size(); i++){
                    String renCol = getRenCol(items.get(i).getRarity());
                    int priceMult = getPriceMult(items.get(i).getRarity());
                    prices[i] = calculatePrice()*priceMult;
                    System.out.println(i+1 + ". " + renCol + items.get(i).getName() + ": " + prices[i] + lib.reset);
                }
                System.out.println((prices.length+1) + ". Back");
                System.out.println("\nCoins: "+ player.getCoins() +"\n");
                c = 0; // Prevents exiting by entering an invalid string after purchasing the last option
                c = Integer.parseInt(lib.getInput("=> "))-1;
                if (player.getCoins() >= prices[c]){
                    player.addItem(items.remove(c));
                    player.removeCoins(prices[c]);
                    if (items.size() == 0){
                        return;
                    }
                }
            } catch (Exception e){
                if (c==items.size()){
                    return;
                }
            }
        }
    }
    
    public void relicMenu(){
        int c = 0;
        while (true){
            try {
                lib.clear();
                renderName("Relics");
                int[] prices = new int[relics.size()];
                for (int i=0; i<relics.size(); i++){
                    String renCol = getRenCol(relics.get(i).getRarity());
                    int priceMult = getPriceMult(relics.get(i).getRarity());
                    prices[i] = calculatePrice()*priceMult;
                    System.out.println(i+1 + ". " + renCol + relics.get(i) + ": " + prices[i] + lib.reset);
                }
                System.out.println((prices.length+1) + ". Back");
                System.out.println("\nCoins: "+ player.getCoins() +"\n");
                c = 0; // Prevents exiting by entering an invalid string after purchasing the last option
                c = Integer.parseInt(lib.getInput("=> "))-1;
                if (player.getCoins() >= prices[c]){
                    player.addRelic(relics.remove(c));
                    player.removeCoins(prices[c]);
                    if (relics.size() == 0){
                        return;
                    }
                }
            } catch (Exception e){
                if (c==relics.size()){
                    return;
                }
            }
        }
    }
    
    public void removeMenu(){
        int cost = calculatePrice();
        while (true){
            int c=-1;
            try {
                if (player.getAttacks().size() <= 1){
                    Achievements.giveAchievement(3);
                    return;
                }
                lib.clear();
                renderName("Remove");
                System.out.println("Remove an attack for: " + cost);
                for (int i=0; i<player.getAttacks().size(); i++){
                    System.out.println((i+1) + ". " + getRenCol(player.getAttacks().get(i).getRarity()) + player.getAttacks().get(i) + lib.reset);
                }
                System.out.println((player.getAttacks().size()+1) + ". Back");
                System.out.println("\nCoins: "+ player.getCoins() +"\n");
                c = Integer.parseInt(lib.getInput("=> "))-1;
                if (c == player.getAttacks().size()){
                    return;
                }
                if (player.getCoins() >= cost){
                    player.removeAttack(c);
                    player.removeCoins(cost);
                }
            } catch (Exception e){}
        }
    }
}
