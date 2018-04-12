package distributed_algs.homework_2.merge_sort;

import java.util.concurrent.RecursiveAction;

public class MergeAction extends RecursiveAction {

    private int[] array;
    private int fromIndex;
    private int toIndex;

    MergeAction(int[] array, int fromIndex, int toIndex) {
        this.array = array;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @Override
    protected void compute() {
        int length = toIndex - fromIndex;

        // exit from recursion
        if (length <= 1) return;

        int indexOfSplit = fromIndex + length / 2;

        invokeAll(new MergeAction(array, fromIndex, indexOfSplit),
                new MergeAction(array, indexOfSplit, toIndex));
        merge(fromIndex, indexOfSplit, toIndex);
    }

    private void merge(int start, int middle, int end) {
        int[] localArray = new int[end - start];

        int leftIndex = start;
        int rightIndex = middle;
        int currentIndex = 0;

        while (leftIndex < middle && rightIndex < end) {
            int left = array[leftIndex];
            int right = array[rightIndex];

            if (left <= right) {
                localArray[currentIndex] = array[leftIndex];
                leftIndex++;
            } else {
                localArray[currentIndex] = array[rightIndex];
                rightIndex++;
            }
            currentIndex++;
        }

        if (leftIndex < middle) {
            System.arraycopy(array, leftIndex, localArray, currentIndex, middle - leftIndex);
        } else if (rightIndex < end) {
            System.arraycopy(array, rightIndex, localArray, currentIndex, end - rightIndex);
        }

        System.arraycopy(localArray, 0, array, start, localArray.length);
    }
}
