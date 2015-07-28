package com.pactera.hscloud.common4j.commonhandler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*/
 * 通过信息号量机制来控制JobServer执行的线程数,执行消息处理
 */
public abstract class CommonHandler {
	private static Log logger = LogFactory.getLog(CommonHandler.class);
	protected static ExecutorService pool;
	protected static Semaphore semp = null;
	private static String job_name = "Server-1";
	private static int max_thread = 16;
	private static int max_timeout = 120;

	public ExecutorService getPool() {
		return pool;
	}

	public Semaphore getSemp() {
		return semp;
	}

	public String getJob_name() {
		return job_name;
	}

	public int getMax_thread() {
		return max_thread;
	}

	/*
	 * newCachedThreadPool() -缓存型池子，先查看池中有没有以前建立的线程，如果有，就reuse.如果没有，就建一个新的线程加入池中
	 * -缓存型池子通常用于执行一些生存期很短的异步型任务因此在一些面向连接的daemon型SERVER中用得不多。
	 * -能reuse的线程，必须是timeout IDLE内的池中线程，缺省timeout是60s,超过这个IDLE时长，线程实例将被终止及移出池。
	 * 注意，放入CachedThreadPool的线程不必担心其结束，超过TIMEOUT不活动，其会自动被终止。
	 * newFixedThreadPool与cacheThreadPool差不多，也是能reuse就用，但不能随时建新的线程
	 * -其独特之处:任意时间点，最多只能有固定数目的活动线程存在
	 * ，此时如果有新的线程要建立，只能放在另外的队列中等待，直到当前的线程中某个线程终止直接被移出池子
	 * -和cacheThreadPool不同，FixedThreadPool没有IDLE机制
	 * （可能也有，但既然文档没提，肯定非常长，类似依赖上层的TCP或UDP
	 * IDLE机制之类的），所以FixedThreadPool多数针对一些很稳定很固定的正规并发线程，多用于服务器
	 * -从方法的源代码看，cache池和fixed 池调用的是同一个底层池，只不过参数不同:fixed池线程数固定，并且是0秒IDLE（无IDLE）
	 * cache池线程数支持0-Integer.MAX_VALUE(显然完全没考虑主机的资源承受能力），60秒IDLE
	 */
	public CommonHandler(String job, int threads) {
		
		if(logger.isDebugEnabled()){
			logger.debug("enter CommonHandler Constructor"+" job:"+job+" threads:"+threads);
		}
		// 线程数
		max_thread = threads;
		// 初始化线程池
		// pool = Executors.newCachedThreadPool();
		// 获取当前系统的CPU 数目
		//int cpuNums = Runtime.getRuntime().availableProcessors();
		pool = Executors.newFixedThreadPool(max_thread);
		//pool = Executors.newCachedThreadPool();
		// 最多只能max_thread个线程同时访问
		semp = new Semaphore(threads);
		// 进程名
		job_name = job;
		
		if(logger.isDebugEnabled()){
			logger.debug("exit CommonHandler Constructor");
		}
		
	}

	public abstract void Handler(final Object... params);

	
	/*
     * 
     */
	public void Shutdown() {
		pool.shutdown();
		semp.acquireUninterruptibly(max_thread);
		semp.release(max_thread);
		 try {
	    	 pool.awaitTermination(max_timeout, TimeUnit.SECONDS);
            logger.info("beyond the waiting time,thread pool will be end and jobserver will be end....");
            
            // shutdownNow() 将放弃所有正在等待的任务，等当前执行的任务全部完成之后，结束线程池。
            List<Runnable> abandoned = pool.shutdownNow();
            logger.info("abandoned tasks:" + abandoned);
            
        } catch (InterruptedException e) {
        	logger.error(e.getMessage(), e);
        }
	}

	public boolean IsTerminate() {
		return pool.isTerminated();

	}

}
