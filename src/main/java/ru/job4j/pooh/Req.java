package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    private static final String GET = "GET";

    private static final String POST = "POST";

    private static final String TOPIC = "topic";

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        var stringArray = content.split(" ");
        var httpRequestType = "";
        var poohMode = "";
        var sourceName = "";
        var param = "";
        if (stringArray.length < 2 || (!GET.equals(stringArray[0]) && !POST.equals(stringArray[0]))) {
            throw new IllegalArgumentException("Некорректная строка запроса");
        }
        httpRequestType = stringArray[0];
        var modeArray = stringArray[1].split("/");
        if (modeArray.length < 3) {
            throw new IllegalArgumentException("Некорректная строка запроса");
        }
        poohMode = modeArray[1];
        sourceName = modeArray[2];
        if (GET.equals(httpRequestType)) {
            param = TOPIC.equals(poohMode) ? modeArray[3] : "";
        } else {
            stringArray = content.split(System.lineSeparator());
            param = stringArray[stringArray.length - 1];
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}

