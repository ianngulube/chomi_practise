
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapExample {

    public static void main(String[] args) {
        Map vehicles = new HashMap();

        // Add some vehicles.
        vehicles.put("BMW", 5);
        vehicles.put("Mercedes", 3);
        vehicles.put("Audi", 4);
        vehicles.put("Ford", 10);

        System.out.println("Total vehicles: " + vehicles.size());

        // Iterate over all vehicles, using the keySet method.
        for (Object key : vehicles.keySet()) {
            System.out.println((String) key + " - " + vehicles.get(key));
        }
        System.out.println();

        String searchKey = "Audi";
        if (vehicles.containsKey(searchKey)) {
            System.out.println("Found total " + vehicles.get(searchKey) + " "
                    + searchKey + " cars!\n");
        }

        // Clear all values.
        vehicles.clear();

        // Equals to zero.
        System.out.println("After clear operation, size: " + vehicles.size());

        List<Map> menu = new ArrayList();
        Map menuItem = new HashMap();
        menuItem.put("code", "1");
        menuItem.put("command", "1");
        menuItem.put("page", "english.language.headerText");
        menu.add(menuItem);
        menuItem = new HashMap();
        menuItem.put("code", "2");
        menuItem.put("command", "2");
        menuItem.put("page", "english.language.headerText");
        menu.add(menuItem);
        menuItem = new HashMap();
        menuItem.put("code", "3");
        menuItem.put("command", "3");
        menuItem.put("page", "english.language.headerText");
        menu.add(menuItem);

        for (Map mapItem : menu) {
            //System.out.println(mapItem);
            for (Object key : mapItem.keySet()) {
                System.out.println((String) key + " - " + mapItem.get(key));
            }

        }
    }
}
