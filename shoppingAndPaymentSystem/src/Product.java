class Product {

    private String name;
    private int cost;
    private int totalQuantity;

    public Product(String name, int cost, int totalQuantity) {
        this.name = name;
        this.cost = cost;
        this.totalQuantity = totalQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getCost() {
        return cost;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void reduceQuanity(int Quantity) {
        this.totalQuantity -= Quantity;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Cost: " + cost + ", Quantity: " + totalQuantity;
    }
}
