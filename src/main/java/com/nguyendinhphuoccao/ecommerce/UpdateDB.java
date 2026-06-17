package com.nguyendinhphuoccao.ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateDB {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres";
        String user = "postgres.nddvgywmwxlmkmextxre";
        String pass = "Phuoccao_123";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            System.out.println("Updating slideshow_2...");
            String updateSql = "UPDATE slideshows SET image = 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_2.png' WHERE display_order = 2";
            stmt.executeUpdate(updateSql);

            System.out.println("Checking for slideshow_3...");
            String checkSql = "SELECT count(*) FROM slideshows WHERE display_order = 3";
            ResultSet rs = stmt.executeQuery(checkSql);
            rs.next();
            if (rs.getInt(1) == 0) {
                System.out.println("Inserting slideshow_3...");
                String insertSql = "INSERT INTO slideshows (id, image, title, placeholder, display_order, published, clicks, created_at, updated_at) VALUES (gen_random_uuid(), 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_3.png', 'Slideshow 3', 'placeholder', 3, true, 0, now(), now())";
                stmt.executeUpdate(insertSql);
            } else {
                System.out.println("Updating slideshow_3...");
                String updateSql3 = "UPDATE slideshows SET image = 'https://nddvgywmwxlmkmextxre.supabase.co/storage/v1/object/public/slideshow-images/slideshow_3.png' WHERE display_order = 3";
                stmt.executeUpdate(updateSql3);
            }

            System.out.println("Successfully updated DB!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
