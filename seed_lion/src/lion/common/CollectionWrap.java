package lion.common;

public final class CollectionWrap<T>{
    private T myWrap;

    public CollectionWrap(T myWrap) {
        this.myWrap = myWrap;
    }

    public CollectionWrap() {
    }

    public T getMyWrap() {
        return myWrap;
    }

    public void setMyWrap(T myWrap) {
        this.myWrap = myWrap;
    }

    @Override
    public String toString() {
        return "CollectionWrap{" +
                "myWrap=" + myWrap +
                '}';
    }
}