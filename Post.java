public class Post implements Comparable<Post> {
    private String postId;
    private String authorId;
    private String content;
    private int likeCount;
    private MyHashMap<String, String> likedByUsers;

    public Post(String postId, String authorId, String content) {
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.likeCount = 0;
        this.likedByUsers = new MyHashMap<>();
    }

    public String getPostId() {
        return postId;
    }

    public String getAuthorId() {
        return authorId;
    }


    public int getLikeCount() {
        return likeCount;
    }



    // Method to like a post by a user
    public boolean likePost(String userId) {
        if (likedByUsers.get(userId) != null) {
            // User has already liked the post

            return false;
        } else {
            likedByUsers.put(userId, userId);
            likeCount++;
            return true;
        }
    }

    // Method to unlike a post by a user
    public boolean unlikePost(String userId) {
        if (likedByUsers.get(userId) != null) {
            likedByUsers.remove(userId);
            likeCount--;
            return true;
        } else {
            // User has not liked the post
            return false;
        }
    }

    @Override
    public int compareTo(Post other) {
        // Prioritize posts with more likes; if equal, prioritize newer posts
        int likeComparison = Integer.compare(this.likeCount, other.likeCount);

        if (likeComparison != 0) {
            return likeComparison;
        } else {
            return this.postId.compareTo(other.postId);
        }
    }

    // Additional methods as needed
}