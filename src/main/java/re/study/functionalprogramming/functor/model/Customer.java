package re.study.functionalprogramming.functor.model;

public class Customer {
    private Address address;

    public Customer() {
    }

    public Customer(String addr) {
        this.address = new Address(addr);
    }

    @Override
    public String toString() {
        return "Customer [address=" + address + "]";
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
