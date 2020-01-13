package com.jiangdg.threadpool;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/** 自定义线程池
 * author : jiangdg
 * date   : 2020/1/13 16:40
 * desc   : 本例只是线程池的简单实现，即只有核心线程情况且不考虑任务溢出策略
 * version: 1.0
 */
public class SimpleThreadPool {

    // 阻塞队列，放置任务
    private BlockingQueue<Runnable> mTaskQueue;
    // 工作线程
    private List<WorkerThread> mThreadList = new ArrayList();

    public SimpleThreadPool(int poolSize, BlockingQueue<Runnable> taskQueue) {
        this.mTaskQueue = taskQueue;

        for(int i=0; i<poolSize; i++) {
            WorkerThread thread = new WorkerThread();
            thread.start();
            mThreadList.add(thread);
        }
    }

    public void execute(Runnable task) {
        if(mTaskQueue == null)
            throw new NullPointerException("taskQueue can not be null.");
        // 向队列中添加任务
        // 如果队列已满，则阻塞调用execute的线程直到有空间
        try {
            mTaskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected class WorkerThread extends Thread {
        @Override
        public void run() {
            super.run();
            // 当前线程无限循环从队列取任务
            // 如果队列为空，则阻塞当前线程直接有任务
            while (true) {
                if(mTaskQueue == null)
                    throw new NullPointerException("taskQueue can not be null.");
                try {
                    Runnable task = mTaskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
