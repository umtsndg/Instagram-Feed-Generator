import java.util.ArrayList;

public class User {
    private String userId;
    private MyHashMap<String, User> following; // User IDs this user is following
    private MyHashMap<String, User> followers; // User IDs following this user
    private MyHashMap<String, Post> posts;     // Post IDs created by this user

    private ArrayList<Post> feed;

    private MyHashMap<String, Post> seenPosts;
    public User(String userId) {
        this.userId = userId;
        this.following = new MyHashMap<>();
        this.followers = new MyHashMap<>();
        this.posts = new MyHashMap<>();
        this.seenPosts = new MyHashMap<>();
        feed = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public MyHashMap<String, User> getFollowing() {
        return following;
    }

    public MyHashMap<String, User> getFollowers() {
        return followers;
    }

    public MyHashMap<String, Post> getPosts() {
        return posts;
    }

    public MyHashMap<String, Post> getSeenPosts() {return seenPosts;}

    public ArrayList<Post> getFeed() {return feed;}

    public void setFeed(ArrayList<Post> feed) {this.feed = feed;}

    public int followUser(User followe){
        if(this.following.get(followe.userId) == null){
            followe.addFollower(this);
            addFollowing(followe);
            for (String string: followe.getPosts().keySet()){
                if(FeedManager.feedMap.get(this.userId).postIndexMap.get(FeedManager.postMap.get(string)) == null) {
                    FeedManager.feedMap.get(this.userId).addPost(FeedManager.postMap.get(string));
                }
            }
            return 0;
        }
        else{
            return 1;
        }
    }

    public int unfollowUser(User unfollowe){
        if(this.following.get(unfollowe.userId) != (null)){
            removeFollowing(unfollowe);
            unfollowe.removeFollower(this);

            for (String string: unfollowe.getPosts().keySet()){
                if(FeedManager.feedMap.get(this.userId).postIndexMap.get(FeedManager.postMap.get(string)) != null) {
                    FeedManager.feedMap.get(this.userId).removePost(FeedManager.postMap.get(string));
                }
            }

            return 0;
        }
        else{
            return 1;
        }
    }

    private void addFollower(User user) {
        this.followers.put(user.getUserId(), user);
    }

    private void addFollowing(User user){
        this.following.put(user.getUserId(), user);
    }

    private void removeFollowing(User user){
        this.following.remove(user.getUserId());
    }

    private void removeFollower(User user) {
        this.followers.remove(user.getUserId());
    }

    public void addPost(String postId) {
        this.posts.put(postId, FeedManager.postMap.get(postId));
    }

    public Post[] sortPosts() {
        Iterable<String> postIds = posts.keySet();

        ArrayList<Post> postList = new ArrayList<>();

        for (String postId : postIds) {
            Post post = posts.get(postId);
            if (post != null) {
                postList.add(post);
            }
        }



        Post[] postsArray = postList.toArray(new Post[postList.size()]);

        if (postsArray.length == 0) {
            return new Post[0];
        }

        quickSort(postsArray, 0, postsArray.length - 1);

        return postsArray;
    }

    private void quickSort(Post[] postsArray, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(postsArray, low, high);
            quickSort(postsArray, low, pivotIndex - 1);
            quickSort(postsArray, pivotIndex + 1, high);
        }
    }

    private int partition(Post[] postsArray, int low, int high) {
        Post pivot = postsArray[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (postsArray[j].compareTo(pivot) > 0) {
                i++;
                swap(postsArray, i, j);
            }
        }
        swap(postsArray, i + 1, high);
        return i + 1;
    }

    private void swap(Post[] postsArray, int i, int j) {
        Post temp = postsArray[i];
        postsArray[i] = postsArray[j];
        postsArray[j] = temp;
    }


    // Additional methods as needed
}