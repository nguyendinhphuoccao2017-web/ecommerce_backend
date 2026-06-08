import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AlterTableScript {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres";
        String user = "postgres.nddvgywmwxlmkmextxre";
        String password = "Phuoccao_123";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connecting to Database...");
            
            // Auto-generate UUID for id column
            String sql = "ALTER TABLE product_tags ALTER COLUMN id SET DEFAULT gen_random_uuid()";
            stmt.executeUpdate(sql);
            
            System.out.println("Successfully altered product_tags.id to auto-generate UUIDs!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
