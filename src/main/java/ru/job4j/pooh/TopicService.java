package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TopicService implements Service {
    private final Map<String, Map<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        var httpRequestType = req.httpRequestType();
        var sourceName = req.getSourceName();
        var param = req.getParam();
        var text = "";
        var status = "501";
        if ("POST".equals(httpRequestType)) {
            var topic = topics.get(sourceName);
            if (topic != null) {
                for (ConcurrentLinkedQueue<String> queue : topic.values()) {
                    queue.add(param);
                }
            }
            text = param;
            status = "200";
        } else if ("GET".equals(httpRequestType)) {
            topics.putIfAbsent(sourceName, new ConcurrentHashMap<>());
            var topic = topics.get(sourceName);
            topics.get(sourceName).putIfAbsent(param, new ConcurrentLinkedQueue<>());
            var recipient = topic.get(param);
            text = recipient.poll();
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
