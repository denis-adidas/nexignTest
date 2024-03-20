import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class NexignStoreTest {

    @BeforeAll
    public static void setUp() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
    }

    @Test
    public void testNexignStoreNavigation() {
        open("https://nexign.com/ru");
        $x("//span[contains(text(),'Store')]").click();
        $("h1").shouldHave(visible);
        $x("//a[contains(text(),'Продукты для разработчиков')]").click();
        $("h1").shouldHave(visible);
    }
    @Test
    public void testNordCountOnNexignPage() {
        Configuration.browser = "chrome";
        open("https://nexign.com/ru");
        String pageContent = $("body").text();
        int nordCount = countWordOccurrences(pageContent, "Nord");
        System.out.println("Количество упоминаний слова \"Nord\" на странице: " + nordCount);
    }

    private int countWordOccurrences(String text, String word) {
        int count = 0;
        String[] words = text.split("\\s+");
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                count++;
            }
        }
        return count;
    }
}
