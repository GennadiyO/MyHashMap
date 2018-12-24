import java.util.Arrays;

public class MyNewHashMap {

    private int FREE = Integer.MAX_VALUE;
    private int count;
    private int size;
    private int[] keys;
    private long[] values;

    public MyNewHashMap(){
        count = 0;
        this.size = 1<<4;
        keys = new int[this.size];
        values = new long[this.size];
        Arrays.fill(keys, FREE);
    }
    public int size(){
        return count;
    }
    public void put(int x, long y){
        if (0.75*size<= count){
            remapping();
        }
        int hashVal = index(x, size) + (6-x%6);
        for (int i = 0; i<size; i++){
            if (linearProbe(x, y, i, size, keys, values)){
                count++;
                return;
            } else if (quadraticProbe(x, y, i, size, keys, values)){
                count++;
                return;
            } else if(doubleHashing(x, y, hashVal, size, keys, values) != Integer.MAX_VALUE){
                hashVal = doubleHashing(x, y, hashVal, size, keys, values);
            } else {
                count++;
                return;
            }
        }
    }

    public long get(int x){
        int hashVal = index(x, size) + (6-x%6);
        for (int i = 0; i<size; i++){
            int linearProbe = keys [index((hash(x) + i), size)];
            int quadraticProbe = keys [index((hash(x)+(i+1)^2), size)];
            int doubleHashing = keys [index((hashVal +(6-x%6)), size)];
            if (linearProbe == x){
                return values[index((hash(x) + i), size)];
            } else if (quadraticProbe == x){
                return values[index((hash(x)+(i+1)^2), size)];
            } else if (doubleHashing == x){
                return values[index((hashVal +(6-x%6)), size)];
            }
            hashVal = index((hashVal +(6-x%6)), size);
        }
        throw new RuntimeException("No such key!");
    }

    private boolean linearProbe (int x, long y, int i, int size, int[] keys, long[] values){
        int k = index((hash(x) + i), size);
        return fill(k, x, y, size, keys, values);
    }

    private boolean quadraticProbe(int x, long y, int i, int size, int[] keys, long[] values) {
        int k = index((hash(x)+(i+1)^2), size);
        return fill(k, x, y, size, keys, values);
    }

    private int doubleHashing(int x, long y, int hashVal, int size, int[] keys, long[] values) {
        int k = index((hashVal +(6-x%6)), size);
        if (k == size){
            k=0;
        }
        if (keys[k] ==FREE){
            keys[k] = x;
            values[k] = y;
            return Integer.MAX_VALUE;
        } else if (keys[k] == x){
            values[k] = y;
            return Integer.MAX_VALUE;
        }
        return k;
    }

    private void remapping() {
        int sizeNew = (int) (size*1.5);
        int[] keysNew = new int[sizeNew];
        long []valuesNew = new long[sizeNew];
        Arrays.fill(keysNew, FREE);
        for (int i = 0; i<keys.length; i++){
            int key = keys[i];
            long value = values[i];
            int hashVal = index(key, sizeNew) + (6-key%6);
            for (int j = 0; j<sizeNew; j++){
                if (linearProbe(key, value, j, sizeNew, keysNew, valuesNew)){
                    break;
                } else if (quadraticProbe(key, value, j, sizeNew, keysNew, valuesNew)){
                    break;
                } else if(doubleHashing(key, value, hashVal, sizeNew, keysNew, valuesNew) != Integer.MAX_VALUE){
                    hashVal = doubleHashing(key, value, hashVal, sizeNew, keysNew, valuesNew);
                } else {
                    break;
                }
            }
        }
        size = sizeNew;
        keys = keysNew;
        values = valuesNew;
    }

    private boolean fill(int k, int x, long y, int size, int[] keys, long[] values){
        if (k == size){
            k=0;
        }
        if (keys[k] == FREE){
            keys[k] = x;
            values[k] = y;
            return true;
        } else if (keys[k] == x){
            values[k] = y;
            return true;
        }else return false;
    }

    private int hash (int x){
        return (x>>15)^x;
    }

    private int index(int hash, int size){
        return Math.abs(hash)%size;
    }
}
