public class ArrayQueue<T> implements Queue <T>{
    protected T[] a;
    int head, tail;
    int capacity;


    public T dequeue() {
        if (empty()) {
            //new Exception("Queue is empty.");
        }
        T temp = a[head];
        for (int i = 0; i < tail; i++) {
            a[i] = a[i + 1];
        }
        tail--;
        return temp;
    }

    public void enqueue(T item){
        if (full()){
            new Exception();
        }
        a[tail] = item;
        tail++;
    }


    public boolean empty(){
        if(tail == 0){
            return true;
        }
        return false;
    }


    boolean full() {
        if (a.length == tail) {
            return true;
        }
        return false;
    }

}