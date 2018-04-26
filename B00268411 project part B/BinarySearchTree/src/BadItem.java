
public class BadItem extends Item {
    
    public BadItem(int value) {
        super(value);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() % 10;
    }
    
}