package utils;

public class Logger {
    private static boolean logging = false;

    public static void toggleLogging(){
        logging ^= true;
    }

    public static <T> void log(T string){
        if(logging) System.out.println(string);
    }
}
