package checkpoint.andela.parser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileStringBuffer
{
	  static final int RESOURCE_LIMIT = 1;
	  
	  // QUEUE TO HOLD THE LIST OF READ STRINGS
	  private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(RESOURCE_LIMIT);
	  
	  boolean isFull() {
		  return queue.size() >= RESOURCE_LIMIT;
	  }
	  
	  boolean isEmpty() {
		  return queue.size() <= 0;
	  }
	  
	  public synchronized void putData(String data) {
		  while ( isFull() ) {
			  try
			  {
				  wait();
			  }
			  catch (InterruptedException e)
			  {
			    // Ignore
			  }
		  }
		  queue.add(data);  
		  notifyAll();       // Notify any waiter
	  }
	  
	  public synchronized String getData() {
		  while ( isEmpty() ) {
			  try {
				  wait();
			  }
			  catch (InterruptedException e) {
				  // Ignore
			  }
		  }
		  String data = queue.poll();

		  if (data.equals(FileParser.END_OF_FILE)) {
			  data = null;
		  }
		  notifyAll(); 
		  return data;
	  }
	  
}
