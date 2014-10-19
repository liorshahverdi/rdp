public class ArrayStack<T> implements IStack<T>
{
 private T stackBody[];
 private int top;
 
 public ArrayStack(int size)
 {
  //Java does not permit creation of generic arrays!! So, we can�t do this.
  //stackBody = new T [size];
  //Instead we create an object array and cast it to T. You get a warning
  //Ignore it.
  stackBody = (T[])new Object [size];
  top = -1;
 }
 
 public boolean isEmpty()
 {
  return (top == -1);
 }
 
 public void push(T item)
 {
  if (top < stackBody.length-1)
  {
   top++;
   stackBody[top] = item;
  }
  else
  {
   System.out.println("Error in ArrayStack.push() � Stack Full ");
  }
 }
 
 public T pop()
 {
  if (isEmpty())
  {
   System.out.println("Error in ArrayStack.pop() � Stack Empty ");
   return null;
  }
  else
  {
   T topElement = stackBody[top];
   top--;
   return topElement;
  }
 }
 
 public T top()
 {
  if (isEmpty())
  {
   System.out.println("Error in ArrayStack.top() � Stack Empty ");
   return null;
  }
  else
  {
   T topElement = stackBody[top];
   return topElement;
  }
 }

}