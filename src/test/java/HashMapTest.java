import org.junit.Assert;
import org.junit.Test;

public class HashMapTest {
    @Test
    public void testMapPutSize(){
        MyNewHashMap map = new MyNewHashMap();
        for (int i = 0; i < 20000; i++){
            map.put(i,i);
        }
        Assert.assertEquals(map.size(), 20000);
    }
    @Test
    public void testMapGet(){
        MyNewHashMap map = new MyNewHashMap();
        for (int i = 0; i < 20000; i++){
            map.put(i,i);
        }
        for (int i = 0; i < 20000; i++){
            Assert.assertEquals(map.get(i), i);
        }
    }
}