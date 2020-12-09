
public interface Queue<T> {
	
	public T dequeue() throws Exception;
	public void enqueue(T item);
	public boolean empty();

}
