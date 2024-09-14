public class ArrayState {
    private final int DEFAULT_LENGTH = 10;
    private final int HIGHLIGHTED_LENGTH = 10;
    
    private int[] array; // main array
    private int[] highlighted; // highlighted indecies
    private int hAmount; // amount of highlighted indecies
    private int areaLeft, areaRight; // left and right idecies of highlighted area
    private boolean isSorted;

    
    public ArrayState() {
        array = new int[DEFAULT_LENGTH];
        highlighted = new int[HIGHLIGHTED_LENGTH];
        hAmount = 0;
        areaLeft = -1;
        areaRight = -1;
        isSorted = false;
    }

    public ArrayState(int l) {
        array = new int[l];
        highlighted = new int[HIGHLIGHTED_LENGTH];
        hAmount = 0;
        areaLeft = -1;
        areaRight = -1;
        isSorted = false;
    }
    
    public ArrayState(ArrayState other) {
        array = new int[other.length()];
        System.arraycopy(other.getArray(), 0, array, 0, array.length);
        hAmount = other.getHighlightedAmount();
        highlighted = new int[HIGHLIGHTED_LENGTH];
        System.arraycopy(other.getHighlighted(), 0, highlighted, 0, hAmount);
        areaLeft = other.getAreaLeftIndex();
        areaRight = other.getAreaRightIndex();
        isSorted = false;
    }

    public ArrayState(int[] a, int[] h, int hAm) {
        array = new int[a.length];
        System.arraycopy(a, 0, array, 0, array.length);
        highlighted = new int[HIGHLIGHTED_LENGTH];
        System.arraycopy(h, 0, highlighted, 0, hAm);
        hAmount = hAm;
        areaLeft = -1;
        areaRight = -1;
        isSorted = false;
    }
    
    public ArrayState(int[] a, int[] h, int hAm, int al, int ar) {
        array = new int[a.length];
        System.arraycopy(a, 0, array, 0, array.length);
        highlighted = new int[HIGHLIGHTED_LENGTH];
        System.arraycopy(h, 0, highlighted, 0, hAm);
        hAmount = hAm;
        areaLeft = al;
        areaRight = ar;
        isSorted = false;
    }
    
    public ArrayState(int[] a, boolean isS) {
        array = new int[a.length];
        System.arraycopy(a, 0, array, 0, array.length);
        highlighted = new int[HIGHLIGHTED_LENGTH];
        hAmount = 0;
        areaLeft = -1;
        areaRight = -1;
        isSorted = isS;
    }
    
    
    public int[] getArray() {
        return array;
    }
    
    
    public int length() {
        return array.length;
    }
    
    
    public int[] getHighlighted() {
        return highlighted;
    }
    
    
    public int getHighlightedAmount() {
        return hAmount;
    }
    
    public int getAreaLeftIndex() {
        return areaLeft;
    }
    
    public int getAreaRightIndex() {
        return areaRight;
    }
    
    
    /**
     * Fills array in order from n. E.g.:
     * n, n + 1, n + 2, ... n + array.length - 1
     * 
     * @param n first array element's value
     */
    public void fillArrayInOrderFrom(int n) {
        for (int i = 0; i < array.length; i++) {
            array[i] = n + i;
        }
    }
    
    
    /**
     * Fills array in with random numbers from l to r
     * 
     * @param l left border of random values
     * @param r right border of random values
     */
    public void fillArrayRandomly(int l, int r) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (int)(Math.random() * (r - l + 1)) + l;
        }
    }

    
    /**
     * Shuffles array
     */
    public void shuffle() {
        int temp, ri;
        
        for (int i = 0; i < array.length - 1; i++) {
            // random index
            ri = (int)(Math.random() * (array.length - i)) + i;
            
            // swap
            temp = array[i];
            array[i] = array[ri];
            array[ri] = temp;
        }
    }
    
    
    /**
     * @return value of the max element in the array
     */
    public int maxElement() {
        int m = array[0];
        
        for (int i = 1; i < array.length; i++) {
            m = Math.max(m, array[i]);
        }
        
        return m;
    }
    
    
    /**
     * @return value of the min element in the array
     */
    public int minElement() {
        int m = array[0];
        
        for (int i = 1; i < array.length; i++) {
            m = Math.min(m, array[i]);
        }
        
        return m;
    }
    
    
    /**
     * @return clone of this ArrayState
     */
    public ArrayState clone() {
        return new ArrayState(this);
    }
}
