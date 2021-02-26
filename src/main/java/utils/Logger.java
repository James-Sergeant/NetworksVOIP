package utils;

public class Logger {
    private static boolean logging = false;

    public static void toggleLogging(){
        logging ^= true;
    }

    public static void log(String string){
        System.out.println(string);
    }
}
