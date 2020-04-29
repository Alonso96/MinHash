package minhash;

import java.util.Arrays;




public class RadixSort {

    private final int radix;

    public RadixSort() {
        this(Byte.SIZE);
    }

    public RadixSort(int radix) {
        assert 32 % radix== 0;
        this.radix= radix;
    }

    public void sort(Long[] data) {
        int[] histogram = new int[(1 << radix) + 1];
        long shift = 0;
        long mask = (1 << radix) - 1;
        long[] copy = new long[data.length];
        int index;
        while (shift < Long.SIZE) {
            Arrays.fill(histogram, 0);
            for (int i = 0; i < data.length; ++i) {
                ++histogram[(int) ((data[i] & mask) >>> shift) + 1];
            }
            for (int i = 0; i < 1 << radix; ++i) {
                histogram[i + 1] += histogram[i];
            }
            for (int i = 0; i < data.length; ++i) {
                index = (int) ((data[i] & mask) >>> shift);
                copy[histogram[ index]++] = data[i];
            }
            for (int i = 0; i < data.length; ++i) {
                data[i] = copy[i];
            }
            shift += radix;
            mask <<= radix;
        }
    }
}



