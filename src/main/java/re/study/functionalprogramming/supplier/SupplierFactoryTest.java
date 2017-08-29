package re.study.functionalprogramming.supplier;

import java.util.function.Supplier;

import org.junit.Test;

public class SupplierFactoryTest {

    static interface Pen {
        void write(String toWrite);

        boolean outOfInk();
    }

    static interface PenFactory {
        Pen create();
    }

    static class MyPen implements Pen {
        int time = 0;

        @Override
        public void write(String story) {
            System.out.println(story);
            time += 1;
        }

        @Override
        public boolean outOfInk() {
            if (time >= 3) {
                time = 0;
                return true;
            }
            return false;
        }
    }

    static class DocumentWriter {

        private PenFactory factory;
        private Pen pen;

        public DocumentWriter(PenFactory factory) {
            this.factory = factory;
            this.pen = factory.create();
        }

        public void writeData(String data) {
            if (pen.outOfInk()) {
                System.out.println("factory.create()");
                pen = factory.create();
            }
            pen.write(data);
        }
    }

    @Test
    public void ut1001_withFactory() throws Exception {
        DocumentWriter writer = new DocumentWriter(new PenFactory() {
            @Override
            public Pen create() {
                return new MyPen();
            }
        });
        writer.writeData(" a ");
        writer.writeData(" b ");
        writer.writeData(" c ");
        writer.writeData(" d ");
        writer.writeData(" e ");
        writer.writeData(" f ");
        writer.writeData(" g ");
    }

    static class FairyWriter {
        private Supplier<Pen> supplier;
        private Pen pen;

        public FairyWriter(Supplier<Pen> supplier) {
            this.supplier = supplier;
            this.pen = supplier.get();
        }

        public void writeData(String data) {
            if (pen.outOfInk()) {
                System.out.println("supplier.get()");
                pen = supplier.get();
            }
            pen.write(data);
        }
    }

    @Test
    public void ut1002_withSupplier() throws Exception {
        FairyWriter writer = new FairyWriter(MyPen::new);
        writer.writeData(" a ");
        writer.writeData(" b ");
        writer.writeData(" c ");
        writer.writeData(" d ");
        writer.writeData(" e ");
        writer.writeData(" f ");
        writer.writeData(" g ");
    }

}
