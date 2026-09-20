package com.pacal.share.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定大小线程池工具类
 */
@Slf4j
public final class ThreadPoolUtil {
	// 默认线程数量
	private static final int DEFAULT_THREAD_COUNT = 16;

	// 使用volatile确保线程可见性
	private static volatile ExecutorService executorService;

	// 私有构造方法防止实例化
	private ThreadPoolUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * 获取线程池实例（使用默认线程数）
	 * @return 线程池实例
	 */
	public static ExecutorService getInstance() {
		if (executorService == null) {
			synchronized (ThreadPoolUtil.class) {
				if (executorService == null) {
					executorService = createExecutorService(DEFAULT_THREAD_COUNT);
				}
			}
		}
		return executorService;
	}

	/**
	 * 获取指定线程数的线程池实例
	 * @param threadCount 线程数量
	 * @return 线程池实例
	 */
	public static ExecutorService getInstance(int threadCount) {
		if (executorService == null) {
			synchronized (ThreadPoolUtil.class) {
				if (executorService == null) {
					executorService = createExecutorService(threadCount);
				} else {
					// 如果已经创建了线程池，记录警告信息
					log.info("Warning: Thread pool already initialized with different thread count");
				}
			}
		}
		return executorService;
	}

	/**
	 * 创建线程池服务
	 * @param threadCount 线程数量
	 * @return 线程池服务
	 */
	private static ExecutorService createExecutorService(int threadCount) {
		// 使用自定义线程工厂，便于识别线程来源和处理未捕获异常
		return Executors.newFixedThreadPool(threadCount, new ThreadFactory() {
			private final AtomicInteger threadNumber = new AtomicInteger(1);
			private final ThreadGroup group = Thread.currentThread().getThreadGroup();

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(group, r, "fixed-pool-thread-" + threadNumber.getAndIncrement(), 0);
				if (t.isDaemon()) {
					t.setDaemon(false);
				}
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				// 添加未捕获异常处理器
				t.setUncaughtExceptionHandler((thread, throwable) -> {
					log.error("Uncaught exception in thread: {}", thread.getName());
					log.error("Thread terminated with uncaught exception", throwable.fillInStackTrace());
				});
				return t;
			}
		});
	}

	/**
	 * 提交带返回值的任务
	 * @param callable 可调用任务
	 * @param <T> 返回值类型
	 * @return Future对象
	 */
	public static <T> Future<T> submit(Callable<T> callable) {
		return getInstance().submit(callable);
	}

	/**
	 * 执行无返回值任务
	 * @param runnable 可运行任务
	 */
	public static void execute(Runnable runnable) {
		getInstance().execute(runnable);
	}

	/**
	 * 使用指定线程数执行任务
	 * @param runnable 可运行任务
	 * @param threadCount 线程数量
	 */
	public static void execute(Runnable runnable, int threadCount) {
		getInstance(threadCount).execute(runnable);
	}

	/**
	 * 关闭线程池
	 */
	public static void shutdown() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
		}
	}

	/**
	 * 立即关闭线程池
	 * @return 未执行的任务列表
	 */
	public static java.util.List<Runnable> shutdownNow() {
		if (executorService != null && !executorService.isShutdown()) {
			return executorService.shutdownNow();
		}
		return java.util.Collections.emptyList();
	}
}

