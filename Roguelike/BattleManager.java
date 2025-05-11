import java.util.ArrayList;

public class BattleManager {
    
    private static String[] battleOptions = new String[] {"Attack", "Item", "Status", "Enemy Status"};
    
    private static Player player;
    private static Enemy enemy;
    private static String messages;
    private static boolean curTurn;
    
    private static ArrayList<Integer> achievements; // This is for in battle achievments as to not overlap with the effect messages
    
    public static void InitBattleManager(Player playerNew, Enemy enemyNew){
        player = playerNew;
        enemy = enemyNew;
        messages = "";
        achievements = new ArrayList<Integer>();
    }
    
    public static boolean startBattle(){
        curTurn = true;
        
        for (Fighter fighter : new Fighter[] {player, enemy}){
            fighter.resetAvailable(3 + fighter.countRelic(32));
            if (fighter.hasRelic(5)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, 5*fighter.countRelic(5)));
            }
            if (fighter.hasRelic(6)){
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, null, 5*fighter.countRelic(6)));
            }
            if (fighter.hasRelic(7)){
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, null, 5*fighter.countRelic(7)));
            }
            if (fighter.hasRelic(8)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, 10*fighter.countRelic(8)));
            }
            if (fighter.hasRelic(9)){
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, null, 10*fighter.countRelic(9)));
            }
            if (fighter.hasRelic(10)){
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, null, 10*fighter.countRelic(10)));
            }
            if (fighter.hasRelic(11)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, 20*fighter.countRelic(11)));
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, null, 20*fighter.countRelic(11)));
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, null, 20*fighter.countRelic(11)));
            }
            if (fighter.hasRelic(35)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, null, fighter.getAttackCount()));
            }
        }
        
        while (true){
            clearMessages();
            lib.clear();
            renderBattle();
            if (curTurn){
                if (player.hasEffect(Effect.EffectEnum.STUN)){
                    addMessage("You are stunned");
                    clearMessages();
                    curTurn = !curTurn;
                    player.countdown(enemy);
                    player.restoreEnergy();
                } else {
                    int choice = battleMenu();
                    if (choice == 1){
                        if (player.chooseAttack(enemy)){
                            playerEndTurn();
                        }
                    } else if (choice == 2){
                        if (player.chooseItem(enemy)){
                            playerEndTurn();
                        }
                    } else if (choice == 3){
                        lib.clear();
                        renderBattle();
                        System.out.println("Effects:");
                        player.printEffects();
                        System.out.println("\nRelics:");
                        player.printRelics();
                        lib.getInput();
                    } else if (choice == 4){
                        lib.clear();
                        renderBattle();
                        System.out.println("Enemy effects:");
                        enemy.printEffects();
                        System.out.println("\nEnemy relics:");
                        enemy.printRelics();
                        lib.getInput();
                    }
                }
            } else {
                if (enemy.hasEffect(Effect.EffectEnum.STUN)){
                    clearMessages();
                    addMessage(enemy.getName() + " is stunned");
                    clearMessages();
                    curTurn = !curTurn;
                    enemy.countdown(player);
                } else {
                    clearMessages();
                    if (enemy.chooseRandomAttack(player)){
                        clearMessages();
                        curTurn = !curTurn;
                        enemy.countdown(player);
                        enemy.addAvailable(1 + enemy.countRelic(33));
                        if (player.hasRelic(1) && !(player.getCurHealth() == 0) && !(enemy.getCurHealth() == 0)){
                            new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2).apply(player, enemy);
                        }
                        clearMessages();
                        enemy.restoreEnergy();
                    }
                }
            }
            clearMessages();
            giveAchievements();
            if (player.isDead()){
                return false;
            } else if (enemy.isDead()){
                if (player.getCurHealth() == 1){
                    Achievements.giveAchievement(2);
                }
                return true;
            }
        }
    }
    
    private static void playerEndTurn(){
        clearMessages();
        lib.clear();
        curTurn = !curTurn;
        if (player.hasEffect(Effect.EffectEnum.POISON) && player.getEffect(Effect.EffectEnum.POISON).getStrength() >= 50){
            Achievements.giveAchievement(11);
        }
        player.countdown(enemy);
        player.addAvailable(1 + player.countRelic(33));
        if (!(enemy.getCurHealth() == 0) && !(player.getCurHealth() == 0)){
            if (enemy.hasRelic(1)){
                new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2).apply(enemy, player);
            }
            if (player.hasRelic(19)){
                new Effect(Effect.EffectEnum.TRUEDAMAGE, 1, Math.max(player.getMaxHealth()/10, 1)).apply(enemy, enemy);
            }
        }
        player.resetItemCount();
        player.restoreEnergy();
    }
    
    private static int battleMenu(){
        ArrayList<Integer> options = new ArrayList<Integer>();
        options.add(1);
        if (player.getItems().size() != 0){
            options.add(2);
        }
        options.add(3);
        options.add(4);
        while (true){
            lib.clear();
            renderBattle();
            System.out.println("1. Attack");
            if (options.get(1) == 2){
                System.out.println("2. Items");
            }
            System.out.println((options.size()-1) + ". Status");
            System.out.println(options.size() + ". Enemy status");
            try {
                return options.get(Integer.parseInt(lib.getInput("\n=> "))-1);
            } catch (Exception e){}
        }
    }
    
    public static void addAchievement(int id){
        achievements.add(id);
    }
    
    public static void giveAchievements(){
        while (true){
            try {
                Achievements.giveAchievement(achievements.remove(0));
            } catch (Exception e) {
                return; // Return if index 0 doesn't exist (empty)
            }
        }
    }
    
    public static  void renderBattle(){
        System.out.println(player + "\n\n" + enemy + "\n");
        System.out.print(messages);
    }
    
    public static void addMessage(String message){
        messages += message + "\n";
        lib.clear();
        renderBattle();
        lib.getInput();
    }
    
    public static void clearMessages(){
        messages = "";
    }
}