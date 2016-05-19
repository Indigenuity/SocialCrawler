package utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

public class ThrottledLimiter {
	
	public static final double DEFAULT_PERMITS_PER_SECOND = 1;
	public static final long DEFAULT_THROTTLE_LENGTH = 30;
	public static final TimeUnit DEFAULT_THROTTLE_TIME_UNIT = TimeUnit.SECONDS;
	
	private final double permitsPerSecond;
	private final long throttleLength;
	private final TimeUnit throttleTimeUnit;
	
    private RateLimiter guavaLimiter;
    private Throttle throttle = new Throttle();
    
    public ThrottledLimiter() {
    	this.permitsPerSecond = DEFAULT_PERMITS_PER_SECOND; 
    	this.throttleLength = DEFAULT_THROTTLE_LENGTH;
    	this.throttleTimeUnit = DEFAULT_THROTTLE_TIME_UNIT;
        this.guavaLimiter = RateLimiter.create(this.permitsPerSecond);
    }
    
    public ThrottledLimiter(double permitsPerSecond, long throttleLength, TimeUnit throttleTimeUnit) {
    	this.permitsPerSecond = permitsPerSecond;
    	this.throttleLength = throttleLength;
    	this.throttleTimeUnit = throttleTimeUnit;
        this.guavaLimiter = RateLimiter.create(this.permitsPerSecond);
    }

    public double acquire() {
        try {
            throttle.waitChoke();
        } catch (Exception e) {
            throw new IllegalStateException(e.getClass().getSimpleName() + " exception in CustomLimiter : " + e.getMessage());
        }
        return guavaLimiter.acquire();
    }

    public void rateLimitNotify() {
        throttle.startChoke();
    }

    class Throttle {
        private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        private ScheduledFuture<?> future;

        public void startChoke() {
            future = executor.schedule(new EndThrottle(), 5, TimeUnit.SECONDS);
        }

        public void waitChoke() throws InterruptedException, ExecutionException {
            if(future == null || future.isDone() || future.isCancelled()) 
                return;
            future.get();
        }
    }

    class EndThrottle implements Runnable {
        @Override 
        public void run() {
        }
    }
}