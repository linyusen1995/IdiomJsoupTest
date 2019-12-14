package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentUtil {
    private static DocumentUtil documentUtil;

    public static DocumentUtil newInstance() {
        if (documentUtil == null) {
            documentUtil = new DocumentUtil();
        }
        return documentUtil;
    }

    public Document getDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36")
                    .timeout(50000).get();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
