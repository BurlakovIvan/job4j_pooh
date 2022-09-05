package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class QueueService implements Service {

    private final Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        var httpRequestType = req.httpRequestType();
        var sourceName = req.getSourceName();
        var param = req.getParam();
        var text = "";
        var status = "501";
        if ("POST".equals(httpRequestType)) {
            queue.putIfAbsent(sourceName, new ConcurrentLinkedQueue<>());
            queue.get(sourceName).add(param);
            text = param;
            status = "200";
        } else if ("GET".equals(httpRequestType)) {
            var linkedQueue = queue.getOrDefault(sourceName, new ConcurrentLinkedQueue<>());
            text = linkedQueue.poll();
            if (text == null) {
                text = "";
                status = "204";
            } else {
                status = "200";
            }
        }
        return new Resp(text, status);
    }
}
