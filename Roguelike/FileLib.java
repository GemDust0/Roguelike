import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileLib {
    public static boolean writeFile(String fileName, String contents){
        try {
            PrintWriter file = new PrintWriter(fileName);
            file.println(contents);
            file.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean createFile(String fileName){
        try {
            PrintWriter file = new PrintWriter(fileName);
            file.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static ArrayList<String> readFile(String fileName){
        try {
            File f = new File(fileName);
            Scanner file = new Scanner(f);
            ArrayList<String> ret = new ArrayList<String>();
            while(file.hasNext()) {
                ret.add(file.nextLine());
            }
            return ret;
        } catch (IOException e) {
            return null;
        }
    }
    
    
    public static String readLine(String fileName, int lineNumber){
        // lineNumber is 1-indexed, not 0-indexed
        try {
            File f = new File(fileName);
            Scanner file = new Scanner(f);
            for (int i=1; i<lineNumber; i++) {
                file.nextLine();
                if (!file.hasNext()){
                    return null;
                }
            }
            return file.nextLine();
        } catch (IOException e) {
            return null;
        }
    }
    
    public static void removeFile(String fileName){
        new File(fileName).delete();
    }
}
