public class StackTester {

 /**
  * @param args
  */
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  ArrayStack<String> s = new ArrayStack<String>(5);
  s.push("a");  //"a"
  s.push("b");  //"a" "b"
  s.push("c");  //"a" "b" "c"
  String x = s.top();
  System.out.println("Top =" + x);  //"c"
  String y = s.pop(); //"c"
  System.out.println("Top =" + y);  //"c"
     String z = s.pop(); //"b"
     String w = s.top(); //"a"
     System.out.println("x =" + x + " y = " + y + " z = " + z + " w = "+ w);
    
    /*ArrayListStack<String> als = new ArrayListStack<String>();
     als.push("a");  //"a"
  als.push("b");  //"a" "b"
  als.push("c");  //"a" "b" "c"
  String x1 = als.top();
  System.out.println("Top =" + x1);  //"c"
  String y1 = als.pop(); //"c"
  System.out.println("Top =" + y1);  //"c"
     String z1 = als.pop(); //"b"
     String w1 = als.top(); //"a"
     System.out.println("x1 =" + x1 + " y1 = " + y1 + " z1 = " + z1
       + " w1 = "+ w1);*/

 }

}