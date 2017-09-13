package re.study.functionalprogramming.advanced;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // Function<? super T, ? extends Stream<? extends R>> mapper
    static public <T> List<T> toListMap(final Path path, final Function<String, ? extends Stream<T>> mapper)
            throws IOException {
        // final String[] keys = Files.lines(path).findFirst().get().split(",");
        // @formatter:off
        List<T> values = Files.lines(path)
                // .skip(1)
                .flatMap(mapper)
                .collect(Collectors.toList());
        // @formatter:on 
        return values;
    }

    /**
     * @param keys 사전에정의된 HeaderNames
     * @return List<Arrays> 데이터
     */
    private final static Function<String[], List<String[]>> buildArraysList(final String[] keys) {
        return v -> {
            final List<String[]> list = new ArrayList<>();
            final String account = v[0];
            final String mapping = v[1];
            final String ptype = v[2];
            for (int i = 3; i < v.length; i++) {
                String[] data = new String[5];
                data[0] = account;
                data[1] = mapping;
                data[2] = ptype;
                data[3] = keys[i]; // week
                data[4] = v[i]; // value of week
                // String c = Arrays.toString(data);
                // System.out.printf("\n %d ==> %s ", i, c);
                list.add(data);
            }
            return list;
        };
    };

    static public List<List<String[]>> toListWithFunction(final Path path, final String dilim) throws IOException {
        // @formatter:off
        final String[] keys = Files.lines(path).findFirst().map(s -> s.split(",")).get();
        final List<List<String[]>> values = Files.lines(path)
                                            .skip(1) /* 첫번째 row 는 헤더 이다. */
                                            .map(line -> line.split(dilim))
                                            .map(buildArraysList(keys))
                                            .collect(Collectors.toList());
        // @formatter:on
        return values;
    }

    /**
     * 
     * <pre>
     * - 1 행은 타이틀
     * - 3 번째 인덱스부터 week 
     * - 2행"ACC_GROUP" "Mapping" "Pricing Type" 기준의 그룹핑 데이타가 반복
     * </pre>
     * 
     * <code>
    원천 데이터
    ACC_GROUP  Mapping Pricing Type    201721  201722  201723  201724 ...
    BBY Item574 SRP 0   0   0   0 ...
    BBY Item574 MAP 0   0   0   0 ...
    BBY Item574 IR  0   0   0   0 ...
    BBY Item574 Incr. IR    0   0   0   0 ...
    BBY Item574 TC to Claim 0   0   0   0 ...
    BBY Item574 MAP Compliant   0   0   0   0 ...
    BBY Item574 Bundle  0   0   0   0 ...
    
    매핑기준
    ACC_GROUP=BBY
    Mapping=Item574
    Pricing Type=SRP
    Week=201721
    Value=0
    
    ACC_GROUP=BBY
    Mapping=Item574
    Pricing Type=SRP
    Week=201722
    Value=5999 
    </code>
     * 
     * @param path 파일경로
     * @param dilim CSV 파일 구분자
     * @param fmap 데이터 컨버저닝 사용자 함수
     * @return
     * @throws IOException
     */
    static public <T, R> List<R> toListWithFunction(final Path path, final String dilim,
            Function<? super String[], ? extends R> fmap) throws IOException {
        // @formatter:off
        final List<R> values = Files.lines(path)
                                            .skip(1) /* 첫번째 row 는 헤더 이다. */
                                            .map(line -> line.split(dilim))
                                            .map(fmap)
                                            .collect(Collectors.toList());
        // @formatter:on
        return values;
    }

}
