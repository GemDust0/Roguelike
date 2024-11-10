public class Achievements {
    private static String[] names = {
        "Bosskiller",
        "Overkill",
        "'Tis but a Scratch",
        "Out of Stock",
        "One Trick Pony",
        "Glutton",
        "Explorer",
        "Thief",
        "Stonks",
        "Heirloom Collector",
        "Heirloom Collector+"
    };
    private static String[] descriptions = {
        "I'm the boss now",
        "Damn you really wanted that guy dead.",
        "How are you still alive???",
        "Guess money can buy happiness, for the owner of that shop.",
        "Daring today aren't we?",
        "You're alive yes, but how are you still standing even after all this time.",
        "Your legs must be tired.",
        "You know you can earn cash in another way right?",
        "Good job! You ruined the economy!",
        "Heirlooms are one of a kind not for what they are, but for what they represent.",
        "You seem to have quite the attachment to them."
    };
    
    private static boolean[] achievements = new boolean[names.length];
    
    public static void giveAchievement(int index){
        try{
            if (!achievements[index]){
                lib.clear();
                Shop.renderName("Achievement");
                achievements[index] = true;
                lib.getInput("You got: " + names[index] + " - " + descriptions[index]);
            }
        } catch (Exception e){
            System.out.println(e);
            lib.getInput();
        }
    }
    
    public static void silentAchievement(int index){
        achievements[index] = true;
    }
    
    public static boolean hasAchievement(int index){
        return index != -2 && (index == -1 || achievements[index]);
    }
    
    public static boolean[] getAchievements(){
        return achievements;
    }
    
    public static int achievementCount(){
        return names.length;
    }
    
    public static void printAchievements(){
        System.out.println("Achievements:");
        for (int i=0; i<names.length; i++){
            if (achievements[i]){
                System.out.println(lib.fore(255, 255, 0) + names[i] + " - " + descriptions[i] + lib.reset);
            } else {
                System.out.println(lib.fore(130, 130, 130) + names[i] + " - " + "???" + lib.reset);
            }
        }
        lib.getInput("\nPress enter to return");
    }
    
    public static void reset(){
        achievements = new boolean[names.length];
    }
}
