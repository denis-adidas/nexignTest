import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.*;
import org.openqa.selenium.By;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        System.out.println("Count of word: \"Nord\" on the page: " + nordCount);
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

    @Test
    public void testSpellingOnNexignPage() throws IOException {
        openAndCheckSpellingOnFirstPages("https://nexign.com/ru", 5);
    }

    private void openAndCheckSpellingOnFirstPages(String url, int pageCount) throws IOException {
        openMainPage(url);

        List<String> spellingErrors = new ArrayList<>();

        ElementsCollection links = getFirstPageLinks(pageCount);
        for (int i = 0; i < links.size(); i++) {
            String pageUrl = links.get(i).getAttribute("href");
            String pageText = getPageText(pageUrl);
            spellingErrors.addAll(checkSpelling(pageText));
        }

        System.out.println("Spelling errors on the first " + pageCount + " pages: " + spellingErrors);
    }

    private void openMainPage(String url) {
        Selenide.open(url);
    }

    private ElementsCollection getFirstPageLinks(int count) {
        return $$(By.cssSelector("a")).filter(visible).last(count);
    }

    private String getPageText(String url) {
        Selenide.open(url);
        return Selenide.$("body").getText();
    }

    private List<String> checkSpelling(String text) throws IOException {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en"));
        return langTool.check(text).stream()
                .map(error -> error.getRule().getDescription())
                .toList();
    }


}
