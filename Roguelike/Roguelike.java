/*
    NOTICE
        If you want your save to carry over after closing the tab, you need to fork the program and run it in your own CodeHS sandbox.
        To trigger the tutorial delete achievements.txt
        Read patch.txt for patch notes
*/

import java.util.Random;
import java.util.ArrayList;
import java.io.File;

public class Roguelike {
    private static String[] options = {"Continue", "Save and quit"};
    private static String saveFile = "save.txt";
    private static String achievementFile = "achievements.txt";
    private static String patchFile = "patch.txt";
    private static String patch = FileLib.readLine(patchFile, 1);
    
    private static Map map;
    private static Player player;
    
    public static void main(String[] args) {
        try {
            loadAchievements();
        } catch (Exception e){
            saveAchievements();
            tutorial();
        }
        boolean quit = false;
        while (!quit){
            try{
                lib.clear();
                System.out.println(patch);
                ArrayList<Integer> options = new ArrayList<Integer>();
                options.add(0);
                System.out.println("\n1. New Run");
                if (new File(saveFile).exists()){
                    options.add(1);
                    System.out.println("2. Load Run");
                }
                options.add(2);
                System.out.println(options.size() + ". Achievements");
                options.add(3);
                System.out.println(options.size() + ". Options");
                options.add(4);
                System.out.println(options.size() + ". Save and quit");
                switch (options.get(Integer.parseInt(lib.getInput("\n=> "))-1)){
                    case 0:
                        startAdventure();
                        break;
                    case 1:
                        loadAdventure();
                        break;
                    case 2:
                        lib.clear();
                        Achievements.printAchievements();
                        break;
                    case 3:
                        optionsMenu();
                        break;
                    case 4:
                        saveAchievements();
                        quit = true;
                        break;
                }
            } catch (Exception e){}
        }
    }
    
    public static void tutorial(){
        lib.clear();
        lib.getInput("Welcome to Lunallis, a once great world, now overrun by the vile. (Press enter to continue)");
        lib.clear();
        lib.getInput("It is your assigned duty to try rid the world of these creatures, and whilst by many deemed impossible, perhaps you will be what changes everyone's mind?");
        lib.clear();
        lib.getInput("I applaud you for your courage, and wish you good luck on your travel, I will now be transferring you to the captain.");
        lib.clear();
        lib.getInput("Good morning, courageous soldier, willing to give your life to take back the once great world of Lunallis, however no matter your bravery, you will need to be trained on how to survive and thrive in this place.");
        lib.clear();
        lib.getInput("First off is the currency, Lunas, or as we prefer to simply call them, coins, is an important thing. Due to the planet's nature some of the few who come are merchants who desire to form a monopoly with the local hellspawn. They will offer you goods in exchange for these so called Lunas, so be sure to obtain a lot of them.");
        lib.clear();
        lib.getInput("Second is your hp, if it drops to 0, you die, so keep an eye out.");
        lib.clear();
        lib.getInput("Third is your energy, you can only make so many actions in a turn, so if you rely mainly on stuff that doesn't use your turn, keep an eye out for your energy.");
        lib.clear();
        lib.getInput("Fourth off are attacks, they are all pretty explanatory in what they do, but try experimenting with them to see exactly what their quirks are, the true power of some may be hidden until you use them yourself. Another thing to note is you start with 2 attacks every battle, and draw another one each turn, your turn will automatically end if you have no attacks remaining.");
        lib.clear();
        lib.getInput("Next up are items, items are a valuable tool, due to having limited uses they are far stronger than attacks, so use them wisely.");
        lib.clear();
        lib.getInput("Lastly are relics, relics provide permanent boosts, so be sure to stock up on a lot of them.");
        lib.clear();
        lib.getInput("Also, if you are ever met with an option to choose, look at the option you'd like to choose, enter the number infront of the option, and press enter. And if nothing seems to be happening, press enter aswell, it may just be waiting for you to continue it.");
        lib.clear();
        lib.getInput("We will be monitoring your progress, and will be allowing you access to superior gear as you achieve certain goals, we wouldn't want to be handing our strongest stuff to our weakest soldier after all. Your first goal will be to kill a boss, a stronger variant of an enemy, but try gaining all of them to be able to obtain all we have to offer.");
        lib.clear();
        lib.getInput("That is all for now soldier, we at LunarCorp wish you good luck on your ventures. Farwell.");
    }
    
    public static void optionsMenu(){
        int c = -1;
        while (c!=2){
            c = lib.menu(new String[] {"Reset data", "Back"}, "\n=> ");
            switch (c){
                case 1:
                    FileLib.removeFile(saveFile);
                    Achievements.reset();
                    saveAchievements();
                    lib.getInput("Cleared data");
                    break;
            }
        }
    }
    
    public static void startAdventure(){
        while (true){
            String seed = "";
            try {
                lib.clear();
                seed = lib.getInput("Seed (enter nothing for a random seed): ");
                lib.setSeed(Integer.parseInt(seed));
                break;
            } catch (Exception e){
                if (seed.equals("")){
                    lib.setSeed(lib.randint(2147483647));
                    break;
                }
            }
        }
        
        player = new Player("You", 100, 5);
        
        player.addAttack(AttackList.attacks[0]);
        player.addAttack(AttackList.attacks[0]);
        player.addAttack(AttackList.attacks[1]);
        player.addAttack(AttackList.attacks[2]);
        player.addAttack(AttackList.attacks[3]);
        player.addAttack(AttackList.attacks[4]);
        player.addAttack(AttackList.attacks[5]);
        
        map = new Map(10, player);
        
        adventure();
    }
    
    public static void loadAchievements(){
        String achievementString = FileLib.readLine(achievementFile, 1);
        for (int i=0; i<achievementString.length(); i++){
            if (achievementString.charAt(i) == '1'){
                Achievements.silentAchievement(i);
            }
        }
    }
    
    public static void saveAchievements(){
        String data = "";
        try {
            for (boolean achievement : Achievements.getAchievements()){
                data += achievement ? 1 : 0;
            }
        } catch (Exception e){ // If player hasn't been created
            for (int i=0; i<Achievements.achievementCount(); i++){
                data += '0';
            }
        }
        FileLib.writeFile("achievements.txt", data);
    }
    
    public static void loadAdventure(){
        ArrayList<String> data = FileLib.readFile(saveFile);
        ArrayList<Attack> attacks = new ArrayList<Attack>();
        ArrayList<Item> items = new ArrayList<Item>();
        ArrayList<Relic> relics = new ArrayList<Relic>();
        ArrayList<Integer> mapSpots = new ArrayList<Integer>();
        lib.setSeed(Integer.parseInt(data.get(3)));
        
        int curState = 0;
        for (String curline : data){
            if (curline.charAt(0) == 'M'){
                curState = 1;
            } else if (curline.charAt(0) == 'A'){
                curState = 2;
            } else if (curline.charAt(0) == 'I'){
                curState = 3;
            } else if (curline.charAt(0) == 'R'){
                curState = 4;
            } else if (curline.charAt(0) == 'N'){
                curState = 5;
            } else {
                switch (curState) {
                    case 1:
                        mapSpots.add(Integer.parseInt(curline));
                        break;
                    case 2:
                        attacks.add(AttackList.attacks[Integer.parseInt(curline)]);
                        break;
                    case 3:
                        items.add(ItemList.items[Integer.parseInt(curline.substring(0, curline.indexOf("-")))]);
                        items.get(items.size()-1).setUses(Integer.parseInt(curline.substring(curline.indexOf("-")+1, curline.length())));
                        break;
                    case 4:
                        relics.add(RelicList.relics[Integer.parseInt(curline)]);
                        break;
                    case 5:
                        int min = Integer.parseInt(curline.substring(0, curline.indexOf('/')));
                        int max = Integer.parseInt(curline.substring(curline.indexOf('/')+1, curline.length()));
                        lib.randint(min, max);
                }
            }
        }
        
        player = new Player("You", Integer.parseInt(data.get(0)), Integer.parseInt(data.get(1)), attacks, items, relics, Integer.parseInt(data.get(2)), Integer.parseInt(data.get(6)));
        map = new Map(mapSpots, player);
        map.setDepth(Integer.parseInt(data.get(4)));
        map.setCur(Integer.parseInt(data.get(5)));
        adventure();
    }
    
    public static void saveAdventure(){
        saveAchievements();
        
        String data = player.getMaxHealth() + "\n" + player.getCurHealth() + "\n" + player.getCoins() + "\n" + lib.getSeed() + "\n" + map.getDepth() + "\n" + map.getCur() + "\n" + player.getMaxEnergy() + "\n";
        data += "M\n";
        for (int spot : map.getMap()){
            data += spot + "\n";
        }
        data += "A\n";
        for (Attack attack : player.getAttacks()){
            for (int i=0; i<AttackList.attacks.length; i++){
                if (attack.equals(AttackList.attacks[i])){
                    data += i + "\n";
                    break;
                }
            }
        }
        data += "I\n";
        for (Item item : player.getItems()){
            for (int i=0; i<ItemList.items.length; i++){
                if (item.equals(ItemList.items[i])){
                    data += i + "-" + item.getUses() + "\n";
                    break;
                }
            }
        }
        data += "R\n";
        for (Relic relic : player.getRelics()){
            data += relic.getId() + "\n";
        }
        data += "N\n";
        for (String string : lib.getActions()){
            data += string + "\n";
        }
        FileLib.writeFile(saveFile, data);
    }
    
    public static void adventure(){
        boolean quit = false;
        
        while (!quit){
            lib.clear();
            System.out.println("Seed: " + lib.getSeed());
            map.displayMap(9, 3);
            System.out.println("----------------");
            for (int i=0; i<options.length; i++){
                System.out.println((i+1) + ". " + options[i]);
            }
            try {
                char c = lib.getInput("\n=> ").charAt(0);
                if (c == '1'){
                    switch (map.moveNext()){
                        case 1:
                            startFight(false);
                            break;
                        case 2:
                            startFight(true);
                            break;
                        case 3:
                            Shop shop = map.shops[lib.randint(map.shops.length)];
                            shop.open();
                            break;
                        case 4:
                            beacon();
                            break;
                        case 5:
                            mysteryEvent();
                            break;
                    }
                    
                    if (player.isDead()){
                        FileLib.removeFile(saveFile);
                        return;
                    }
                } else if (c == '2'){
                    saveAdventure();
                    quit = true;
                }
            } catch (Exception e){}
        }
    }
    
    public static void startFight(boolean boss){
        Enemy.Type[] typeList = new Enemy.Type[] {Enemy.Type.ATTACK, Enemy.Type.TANK};
        Enemy enemy = new Enemy(typeList[lib.randint(typeList.length)], map.getDepth(), boss);
            
        if (new BattleManager(player, enemy).startBattle()){
            player.clearEffects();
            int reward = (int)((30*Math.pow(1.5, map.getDepth())) + player.countRelic(2)*5);
            reward *= 1+(player.countRelic(3)*0.1);
            if (boss){
                reward = (int)(reward*2.5);
                Achievements.giveAchievement(4);
            }
            if (reward >= 10000){
                Achievements.giveAchievement(8);
            }
            player.addCoins(reward);
            lib.clear();
            lib.getInput("You beat " + enemy.getName());
            lib.getInput("You received " + reward + " lunas");
            lib.getInput("You now have " + player.getCoins() + " lunas");
            
            chooseReward();
        }
    }
    
    public static void chooseReward(){
        ArrayList options = new ArrayList();
        int rewardCount = 3;
        for (int i=0; i<rewardCount; i++){
            switch (lib.randint(3)){
                case 0:
                    try {
                        Shop.Rarity rarity = Shop.generateRarity();
                        ArrayList<Attack> tempAttacks = new ArrayList<Attack>();
                        for (Attack attack : AttackList.attacks){
                    if (attack.getRarity() == rarity && Achievements.hasAchievement(attack.getRequirement())){
                                tempAttacks.add(attack);
                            }
                        }
                        options.add(tempAttacks.get(lib.randint(tempAttacks.size())));
                    } catch (Exception e) {
                        i--;
                    }
                    break;
                case 1:
                    try {
                        Shop.Rarity rarity = Shop.generateRarity();
                        ArrayList<Item> tempItems = new ArrayList<Item>();
                        for (Item item : ItemList.items){
                            if (item.getRarity() == rarity && Achievements.hasAchievement(item.getRequirement())){
                                tempItems.add(item);
                            }
                        }
                        options.add(tempItems.get(lib.randint(tempItems.size())));
                    } catch (Exception e) {
                        i--;
                    }
                    break;
                case 2:
                    try {
                        Shop.Rarity rarity = Shop.generateRarity();
                        ArrayList<Relic> tempRelics = new ArrayList<Relic>();
                        for (Relic relic : RelicList.relics){
                            if (relic.getRarity() == rarity && Achievements.hasAchievement(relic.getRequirement())){
                                if (rarity == Shop.Rarity.UNIQUE){
                                    // Avoid duplicate uniques
                                    if (!player.hasRelic(relic.getId())){ // Check if player has relic already
                                        boolean alreadyIn = false;
                                        for (int j=0; j<options.size(); j++){ // Check if relic is not an option already
                                            if (options.get(j) instanceof Relic){
                                                if (relic.getId() == ((Relic)options.get(j)).getId()){
                                                    alreadyIn = true;
                                                    break;
                                                }
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
                        options.add(tempRelics.get(lib.randint(tempRelics.size())));
                    } catch (Exception e) {
                        i--;
                    }
                    break;
            }
        }
        
        while (true){
            try {
                lib.clear();
                Shop.renderName("Reward");
                for (int i=0; i<options.size(); i++){
                    System.out.print((i+1) + ". ");
                    if (options.get(i) instanceof Attack){
                        System.out.print(Shop.getRenCol(((Attack)options.get(i)).getRarity()));
                    } else if (options.get(i) instanceof Item){
                        System.out.print(Shop.getRenCol(((Item)options.get(i)).getRarity()));
                    } else if (options.get(i) instanceof Relic){
                        System.out.print(Shop.getRenCol(((Relic)options.get(i)).getRarity()));
                    }
                    System.out.println(options.get(i) + lib.reset);
                }
                int c = Integer.parseInt(lib.getInput("\n=> "))-1;
                if (options.get(c) instanceof Attack){
                    player.addAttack((Attack)options.get(c));
                } else if (options.get(c) instanceof Item){
                    player.addItem((Item)options.get(c));
                } else {
                    player.addRelic((Relic)options.get(c));
                }
                return;
            } catch (Exception e){}
        }
    }
    
    public static void beacon(){
        int c = -1;
        while (true){
            try {
                lib.clear();
                Shop.renderName("Beacon");
                System.out.println("1. Heal\n2. Reward");
                c = Integer.parseInt(lib.getInput("\n=> "));
                if (c == 1 || c == 2){
                    break;
                }
            } catch (Exception e) {}
        }
        
        switch (c){
            case 1:
                lib.clear();
                int heal = (int)Math.ceil(player.getMaxHealth() * (lib.randint(10, 20) * 0.01)); // Heal 10-20% of max hp
                Shop.renderName("Heal");
                lib.getInput("The beacon healed you for " + heal + " health");
                break;
            case 2:
                chooseReward();
                break;
        }
    }
    
    public static void mysteryEvent(){
        boolean eventHappened = false;
        ArrayList<Integer> events = new ArrayList<Integer>();
        
        events.add(0);
        
        if (!player.hasRelic(14)){
            events.add(1);
        }
        
        events.add(2);
        events.add(3);
        events.add(4);
        
        while (!eventHappened){
            int event = events.get(lib.randint(events.size()));
            switch (event){
                case 0:
                    lib.clear();
                    Shop.renderName("Mystery");
                    lib.getInput("There is nothing");
                    eventHappened = true;
                    break;
                case 1:
                    while (true){
                        try {
                            lib.clear();
                            Shop.renderName("Mystery");
                            System.out.println("A strange figure stands before you, offering you riches, will you accept?\n1. Accept\n2. Reject\n3. Fight");
                            int c = Integer.parseInt(lib.getInput("\n=> "));
                            lib.clear();
                            Shop.renderName("Mystery");
                            if (c == 1){
                                player.addCoins(1000);
                                player.addRelic(RelicList.relics[14]);
                                lib.getInput("The figured gave you riches, but at what cost?");
                            break;
                        } else if (c == 2) {
                            lib.getInput("You decide not to accept the figure's offer");
                            break;
                        } else if (c == 3){
                            lib.getInput("You decide to engage in combat with the figure.");
                            startFight(true);
                            break;
                        }
                    } catch (Exception e) {}
                }
                eventHappened = true;
                break;
            case 2:
                while (true){
                    try {
                        lib.clear();
                        Shop.renderName("Mystery");
                        System.out.println("A strange figure stands before you, offering you riches, will you accept?\n1. Accept\n2. Reject\n3. Fight");
                        int c = Integer.parseInt(lib.getInput("\n=> "));
                        lib.clear();
                        Shop.renderName("Mystery");
                        if (c == 1){
                            player.addCoins(1000);
                            lib.getInput("The figured gave you riches.");
                            break;
                        } else if (c == 2) {
                            lib.getInput("You decide not to accept the figure's offer");
                            break;
                        } else if (c == 3){
                            lib.getInput("You decide to engage in combat with the figure.");
                            startFight(true);
                            break;
                        }
                    } catch (Exception e) {}
                }
                eventHappened = true;
                break;
            case 3:
                while (true){
                    try {
                        lib.clear();
                        Shop.renderName("Mystery");
                        System.out.println("A strange figure stands before you, offering you riches, will you accept?\n1. Accept\n2. Reject\n3. Fight");
                        int c = Integer.parseInt(lib.getInput("\n=> "));
                        lib.clear();
                        Shop.renderName("Mystery");
                        if (c == 1){
                            player.addCoins(1);
                            lib.getInput("The figured gave you ric- Wait really? One coin? That's it?");
                            break;
                        } else if (c == 2) {
                            lib.getInput("You decide not to accept the figure's offer");
                            break;
                        } else if (c == 3){
                            lib.getInput("You decide to engage in combat with the figure.");
                            startFight(true);
                            break;
                        }
                    } catch (Exception e) {}
                }
                eventHappened = true;
                break;
            case 4:
                while (true){
                    try {
                        lib.clear();
                        Shop.renderName("Mystery");
                        System.out.println("A strange figure stands before you, offering you riches, will you accept?\n1. Accept\n2. Reject\n3. Fight");
                        int c = Integer.parseInt(lib.getInput("\n=> "));
                        lib.clear();
                        Shop.renderName("Mystery");
                        if (c == 1){
                            player.removeCoins(player.getCoins());
                            lib.getInput("The figure preys on the weakness you display as you figure they'll provide you with riches, stealing all your coins instead.'");
                            break;
                        } else if (c == 2) {
                            lib.getInput("You decide not to accept the figure's offer");
                            break;
                        } else if (c == 3){
                            lib.getInput("You decide to engage in combat with the figure.");
                            startFight(true);
                            break;
                        }
                    } catch (Exception e) {}
                }
                eventHappened = true;
                break;
            case 5:
                while (true){
                    try {
                        lib.clear();
                        Shop.renderName("Mystery");
                        System.out.println("You come across a park, would you like to stay?\n1. Stay\n2. Leave");
                        int c = Integer.parseInt(lib.getInput("\n=> "));
                        lib.clear();
                        Shop.renderName("Mystery");
                        if (c == 1){
                            boolean bad = lib.randint(5) == 0;
                            if (bad){
                                player.addRelic(RelicList.relics[16]);
                                lib.getInput("You decide to stay for a bit, you suddenly feel very tired.");
                            } else {
                                player.addRelic(RelicList.relics[15]);
                                lib.getInput("You decide to stay for a bit, you feel energized.'");
                            }
                            break;
                        } else if (c == 2) {
                            lib.getInput("You decide not to stay at the park");
                            break;
                        }
                    } catch (Exception e) {}
                }
                eventHappened = true;
                break;
            }
        }
    }
}
