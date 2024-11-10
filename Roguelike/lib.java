import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class lib {
    private static Scanner input = new Scanner(System.in);
    private static long seed = System.nanoTime();
    private static Random rng = new Random(seed);
    private static String rngFile = "rng.ser";
    private static ArrayList<String> actions = new ArrayList<String>();
    
    public static String reset = "\u001B[0m";
    public static String bold =  "\u001B[1m";
    
    public static String fore(int r, int g, int b){
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }
    
    public static String back(int r, int g, int b){
        return "\u001B[48;2;" + r + ";" + g + ";" + b + "m";
    }
    
    public static void clear(){
        System.out.print("\033[H\033[2J");
    }
    
    public static String getInput(String message){
        System.out.print(message);
        return input.nextLine();
    }
    
    public static String getInput(){
        return input.nextLine();
    }
    
    public static int menu(String[] options, String message){
        while (true){
            clear();
            for (int i=0; i<options.length; i++){
                System.out.println((i+1) + ". " + options[i]);
            }
            try {
                int c = Integer.parseInt(getInput(message));
                if (c > 0 && c <= options.length){
                    return c;
                }
            } catch (Exception e){}
        }
    }
    
    public static int clamp(int num, int min, int max){
        if (num < min){
            return min;
        } else if (num > max){
            return max;
        }
        return num;
    }
    
    public static int randint(int min, int max){
        if (max <= min){
            return -1;
        }
        actions.add(min + "/" + max);
        return rng.nextInt(max-min)+min;
    }
    
    public static int randint(int max){
        return randint(0, max);
    }
    
    public static void setSeed(long newSeed){
        seed = newSeed;
        rng.setSeed(seed);
        actions = new ArrayList<String>();
    }
    
    public static long getSeed(){
        return seed;
    }
    
    public static ArrayList<String> getActions(){
        return actions;
    }
}
