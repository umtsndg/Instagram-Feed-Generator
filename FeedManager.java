import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class FeedManager {
    public static FileWriter writer;
    public static MyHashMap<String, User> userMap;        // Maps user IDs to User objects
    public static MyHashMap<String, Post> postMap;        // Maps post IDs to Post objects
    public static MyHashMap<String, PostHeap> feedMap;    // Maps user IDs to their feed heaps

    public static PostHeap generalHeap = new PostHeap("0");


    public FeedManager() {
        userMap = new MyHashMap<>();
        postMap = new MyHashMap<>();
        feedMap = new MyHashMap<>();
    }

    // Create a new user
    public int createUser(String userId) throws IOException {
        if (userMap.get(userId) != null) {
            return 1;
        }
        User user = new User(userId);
        userMap.put(userId, user);
        feedMap.put(userId, new PostHeap(userId));

        return 0;
    }

    // User follows another user
    public int followUser(String followerId, String followeeId) {
        User follower = userMap.get(followerId);
        User followee = userMap.get(followeeId);


        if (follower == null || followee == null) {
            return 1;
        }
        if(followee == follower){
            return 1;
        }

        return follower.followUser(followee);
    }

    public int unfollowUser(String followerId, String followeeId) {
        User follower = userMap.get(followerId);
        User followee = userMap.get(followeeId);

        if (follower == null || followee == null) {
            return 1;
        }

        return follower.unfollowUser(followee);
    }

    // User creates a new post
    public int createPost(String authorId, String postId, String content) {
        User author = userMap.get(authorId);
        if (author == null) {
            return 1;
        }

        if(postMap.get(postId) == null) {
            Post post = new Post(postId, authorId, content);
            postMap.put(postId, post);

            author.addPost(postId);

            // Update feeds of followers
            for (String followerId : author.getFollowers().keySet()) {
                PostHeap feedHeap = feedMap.get(followerId);
                if (feedHeap != null) {
                    feedHeap.addPost(post);
                    generalHeap.addPost(post);
                }
            }
            return 0;
        }
        else{
            return 1;
        }
    }

    // User likes or unlikes a post
    public int toggleLikePost(String userId, String postId) {
        Post post = postMap.get(postId);
        if (post == null) {
            return -1;
        }

        User user = userMap.get(userId);
        if (user == null) {
            return -1;
        }

        if (post.likePost(userId)) {
            userMap.get(userId).getSeenPosts().put(postMap.get(postId).getPostId(), postMap.get(postId));
            updatePostInFeeds(post, 0);
            return 1;
        }
        else if(post.unlikePost(userId)){
            userMap.get(userId).getSeenPosts().put(postMap.get(postId).getPostId(), postMap.get(postId));
            updatePostInFeeds(post, 1);
            return 2;
        }
        else{
            return -1;
        }
    }

    // Update the post in all relevant feeds
    private void updatePostInFeeds(Post post, int like) {
        // Get the author of the post
        User author = userMap.get(post.getAuthorId());
        if (author != null) {
            // Update the post in the feeds of all followers
            for (String followerId : author.getFollowers().keySet()) {
                PostHeap feedHeap = feedMap.get(followerId);
                if (feedHeap != null) {
                    feedHeap.updatePost(post, like);
                    generalHeap.updatePost(post, like);
                }
            }
        }
    }

    // Generate feed for a user (modified to use PostHeap directly)
    public String generateFeed(String userId, int numberOfPosts, int scroll) throws IOException {
        String out = "";
        PostHeap feedHeap = feedMap.get(userId);
        if (feedHeap == null) {
            return "Some error occurred in generate_feed.\n";
        }

        ArrayList<Post> inFeed = new ArrayList<>();
        userMap.get(userId).setFeed(new ArrayList<>());
        out += "Feed for " + userId + ":\n";
        int count = 0;
        while (!feedHeap.isEmpty() && count < numberOfPosts) {
            Post post = feedHeap.removeTopPost();
            if(userMap.get(userId).getSeenPosts().get(post.getPostId()) == null && userMap.get(userId).getFollowing().get(post.getAuthorId()) != null) {
                out += "Post ID: " + post.getPostId() + ", Author: " + post.getAuthorId() +
                        ", Likes: " + post.getLikeCount() + "\n";
                userMap.get(userId).getFeed().add(post);
                inFeed.add(post);
                if(scroll == 1){
                    seePost(userId, post.getPostId());
                }
                count++;
            }
        }
        if(count < numberOfPosts){
            out += "No more posts available for " + userId + ".\n";
        }
        for(Post post: inFeed){
            feedHeap.addPost(post);
        }
        return out;
    }

    public int seePost(String userId, String postId){
        Post post = postMap.get(postId);
        if (post == null) {
            return 1;
        }

        User user = userMap.get(userId);
        if (user == null) {
            return 1;
        }
        userMap.get(userId).getSeenPosts().put(postMap.get(postId).getPostId(), postMap.get(postId));
        return 0;
    }

    public int seeAllPostsFromUser(String viewerId, String viewedId){
        try {
            User viewed;
            User viewer;
            viewed = userMap.get(viewedId);
            viewer = userMap.get(viewerId);

            if(viewer == null){
                return 1;
            }
            for (String postId : viewed.getPosts().keySet()) {
                seePost(viewerId, postId);
                Post post = postMap.get(postId);
                viewer.getSeenPosts().put(postId, post);
            }
            return 0;
        }
        catch (Exception e){
            return 1;
        }
    }

    public String scrollThroughFeed(String userId, int num, String[] likes) throws IOException {
        String out = "";
        generateFeed(userId, num, 1);
        User user = userMap.get(userId);
        if(user == null){
            return "Some error occurred in scroll_through_feed.\n";
        }
        ArrayList<Post> feed = user.getFeed();
        out += userId + " is scrolling through feed:\n";
        for(int i = 0; i < num; i++){
            if(i == feed.size()){
                out +="No more posts in feed.\n";
                break;
            }
            Post post = postMap.get(feed.get(i).getPostId());
            user.getSeenPosts().put(post.getPostId(), post);
            out += userId + " saw " + post.getPostId() + " while scrolling";
            if(Integer.parseInt(likes[i]) == 1){
                toggleLikePost(userId, post.getPostId());
                out += " and clicked the like button";
            }
            out +=".\n";
        }
        return out;

    }
    public String sortPosts(String userId) throws IOException {
        String out = "";
        try {
            Post[] arr = userMap.get(userId).sortPosts();
            if (arr.length == 0) {
                out += "No posts from" + userId + ".\n";
                return out;
            }
            out += "Sorting " + userId + "'s posts:\n";
            for (int i = 0; i < arr.length; i++) {
                out += arr[i].getPostId() + ", " + "Likes: " + arr[i].getLikeCount() + "\n";
                if(i == arr.length - 1){
                    break;
                }

            }
            return out;
        }
        catch (Exception e){
            out = "Some error occurred in sort_posts.\n";
            return out;
        }
    }

    // Additional methods as needed
}