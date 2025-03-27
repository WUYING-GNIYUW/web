package userServer.thread;

public class AuthorityThread {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void set(String authority){
        threadLocal.set(authority);
    }
    public static String get() {
        return threadLocal.get();
    }
    public static void remove(){
        threadLocal.remove();
    }
}
