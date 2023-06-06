package daemon_thread;

/**
 * @description: 守护线程
 * @author: wczy9
 * @createTime: 2023-06-06  10:46
 */
public class DaemonThread {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (; ; ){

            }
        });
        //将线程的daemon参数设置为true就变为守护线程
        thread.setDaemon(true);
        thread.start();
    }

}