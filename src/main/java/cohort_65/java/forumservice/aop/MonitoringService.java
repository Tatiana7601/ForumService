package cohort_65.java.forumservice.aop;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MonitoringService {

    private final RedisTemplate<String, Object> redisTemplate;

    public MonitoringService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void recordMetric(String methodName, long executionTime, boolean success) {
        String key = "metrics:" + methodName;
        Map<String, Object> metric = (Map<String, Object>) redisTemplate.opsForValue().get(key);
        if (metric == null) {
            metric = new HashMap<>();
            metric.put("count", 0L);
            metric.put("errors", 0L);
            metric.put("totalTime", 0L);
        }

        metric.put("count", (Long) metric.get("count") + 1);
        metric.put("totalTime", (Long) metric.get("totalTime") + executionTime);
        if (!success) {
            metric.put("errors", (Long) metric.get("errors") + 1);
        }

        redisTemplate.opsForValue().set(key, metric);
    }

    public void recordError(String methodName) {
        String key = "metrics:" + methodName;
        Map<String, Object> metric = (Map<String, Object>) redisTemplate.opsForValue().get(key);
        if (metric == null) {
            metric = new HashMap<>();
            metric.put("count", 0L);
            metric.put("errors", 0L);
            metric.put("totalTime", 0L);
        }


        metric.put("errors", (Long) metric.get("errors") + 1);

        redisTemplate.opsForValue().set(key, metric);

    }
}