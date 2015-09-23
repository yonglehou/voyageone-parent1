package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.tcb.TaskControlBlock;
import com.voyageone.batch.ims.bean.tcb.TaskStatus;
import com.voyageone.batch.ims.job.UploadProductJob;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leo on 2015/5/28.
 */
public abstract class UploadWorkloadHandler extends Thread{

    protected  List<TaskControlBlock> running_queue;
    //优先级高的运行队列
    protected  List<TaskControlBlock> pri_running_queue;
    protected  List<TaskControlBlock> suspend_queue;

    //tcb运行时，子类必须将该成员赋值
    protected TaskControlBlock current_tcb;
    protected Object queue_lock;

    protected boolean allowRun;
    protected TaskStatus taskStatus;
    private static Log logger = LogFactory.getLog(UploadWorkloadHandler.class);

    private IssueLog issueLog;

    public UploadWorkloadHandler(IssueLog issueLog) {
        queue_lock = new Object();
        pri_running_queue = new ArrayList<>();
        running_queue = new ArrayList<>();
        suspend_queue = new ArrayList<>();
        taskStatus = TaskStatus.INIT;
        allowRun = true;
        this.issueLog = issueLog;
    }

    abstract protected void doJob(TaskControlBlock tcb);

    protected void threadComplete() {
        logger.info("Thread " + getName() + " is finished!");
    }

    @Override
    public void run() {

        while(allowRun)
        {
            if (pri_running_queue.size() != 0 || running_queue.size() != 0) {
                logger.debug(getName() + " pri_running task count: " + pri_running_queue.size());
                logger.debug(getName() + " running task count: " + running_queue.size());
                logger.debug(getName() + " suspend task count: " + suspend_queue.size());
                TaskControlBlock currentTcb = null;

                if (pri_running_queue.size() != 0)
                    currentTcb = pri_running_queue.get(0);
                else if (running_queue.size() != 0)
                    currentTcb = running_queue.get(0);

                if (currentTcb != null) {
                    current_tcb = currentTcb;
                    taskStatus = TaskStatus.RUNNING;

                    // 捕捉线程运行时异常
                    try {
                        doJob(currentTcb);
                    } catch (RuntimeException re) {
                        abortTcb(currentTcb, re);
                    }
                }
            }
            else if (suspend_queue.size() != 0) {
                suspendSelf();
            }
            else
            {
                stopRunning();
                break;
            }
        }
        threadComplete();
    }

    protected void abortTcb(TaskControlBlock currentTcb, RuntimeException re) {
        stopTcb(currentTcb);
    }

    public void stopRunning()
    {
        allowRun = false;
        if (taskStatus == TaskStatus.SUSPEND)
        {
            taskStatus = TaskStatus.RUNNING;
            wakeSelf();
        }
    }

    //tcb运行时，该函数必须被子类调用
    protected void setCurrentTcb(TaskControlBlock tcb)
    {
        current_tcb = tcb;
    }

    public TaskControlBlock getCurrent_tcb() {
        return current_tcb;
    }

    public void suspendCurrentTask()
    {
        suspendTask(current_tcb);
    }

    public void suspendTask(TaskControlBlock tcb) {
        if (tcb.getTcb_queue() != pri_running_queue && tcb.getTcb_queue() != running_queue) {
            logger.error("This condition must be not occurred!");
            return;
        }

        logger.debug("try get lock:" + tcb.getTcb_queue());
        synchronized (tcb.getTcb_queue()) {
            logger.debug("success to get lock:" + tcb.getTcb_queue());
            logger.debug("try get lock:" + tcb.getTcb_queue());
            synchronized (suspend_queue)
            {
                logger.debug("success to get lock:" + suspend_queue);
                Iterator it$ = tcb.getTcb_queue().iterator();
                while(it$.hasNext())
                {
                    if (it$.next() == tcb)
                        it$.remove();
                }

                tcb.setTcb_queue(suspend_queue);
                suspend_queue.add(tcb);
            }
        }
        if (pri_running_queue.isEmpty() && running_queue.isEmpty()) {
            suspendSelf();
        }
    }

    public void stopTcb(TaskControlBlock tcb)
    {
        if (tcb.getTcb_queue() != null) {
            logger.debug("try get lock:" + tcb.getTcb_queue());
            synchronized (tcb.getTcb_queue()) {
                logger.debug("success to get lock:" + tcb.getTcb_queue());
                Iterator<TaskControlBlock> it$ = tcb.getTcb_queue().iterator();
                while (it$.hasNext()) {
                    if (it$.next().equals(tcb)) {
                        tcb.setTcb_queue(null);
                        it$.remove();
                        break;
                    }
                }
            }
        }
    }

    public void stopCurrentTcb()
    {
       stopTcb(current_tcb);
    }

    public void resumeTaskToPriQueue(TaskControlBlock tcb)
    {
        resumeTaskToTaskQueue(tcb, pri_running_queue);

        if (taskStatus == TaskStatus.SUSPEND) {
            wakeSelf();
            taskStatus = TaskStatus.RUNNING;
        }
    }

    public void resumeTaskToTaskQueue(TaskControlBlock tcb, List<TaskControlBlock> tcbQueue)
    {
        if (tcbQueue != running_queue && tcbQueue != pri_running_queue)
        {
            logger.error("This condition must be not occurred!");
            return;
        }

        logger.debug("try get lock:" + suspend_queue);
        synchronized (suspend_queue)
        {
            logger.debug("success to get lock:" + suspend_queue);
            logger.debug("try get lock:" + tcbQueue);
            synchronized (tcbQueue) {
                logger.debug("success to get lock:" + tcbQueue);
                Iterator it$ = suspend_queue.iterator();
                while (it$.hasNext()) {
                    if (it$.next() == tcb)
                        it$.remove();
                }

                tcb.setTcb_queue(tcbQueue);
                tcbQueue.add(tcb);
            }
        }
    }

    public boolean addRunningTask(TaskControlBlock tcb)
    {
        logger.debug("try get lock:" + running_queue);
        synchronized (running_queue) {
            logger.debug("success to get lock:" + running_queue);
            Iterator<TaskControlBlock> it$ = running_queue.iterator();
            while (it$.hasNext())
            {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb))
                {
                    return false;
                }
            }
            tcb.setTcb_queue(running_queue);
            running_queue.add(tcb);

            if (taskStatus == TaskStatus.SUSPEND)
            {
                taskStatus = TaskStatus.RUNNING;
                wakeSelf();
            }
            return true;
        }
    }

    public boolean addPriRunningTask(TaskControlBlock tcb) {
        logger.debug("try get lock:" + pri_running_queue);
        synchronized (pri_running_queue) {
            logger.debug("success to get lock:" + pri_running_queue);
            Iterator<TaskControlBlock> it$ = pri_running_queue.iterator();
            while (it$.hasNext())
            {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb))
                {
                    return false;
                }
            }

            tcb.setTcb_queue(pri_running_queue);
            pri_running_queue.add(tcb);
            return true;
        }
    }

    public boolean addSuspendTask(TaskControlBlock tcb)
    {
        logger.debug("try get lock:" + suspend_queue);
        synchronized (suspend_queue) {
            logger.debug("success to get lock:" + suspend_queue);
            Iterator<TaskControlBlock> it$ = suspend_queue.iterator();
            while (it$.hasNext())
            {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb))
                {
                    return false;
                }
            }
            tcb.setTcb_queue(suspend_queue);
            suspend_queue.add(tcb);
            return true;
        }
    }

    public TaskControlBlock removeRunningTask(TaskControlBlock tcb) {
        logger.debug("try get lock:" + running_queue);
        synchronized (running_queue) {
            logger.debug("success to get lock:" + running_queue);
            Iterator<TaskControlBlock> it$ = running_queue.iterator();
            while (it$.hasNext()) {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb)) {
                    tcb.setTcb_queue(null);
                    it$.remove();
                    return tcb;
                }
            }
            return null;
        }
    }

    public TaskControlBlock removePriRunningTask(TaskControlBlock tcb) {
        logger.debug("try get lock:" + pri_running_queue);
        synchronized (pri_running_queue) {
            logger.debug("success to get lock:" + pri_running_queue);
            Iterator<TaskControlBlock> it$ = pri_running_queue.iterator();
            while (it$.hasNext()) {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb)) {
                    tcb.setTcb_queue(null);
                    it$.remove();
                    return tcb;
                }
            }
            return null;
        }
    }

    public TaskControlBlock removeSuspendTask(TaskControlBlock tcb)
    {
        logger.debug("try get lock:" + suspend_queue);
        synchronized (suspend_queue)
        {
            logger.debug("success to get lock:" + suspend_queue);
            Iterator<TaskControlBlock> it$ = suspend_queue.iterator();
            while (it$.hasNext())
            {
                TaskControlBlock compare_tcb = it$.next();
                if (compare_tcb.equals(tcb))
                {
                    tcb.setTcb_queue(null);
                    it$.remove();
                    return tcb;
                }
            }
        }
        return null;
    }

    public void suspendSelf()
    {
        logger.info(getName() + " do suspend!");
        logger.debug("try to get lock-queue_lock:" + queue_lock);
        synchronized (queue_lock)
        {
            logger.debug("success to get lock queue_lock:" + queue_lock);
            //必须在锁里面再次做这样的检查，防止当前线程由于时间片分配关系，还没有执行到queue_lock.wait，
            //UploadImageHandler线程已经执行结束，并且做了queue_lock.notify的操作，这样当时间片分配到
            //当前线程时，他仍然执行queue_lock.wait的操作，导致不同步
            if (pri_running_queue.isEmpty() && running_queue.isEmpty() && !suspend_queue.isEmpty()) {
                try {
                    taskStatus = TaskStatus.SUSPEND;
                    logger.info(getName() + " is blocked");
                    queue_lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void wakeSelf()
    {
        logger.info(getName() + " do notify!");
        synchronized (queue_lock)
        {
            logger.info(getName() + " is notified!");
            queue_lock.notify();
        }
    }

    protected void logIssue (String message) {
        issueLog.log(UploadProductJob.class.getSimpleName(), message, ErrorType.BatchJob, SubSystem.IMS);
    }
}
