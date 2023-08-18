import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ir.shelmos_search.ShelmosSearch;
import java.util.List;

class ShelmosSearchTest {

    private ShelmosSearch shelmosSearch;

    @BeforeEach
    void setUp() {
        shelmosSearch = new ShelmosSearch();
    }

    @Test
    void search_searchAnyString_ShouldReturnEmptyList() {
        boolean assertion = shelmosSearch.search("about").isEmpty();
        Assertions.assertTrue(assertion);
    }

    @Test
    void addDoc_addOneDocument_shouldAppearInSearchResult() {
        String title = "Design by MSP";
        String content = "Design is Good!\nIt's all about engineering";
        shelmosSearch.addDoc(title, content);
        boolean assertion = shelmosSearch.search("about").contains(title);
        Assertions.assertTrue(assertion);
    }

    @Test
    void addDoc_addTwoDocument_shouldAppearInSearchResult() {
        String title1 = "Design by MSP";
        String content1 = "Design is Good!\nIt's all about coding.";
        String title2 = "Clean Code";
        String content2 = "Clean code is code that is easy to read, understand, and maintain. It is code that is well-organized, concise, and follows best practices. Clean code is important because it makes it easier for other developers to work with your code, reduces the likelihood of bugs and errors, and makes it easier to add new features and functionality.";
        shelmosSearch.addDoc(title1, content1);
        shelmosSearch.addDoc(title2, content2);
        List<String> search = shelmosSearch.search("code");
        Assertions.assertTrue(search.containsAll(List.of(new String[]{title1, title2})));
    }
}
