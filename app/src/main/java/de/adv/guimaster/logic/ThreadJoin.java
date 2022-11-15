package de.adv.guimaster.logic;

import de.adv.guimaster.StartupActivity;

public class ThreadJoin extends Thread{

    public Thread thread;
    public StartupActivity startupActivity;

    public ThreadJoin(Thread t, StartupActivity sa){
        thread = t;
        startupActivity = sa;
    }

    @Override
    public void run(){
        try{
            thread.join();
        } catch (InterruptedException ie){
            ie.printStackTrace();
        }
        startupActivity.finishStartup();
    }
}
