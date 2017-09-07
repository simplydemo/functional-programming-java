package re.study.functionalprogramming.advanced;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 함수형 프로그래밍을 사용 하면 데이터 변환이 자유 롭다.
 */
public class CSVReader {

    static public List<List<String>> toList(final Path path) throws IOException {
        return toList(path, ",");
    }

    static public List<List<String>> toList(final Path path, final String dilim) throws IOException {
        // @formatter:off
        final List<List<String>> values = Files.lines(path)
                                            .skip(1) /* 첫번째 row 는 헤더 이다. */
                                            .map(line -> line.split(dilim))
                                            .map(arr -> Arrays.asList(arr))
                                            .collect(Collectors.toList());
        // @formatter:on
        return values;
    }

    static public List<Map<String, String>> toListMap(final Path path) throws IOException {
        return toListMap(path, ",");
    }

    static public List<Map<String, String>> toListMap(final Path path, final String dilim) throws IOException {
        final String[] keys = Files.lines(path).findFirst().get().split(dilim);
        List<Map<String, String>> values = Files.lines(path).skip(1).map(line -> line.split(dilim)).map(v -> {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < v.length; i++) {
                map.put(keys[i], v[i]);
            }
            return map;
        }).collect(Collectors.toList());
        return values;
    }

}
