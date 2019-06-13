package tony;
import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
    CuckooHashMap<String, String> test1 = new CuckooHashMap<String, String>();

    System.out.println("Number of entries in test1: \t" + test1.size());
    System.out.println("Load factor of test1: \t\t" + test1.loadFactor());

    System.out.println("Adding some entries with duplicate keys");
    test1.put("Nicki", "Minaj");
    test1.put("Amy", "Winehouse");
    test1.put("Jay", "Z");
    test1.put("Justin", "Bieber");
    test1.put("Paul", "McCartney");
    test1.put("Freddie", "Mercury");
    test1.put("John", "Legend");
    test1.put("Elvis", "Presley");
    test1.put("50", "Cent");
    test1.put("John", "Harrison");
    test1.put("Elton", "John");
    test1.put("John", "Lennon");
    test1.put("Justin", "Timberlake");

    System.out.println("Number of entries in test1: \t" + test1.size());
    System.out.println("Load factor of test1: \t\t" + test1.loadFactor());    
    
    ArrayList<Entry<String, String>> entries = (ArrayList<Entry<String, String>>) test1.entrySet();
    ArrayList<String> keys = (ArrayList<String>) test1.keySet();
    ArrayList<String> values = (ArrayList<String>) test1.valueSet();

    for (String v: values) {
      System.out.print(v + ", ");
    }
    System.out.println();
    for (String k: keys) {
      System.out.print(k + ", ");
    }
    System.out.println();

    for (Entry<String, String> e: entries) {
      System.out.println(e.getKey() + " => " + test1.get(e.getKey()));
      System.out.println("Removing ... " + test1.remove(e.getKey()));
    }

    System.out.println("Number of entries in test1: \t" + test1.size());
    System.out.println("Load factor of test1: \t\t" + test1.loadFactor());
  }
  
}