package re.study.functionalprogramming.functor.model;

public class Address {
    private String street;

    public Address() {
    }

    public Address(String addr) {
        this.street = addr;
    }

    @Override
    public String toString() {
        return "Address [street=" + street + "]";
    }

    public String street() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
