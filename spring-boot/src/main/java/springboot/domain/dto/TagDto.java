package springboot.domain.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class TagDto {

    @Getter
    public static class TagsResDto {
        private final List<String> tags = new ArrayList<>();
    }
}
