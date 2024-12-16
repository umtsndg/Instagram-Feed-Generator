import java.util.ArrayList;

public class PostHeap {
    private String userId;
    public ArrayList<Post> heap;

    public MyHashMap<Post, Integer> postIndexMap;


    public PostHeap(String userId) {
        this.userId = userId;
        this.heap = new ArrayList<>();
        this.postIndexMap = new MyHashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void addPost(Post post) {
        heap.add(post);
        int index = heap.size() - 1;
        postIndexMap.put(post, index);
        heapifyUp(index);
    }

    public Post removeTopPost() {
        if (isEmpty()) {
            return null;
        }

        Post topPost = heap.get(0);

        heap.set(0, heap.get(heap.size() - 1));
        postIndexMap.put(heap.get(0), 0); // Update the map for the new root
        heap.remove(heap.size() - 1);
        postIndexMap.remove(topPost); // Remove the top post from the map

        if (!isEmpty()) {
            heapifyDown(0);
        }

        return topPost;
    }


    public void updatePost(Post post, int like) {

        Integer index = postIndexMap.get(post);
        if (index != null) {
            if(like == 0) {
                heapifyUp(index);
            }
            else {
                heapifyDown(index);
            }
        }
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    public void removePost(Post post) {
        Integer index = postIndexMap.get(post);
        if (index == null) {
            // Post not found in the heap
            return;
        }

        int lastIndex = heap.size() - 1;
        Post lastPost = heap.get(lastIndex);

        // Swap the post to remove with the last post
        swap(index, lastIndex);

        // Remove the last element (which is the post to remove)
        heap.remove(lastIndex);
        postIndexMap.remove(post);

        // If we removed the last element, no need to heapify
        if (index == lastIndex) {
            return;
        }

        // Since we swapped the last element to index 'index', we need to restore the heap property
        int parentIndex = (index - 1) / 2;

        if (index > 0 && heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
            // If the moved post is greater than its parent, heapify up
            heapifyUp(index);
        } else {
            // Else, heapify down
            heapifyDown(index);
        }
    }


    private void heapifyDown(int index) {
        int size = heap.size();
        while (index < size) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int largest = index;

            if (leftChildIndex < size && heap.get(leftChildIndex).compareTo(heap.get(largest)) > 0) {
                largest = leftChildIndex;
            }

            if (rightChildIndex < size && heap.get(rightChildIndex).compareTo(heap.get(largest)) > 0) {
                largest = rightChildIndex;
            }

            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Post temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        postIndexMap.put(heap.get(i), i);
        postIndexMap.put(heap.get(j), j);
    }

}