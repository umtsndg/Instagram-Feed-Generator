import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        FeedManager feedManager = new FeedManager();

        File file = new File("src/type2_large1.txt");
        BufferedReader scanner = new BufferedReader(new FileReader(file));
        FileWriter writer = new FileWriter("out.txt");

        String[] data;
        FeedManager.writer = writer;
        while (scanner.ready()){
            String x = scanner.readLine();
            data = x.split(" ");
            int status;

            if(data[0].equals("create_user")){
                status = feedManager.createUser(data[1]);
                if(status == 1){
                    writer.write("Some error occurred in create_user.\n");
                }
                else{
                    writer.write("Created user with Id " + data[1] + ".\n");
                }
            }

            else if(data[0].equals("follow_user")){
                status = feedManager.followUser(data[1], data[2]);
                if(status == 1){
                    writer.write("Some error occurred in follow_user.\n");
                }
                else if(status == 0){
                    writer.write(data[1] + " followed " + data[2] + ".\n");
                }
            }

            else if(data[0].equals("unfollow_user")){
                status = feedManager.unfollowUser(data[1], data[2]);
                if(status == 1){
                    writer.write("Some error occurred in unfollow_user.\n");
                }
                else if(status == 0){
                    writer.write(data[1] + " unfollowed " + data[2] + ".\n");
                }
            }

            else if(data[0].equals("create_post")){
                status = feedManager.createPost(data[1], data[2], data[3]);
                if(status == 0){
                    writer.write(data[1] + " created a post with Id " + data[2] + ".\n");
                }
                else if(status == 1){
                    writer.write("Some error occurred in create_post.\n");
                }
            }

            else if(data[0].equals("see_post")){
                status = feedManager.seePost(data[1], data[2]);
                if(status == 1){
                    writer.write("Some error occurred in see_post.\n");
                }
                else{
                    writer.write(data[1] + " saw " + data[2] + ".\n");
                }
            }

            else if(data[0].equals("see_all_posts_from_user")){
                status = feedManager.seeAllPostsFromUser(data[1], data[2]);
                if(status == 0){
                    writer.write(data[1] + " saw all posts of " + data[2] + ".\n");
                }
                else{
                    writer.write("Some error occurred in see_all_posts_from_user.\n");
                }
            }

            else if(data[0].equals("generate_feed")){
                String out = feedManager.generateFeed(data[1], Integer.parseInt(data[2]), 0);
                writer.write(out);
            }

            else if(data[0].equals("toggle_like")){

                status = feedManager.toggleLikePost(data[1], data[2]);
                if(status == -1){
                    writer.write("Some error occurred in toggle_like.\n");
                }
                else if(status == 1){
                    writer.write(data[1] + " liked " + data[2] + ".\n");
                }
                else{
                    writer.write(data[1] + " unliked " + data[2] + ".\n");
                }
            }

            else if(data[0].equals("scroll_through_feed")){
                String out;
                out = feedManager.scrollThroughFeed(data[1], Integer.parseInt(data[2]), Arrays.copyOfRange(data, 3, data.length));
                writer.write(out);
            }

            else if(data[0].equals("sort_posts")){

                String out = feedManager.sortPosts(data[1]);
                writer.write(out);
            }


        }
        writer.flush();
        writer.close();
        System.out.println(System.currentTimeMillis() - startTime);
    }
}