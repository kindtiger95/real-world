package springboot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ArticleServiceTest {
    @Autowired ArticleService articleService;

    @Test
    public void 문자열_변환_테스트() {
//        String test_test_test = this.articleService.makeSlug("test test test");
//        System.out.println(test_test_test);
    }
}