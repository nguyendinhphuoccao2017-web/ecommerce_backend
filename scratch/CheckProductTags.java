import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckProductTags {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres";
        String user = "postgres.nddvgywmwxlmkmextxre";
        String password = "Phuoccao_123";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connecting to Database...");
            
            // Check products
            System.out.println("=== PRODUCTS ===");
            ResultSet rs = stmt.executeQuery("SELECT id, product_name, published FROM products LIMIT 5");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + " | Name: " + rs.getString("product_name") + " | Published: " + rs.getBoolean("published") + " (wasNull: " + rs.wasNull() + ")");
            }

            // Check tags
            System.out.println("=== TAGS ===");
            ResultSet rs2 = stmt.executeQuery("SELECT id, tag_name FROM tags LIMIT 5");
            while (rs2.next()) {
                System.out.println("Tag ID: " + rs2.getString("id") + " | Name: " + rs2.getString("tag_name"));
            }

            // Check product_tags
            System.out.println("=== PRODUCT_TAGS ===");
            ResultSet rs3 = stmt.executeQuery("SELECT product_id, tag_id FROM product_tags LIMIT 5");
            while (rs3.next()) {
                System.out.println("ProductID: " + rs3.getString("product_id") + " | TagID: " + rs3.getString("tag_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
