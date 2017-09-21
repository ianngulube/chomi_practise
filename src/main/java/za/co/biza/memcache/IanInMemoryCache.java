package za.co.biza.memcache;

import java.util.ArrayList;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;

public class IanInMemoryCache<K, T> {

    private long timeToLive;
    private LRUMap ianCacheMap;

    protected class IanCacheObject {

        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected IanCacheObject(T value) {
            this.value = value;
        }
    }

    public IanInMemoryCache(long crunchifyTimeToLive, final long ianTimerInterval, int maxItems) {
        this.timeToLive = crunchifyTimeToLive * 1000;

        ianCacheMap = new LRUMap(maxItems);

        if (timeToLive > 0 && ianTimerInterval > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(ianTimerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    public void put(K key, T value) {
        synchronized (ianCacheMap) {
            ianCacheMap.put(key, new IanCacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (ianCacheMap) {
            IanCacheObject c = (IanCacheObject) ianCacheMap.get(key);

            if (c == null) {
                return null;
            } else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (ianCacheMap) {
            ianCacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (ianCacheMap) {
            return ianCacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (ianCacheMap) {
            MapIterator itr = ianCacheMap.mapIterator();

            deleteKey = new ArrayList<K>((ianCacheMap.size() / 2) + 1);
            K key = null;
            IanCacheObject c = null;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (IanCacheObject) itr.getValue();

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (ianCacheMap) {
                ianCacheMap.remove(key);
            }

            Thread.yield();
        }
    }
}
