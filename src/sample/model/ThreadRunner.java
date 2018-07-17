package sample.model;

import javafx.application.Platform;

abstract public class ThreadRunner implements Runnable{
    ThreadRunnerFinishedListener listener;

    abstract public static class ThreadRunnerFinishedListener{
        public  abstract void onStop();
        public void notifyListener(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    onStop();
                }
            });
        }
    }

    public void run(){
        doWork();
        notifyListener();
    }

    abstract public void doWork();

    public void setFinishedLitener(ThreadRunnerFinishedListener listener){
        this.listener = listener;
    }

    private void notifyListener(){
        listener.notifyListener();
    }

}
