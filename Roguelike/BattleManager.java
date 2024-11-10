import java.util.ArrayList;

public class BattleManager {
    
    private static String[] battleOptions = new String[] {"Attack", "Item", "Status", "Enemy Status"};
    
    private Player player;
    private Enemy enemy;
    private String messages;
    
    private ArrayList<Integer> achievements; // This is for in battle achievments as to not overlap with the effect messages
    
    public BattleManager(Player player, Enemy enemy){
        this.player = player;
        this.enemy = enemy;
        this.messages = "";
        this.achievements = new ArrayList<Integer>();
    }
    
    public boolean startBattle(){
        boolean curTurn = true;
        
        for (Fighter fighter : new Fighter[] {player, enemy}){
            fighter.resetAvailable(2);
            if (fighter.hasRelic(5)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, 1, 5*fighter.countRelic(5)));
            }
            if (fighter.hasRelic(6)){
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, 1, 5*fighter.countRelic(6)));
            }
            if (fighter.hasRelic(7)){
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, 1, 5*fighter.countRelic(7)));
            }
            if (fighter.hasRelic(8)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, 1, 10*fighter.countRelic(8)));
            }
            if (fighter.hasRelic(9)){
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, 1, 10*fighter.countRelic(9)));
            }
            if (fighter.hasRelic(10)){
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, 1, 10*fighter.countRelic(10)));
            }
            if (fighter.hasRelic(11)){
                fighter.addEffect(new Effect(Effect.EffectEnum.STRENGTH, 1, 20*fighter.countRelic(11)));
                fighter.addEffect(new Effect(Effect.EffectEnum.ARMOR, 1, 20*fighter.countRelic(11)));
                fighter.addEffect(new Effect(Effect.EffectEnum.REGENERATION, 1, 20*fighter.countRelic(11)));
            }
        }
        
        while (true){
            clearMessages();
            lib.clear();
            renderBattle();
            if (curTurn){
                if (player.hasEffect(Effect.EffectEnum.STUN)){
                    addMessage("You are stunned");
                    lib.clear();
                    renderBattle();
                    lib.getInput();
                    clearMessages();
                    curTurn = !curTurn;
                    player.countdown(enemy, this);
                    player.restoreEnergy();
                } else {
                    int choice = battleMenu();
                    if (choice == 1){
                        if (player.chooseAttack(enemy, this)){
                            clearMessages();
                            lib.clear();
                            curTurn = !curTurn;
                            player.countdown(enemy, this);
                            player.addAvailable(1);
                            if (enemy.hasRelic(1) && !(enemy.getCurHealth() == 0) && !(player.getCurHealth() == 0)){
                                new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2).apply(enemy, player, this);
                                renderBattle();
                                lib.getInput();
                            }
                            player.restoreEnergy();
                        }
                    } else if (choice == 2){
                        if (player.chooseItem(enemy, this)){
                            clearMessages();
                            curTurn = !curTurn;
                            player.countdown(enemy, this);
                            player.addAvailable(1);
                            if (enemy.hasRelic(1) && !(enemy.getCurHealth() == 0) && !(player.getCurHealth() == 0)){
                                new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2).apply(enemy, player, this);
                                lib.clear();
                                renderBattle();
                                lib.getInput();
                            }
                            player.restoreEnergy();
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
                    lib.clear();
                    renderBattle();
                    lib.getInput();
                    clearMessages();
                    curTurn = !curTurn;
                    enemy.countdown(player, this);
                } else {
                    clearMessages();
                    if (enemy.chooseRandomAttack(player, this)){
                        clearMessages();
                        curTurn = !curTurn;
                        enemy.countdown(player, this);
                        enemy.addAvailable(1);
                        if (player.hasRelic(1) && !(player.getCurHealth() == 0) && !(enemy.getCurHealth() == 0)){
                            new Effect(Effect.EffectEnum.PERCENTDAMAGE, 1, 2).apply(player, enemy, this);
                            lib.clear();
                            renderBattle();
                            lib.getInput();
                        }
                        clearMessages();
                        player.resetItemCount();
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
                    Achievements.giveAchievement(1);
                }
                return true;
            }
        }
    }
    
    private int battleMenu(){
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
    
    public void addAchievement(int id){
        achievements.add(id);
    }
    
    public void giveAchievements(){
        while (true){
            try {
                Achievements.giveAchievement(achievements.remove(0));
            } catch (Exception e) {
                return; // Return if index 0 doesn't exist (empty)
            }
        }
    }
    
    public void renderBattle(){
        System.out.println(player + "\n\n" + enemy + "\n");
        System.out.print(messages);
    }
    
    public void addMessage(String message){
        messages += message + "\n";
    }
    
    public void clearMessages(){
        messages = "";
    }
}
