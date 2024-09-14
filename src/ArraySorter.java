import java.util.ArrayList;
import java.util.LinkedList;

public class ArraySorter {
    private int temp;
    private int areaLeft, areaRight;
    private ArrayList<ArrayState> arrayStates;
    
    
    public ArraySorter(ArrayList<ArrayState> as) {
        arrayStates = as;
        areaLeft = -1;
        areaRight = -1;
    }
    
    
    private void push(int[] a) {
        arrayStates.add(new ArrayState(a, new int[0], 0, areaLeft, areaRight));
    }
    
    private void push(int[] a, int i) {
        arrayStates.add(new ArrayState(a, new int[]{i}, 1, areaLeft, areaRight));
    }
    
    private void push(int[] a, int i1, int i2) {
        arrayStates.add(new ArrayState(a, new int[]{i1, i2}, 2, areaLeft, areaRight));
    }
    
    private void push(int[] a, int i1, int i2, int i3) {
        arrayStates.add(new ArrayState(a, new int[]{i1, i2, i3}, 3, areaLeft, areaRight));
    }
    
    private void push(int[] a, int i1, int i2, int i3, int i4) {
        arrayStates.add(new ArrayState(a, new int[]{i1, i2, i3, i4}, 4, areaLeft, areaRight));
    }
    
    private void push(int[] a, boolean isSorted) {
        arrayStates.add(new ArrayState(a, isSorted));
    }
    
    
    private void setArea(int aL, int aR) {
        areaLeft = aL;
        areaRight = aR;
    }
    
    private void clearArea() {
        areaLeft = -1;
        areaRight = -1;
    }
    
    
    public void bubbleSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                //push(a, j, j + 1);
                
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                    
                    //push(a, j, j + 1);
                }
                
                push(a, j, j + 1);
            }
        }
        
        push(a, true); // sorted
    }
    
    
    public void selectionSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int mini;
        
        for (int i = 0; i < a.length - 1; i++) {
            mini = i;
            
            for (int j = i + 1; j < a.length; j++) {
                push(a, i, mini, j);
                
                if (a[mini] > a[j]) {
                    mini = j;
                }
            }
            
            //push(a, i, mini);
            
            if (mini != i) {
                swap(a, i, mini);
                
                //push(a, i, mini);
            }
            
            push(a, i, mini);
        }
        
        push(a, true); // sorted
    }
    
    
    public void insertionSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        insertionSort(a, 0, a.length - 1);
        
        push(a, true); // sorted
    }
    
    public void insertionSort(int[] a, int l, int r) {
        int j, next;
        
        for (int i = l + 1; i <= r; i++) {
            next = a[i];
            j = i;
            
            push(a, i);
            
            while (j > l && a[j - 1] > next) {
                //push(a, j, j - 1);
                
                a[j] = a[j - 1];
                
                push(a, j, j - 1);
                
                j--;
            }
            
            a[j] = next;
            
            push(a, j);
        }
    }
    
    
    public void binaryInsertionSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        binaryInsertionSort(a, 0, a.length - 1);
        
        push(a, true); // sorted
    }
    
    private void binaryInsertionSort(int[] a, int l, int r) {
        int next, nextl, nextr, mid;
        
        for (int i = l + 1; i <= r; i++) {
            next = a[i];
            nextl = l;
            nextr = i - 1;
            
            //push(a, i);
            
            while (nextr >= nextl) {
                setArea(nextl, nextr);
                push(a, i);
                
                mid = (nextl + nextr) / 2;
                
                push(a, i, mid);
                
                if (a[mid] > next) {
                    nextr = mid - 1;
                }
                else {
                    nextl = mid + 1;
                }
            }
            
            // nextl is new index for next
            clearArea();
            push(a, i, nextl);
            
            System.arraycopy(a, nextl, a, nextl + 1, i - nextl);
            a[nextl] = next;
            
            push(a, nextl);
        }
    }
    
    
    public void mergeSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        msort(a, 0, a.length - 1);
        
        push(a, true); // sorted
    }
    
    public void msort(int[] a, int l, int r) {
        setArea(l, r);
        push(a);
        clearArea();
        
        if (l == r) {
            return;
        }
        
        int mid = (l + r) / 2;
        msort(a, l, mid);
        msort(a, mid + 1, r);
        merge(a, l, mid, mid + 1, r);
    }
    
    /*public void mmerge(int[] a, int[] ha, int l1, int r1, int l2, int r2) {
        int i1 = l1, i2 = l2;
        int j = l1;
        
        while (i1 <= r1 && i2 <= r2) {
            if (a[i1] < a[i2]) {
                ha[j] = a[i1];
                i1++;
            }
            else {
                ha[j] = a[i2];
                i2++;
            }
            j++;
        }
        
        // add elements that are left
        while (i1 <= r1) { // or this
            ha[j] = a[i1];
            i1++;
            j++;
        }
        while (i2 <= r2) { // or this
            ha[j] = a[i2];
            i2++;
            j++;
        }
        
        // copy all to main array
        while (l1 <= r2) { 
            a[l1] = ha[l1];
            l1++;
        }
    }*/
    
    
    public void quickSortLL() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        qsortll(a, 0, a.length - 1);
        
        push(a, true); // sorted
    }
    
    public void qsortll(int[] a, int l, int r) {
        //setArea(l, r);
        //push(a);
        //clearArea();
        
        if (l >= r) {
            return;
        }
        
        int pivot = qpartitionll(a, l, r);
        
        qsortll(a, l, pivot - 1);
        qsortll(a, pivot + 1, r);
    }
    
    public int qpartitionll(int[] a, int l, int r) {
        int n = a[r];
        int il = l;
        
        setArea(l, r);
        //push(a);
        
        for (int ir = l; ir < r; ir++) {
            //push(a, r, il, ir);
            
            if (a[ir] < n) {
                swap(a, il, ir);
                
                push(a, r, il, ir);
                
                il++;
            }
            else {
                push(a, r, il, ir);
            }
        }
        swap(a, il, r);
        
        push(a, il, r);
        clearArea();
        
        return il;
    }
    
    
    public void quickSortLR() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        qsortlr(a, 0, a.length - 1);
        
        push(a, true); // sorted
    }
    
    public void qsortlr(int[] a, int l, int r) {
        //setArea(l, r);
        //push(a);
        //clearArea();
        
        if (l >= r) {
            return;
        }
        
        int pivot = qpartitionlr(a, l, r);
        
        qsortlr(a, l, pivot - 1);
        qsortlr(a, pivot + 1, r);
    }
    
    public int qpartitionlr(int[] a, int l, int r) {
        int n = a[l];
        int il = l, ir = r;
        
        setArea(l, r);
        //push(a);
        
        while (true) {
            while (a[il] < n) {
                push(a, il, ir);
                
                il++;
            }
            
            while (a[ir] > n) {
                push(a, il, ir);
                
                ir--;
            }
            
            push(a, il, ir);
            
            if (il >= ir) {
                clearArea();
                
                return ir;
            }
            
            swap(a, il, ir);
            
            push(a, il, ir);
        }
    }
    
    
    public void timSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int runMinLength = 32;
        
        for (int i = 0; i < a.length; i += runMinLength) {
            setArea(i, Math.min(i + runMinLength - 1, a.length - 1));
            
            insertionSort(a, i, Math.min(i + runMinLength - 1, a.length - 1));
        }
        
        clearArea();
        
        int r;
        for (int run = runMinLength; run < a.length; run *= 2) {
            for (int i = 0; i < a.length; i += run * 2) {
                r = Math.min(i + run * 2 - 1, a.length - 1);
                if (i + run - 1 < r) {
                    merge(a, i, i + run - 1, i + run, r);
                }
            }
        }
        
        push(a, true); // sorted
    }
    
    public void merge(int[] a, int l1, int r1, int l2, int r2) {
        // if array 2 goes first, swap borders of arrays 1 and 2
        if (l1 > l2) {
            temp = l1;
            l1 = l2;
            l2 = temp;
            temp = r1;
            r1 = r2;
            r2 = temp;
        }
        
        setArea(l1, r2);
        
        int[] ha = new int[Math.min(r1 - l1, r2 - l2) + 1]; // will be a copy of the shortest of 2 arrays
        int j = l1;
        
        // array 1 should be shorter and in ha
        // array 2 should be longer and on the right of [l1, r2] interval
        if (r1 - l1 > r2 - l2) { // array 1 is longer
            // copy from array 2 to ha
            for (int i = 0; i < ha.length; i++) {
                ha[i] = a[l2 + i];
            }
            
            // shift all elements of array 1 to the right
            int shift = r2 - l2 + 1;
            for (int i = r1; i >= l1; i--) {
                //push(a, i, i + shift);
                
                a[i + shift] = a[i];
                
                push(a, i, i + shift);
            }
            //System.arraycopy(a, l1, a, l1 + shift, r1 - l1 + 1);
            
            push(a);
            
            // fix indecies
            r1 = r2 - l2;
            l2 = l1 + shift;
            l1 = 0;
        }
        else { // array 2 is longer or equal
            // copy from array 1 to ha
            for (int i = 0; i < ha.length; i++) {
                ha[i] = a[l1 + i];
                
                //push(a, l1 + i);
            }
            
            //push(a);
            
            // fix indecies
            r1 -= l1;
            l1 = 0;
        }
        
        int i1 = l1, i2 = l2;
        // i1    - ha
        // j, i2 - a
        while (i1 <= r1 && i2 <= r2) {
            //push(a, j, i2);
            
            if (ha[i1] < a[i2]) {
                a[j] = ha[i1];
                
                push(a, j, i2);
                
                i1++;
            }
            else {
                a[j] = a[i2];
                
                push(a, j, i2);
                
                i2++;
            }
            j++;
        }
        
        //push(a);
        
        // add elements that are left
        while (i1 <= r1) { // elements left in arrray 1
            //push(a, j);
            
            a[j] = ha[i1];
            
            push(a, j);
            
            i1++;
            j++;
        }
        // if elements left in array 2, they are already placed correctly
    
        clearArea();
    }
    
    
    public void shellSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int j, next;
        
        for (int gap = a.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < a.length; i++) {
                next = a[i];
                j = i;
                
                push(a, i);
                
                while (j >= gap && a[j - gap] > next) {
                    //push(a, j, j - gap);
                    
                    a[j] = a[j - gap];
                    
                    push(a, j, j - gap);
                    
                    j -= gap;
                }
                
                a[j] = next;
                
                push(a, j);
            }
        }
        
        push(a, true); // sorted
    }
    
    
    public void radixSort10LSD() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int m = arrayStates.get(0).maxElement();
 
        for (int exp = 1; m / exp > 0; exp *= 10) {
            countradix10LSD(a, exp);
        }
        
        push(a, true); // sorted
    }

    void countradix10LSD(int[] a, int exp) {
        int[] res = new int[a.length];
        int[] count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0 x10
        int i;
 
        for (i = 0; i < a.length; i++) {
            count[a[i] / exp % 10]++;
            
            push(a, i);
        }
 
        for (i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
 
        for (i = a.length - 1; i >= 0; i--) {
            res[count[a[i] / exp % 10] - 1] = a[i];
            count[a[i] / exp % 10]--;
        }
 
        for (i = 0; i < a.length; i++) {
            a[i] = res[i];
            
            push(a, i);
        }
    }
    
    
    public void radixSort10MSD() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int m = arrayStates.get(0).maxElement();
 
        int exp = 1;
        while (m / (exp * 10) > 0) {
            exp *= 10;
        }
        
        radix10MSD(a, 0, a.length - 1, exp);
        
        push(a, true); // sorted
    }
    
    public void radix10MSD(int[] a, int l, int r, int exp) {
        if (r <= l) {
            return;
        }
        
        int[] res = new int[r - l + 1];
        int[] count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0 x10
        int[] countCopy = new int[10];
        int i;
 
        setArea(l, r);
        
        for (i = l; i <= r; i++) {
            count[a[i] / exp % 10]++;
            
            push(a, i);
        }
 
        for (i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        System.arraycopy(count, 0, countCopy, 0, 10);
 
        for (i = r; i >= l; i--) {
            res[count[a[i] / exp % 10] - 1] = a[i];
            count[a[i] / exp % 10]--;
        }
 
        for (i = l; i <= r; i++) {
            a[i] = res[i - l];
            
            push(a, i);
        }
        
        clearArea();
        
        exp /= 10;
        radix10MSD(a, l, l + countCopy[0] - 1, exp);
        for (i = 0; i <= 8; i++) {
            radix10MSD(a, l + countCopy[i], l + countCopy[i + 1] - 1, exp);
        }
    }
    
    
    public void countingSort() {
        int[] a = arrayStates.get(arrayStates.size() - 1).clone().getArray();
        
        int i;
        int ma = arrayStates.get(0).maxElement();
        int mi = arrayStates.get(0).minElement();
        
        int[] count = new int[ma - mi + 1];
        int[] res = new int[a.length];
        
        for (i = 0; i < a.length; i++) {
            count[a[i] - mi]++;
            
            push(a, i);
        }
 
        for (i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
 
        for (i = a.length - 1; i >= 0; i--) {
            res[count[a[i] - mi] - 1] = a[i];
            count[a[i] - mi]--;
        }
 
        for (i = 0; i < a.length; i++) {
            a[i] = res[i];
            
            push(a, i);
        }
        
        push(a, true); // sorted
    }
    
    
    
    private void printArrayInt(int[] a) {
        System.out.print("[");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + ", ");
        }
        System.out.println("]");
    }
    
    
    
    private void swap(int[] a, int i1, int i2) {
        temp = a[i1];
        a[i1] = a[i2];
        a[i2] = temp;
    }
    
    private int randInt(int l, int r) { // inclusive, exclusive
        return (int) (Math.random() * (r - l)) + l;
    }
    
    public void shuffle(int[] a) {
        int len = a.length;
        for (int i = 0; i < len - 1; i++) {
            swap(a, i, randInt(i, len));
        }
    }
}
