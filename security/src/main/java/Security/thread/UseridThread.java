package Security.thread;

public class UseridThread {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void set(String userId){
        threadLocal.set(userId);
    }
    public static String get() {
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
}
