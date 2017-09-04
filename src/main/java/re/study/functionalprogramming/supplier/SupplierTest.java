package re.study.functionalprogramming.supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class SupplierTest {

    Supplier<String> hello = () -> "hello";

    @Test
    public void ut1001_hello() throws Exception {
        System.out.println(hello.get());
    }

    Function<String, Supplier<String>> getVal = x -> () -> "[" + x + "]";

    @Test
    public void ut1002_list() throws Exception {
        List<String> names = new ArrayList<>();
        names.add("David");
        names.add("Sam");
        names.add("Ben");

        names.stream().forEach((x) -> {
            String value = getVal.apply(x).get();
            System.out.println(value);
        });
    }

    @Builder
    @Data
    @AllArgsConstructor
    static class Item {
        private String id;
        private String msg;

        public Item() {
        }

        public Item(String item) {
            super();
            this.id = item;
        }

        public String getMsg() {
            return "Hello " + (this.msg + "");
        }

        public static String getStaticVal() {
            return "Static Val";
        }
    }

    Supplier<Item> supplier = Item::new;

    @Test
    public void ut1003_supplier() throws Exception {
        Item item = supplier.get();
        System.out.println(item.getMsg());
    }

    @Test
    public void ut1004_supplierGetStaticVal() throws Exception {
        Supplier<String> supplier = Item::getStaticVal;
        String val = supplier.get();
        System.out.println("Calling  Method: " + val);
    }

    @Test
    public void ut1005_supplierWithStream() throws Exception {
        List<Item> list = new ArrayList<>();
        list.add(new Item("AA"));
        list.add(new Item("BB"));
        list.add(new Item("CC"));
        Stream<String> names = list.stream().map(Item::getId);
        names.forEach(n -> System.out.println(n));
    }

}
