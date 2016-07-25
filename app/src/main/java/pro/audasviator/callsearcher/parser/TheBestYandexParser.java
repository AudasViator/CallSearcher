package pro.audasviator.callsearcher.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TheBestYandexParser {
    private static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 4.0.3; ru-ru; HTC Sensation Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
    private static final int TIMEOUT = 2000;

    public ParsedAnswer getAnswer(String query) {
        try {
            Connection.Response response = Jsoup
                    .connect("https://yandex.ru/search/touch/?text=" + query)
                    .timeout(TIMEOUT)
                    .userAgent(USER_AGENT)
                    .execute();

            if (response != null) {
                Document document = response.parse();

                // Может выйти так,что title и content будут из разных ответов
                // Но нам всё равно, и так ничего не гарантируется

                String title = document.getElementsByClass("serp-item__title").get(0).getAllElements().get(0).text();
                String content = document.getElementsByClass("organic__content-wrapper").get(0).getAllElements().get(0).text();

                return new ParsedAnswer(title, content, "", true);
            } else {
                return new ParsedAnswer("", "", "Can`t connect", false);
            }

        } catch (IOException e) {
            return new ParsedAnswer("", "", e.getMessage(), false);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new ParsedAnswer("", "", "No answer!", false);
        }
    }

}
