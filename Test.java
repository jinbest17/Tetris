class Test{
  public Test() {
    System.out.println("SUPER no arg");
  }

  public Test(int a) {
    System.out.println("SUPER INT" + a);
  }
    public static void main(String[] args) {
     new Test();
    new Test(5);
    new Children();
    new Children("AS");
    new Children(6);
    }
}

class Children extends Test {
  public Children() {
    System.out.println("CHILD NO ARG");
  }

  public Children(String s) {
    this();
    System.out.println("Child string" + s);
  }

  public Children(int a) {
    super(a);
    System.out.println("Child int" +a);
  }

}



