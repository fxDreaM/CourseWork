package coursework;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class PopularDishesForm extends javax.swing.JFrame {

    public PopularDishesForm(java.awt.Frame parent, String title, boolean modal, Connection conn) throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Vector<Vector> dishes_data;
        Vector<String> dishes_column_names;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT dish,COUNT(dish)\n"
                + "FROM orders\n"
                + "LEFT JOIN orders_dishes ON orders.idorder = orders_dishes.orders_idorder\n"
                + "INNER JOIN dishes ON dishes.iddish = orders_dishes.dishes_iddish\n"
                + "GROUP BY dish\n"
                + "ORDER BY COUNT(dish) DESC\n"
        );      
        dishes_data = new Vector<Vector>();
        dishes_column_names = new Vector<String>();
        dishes_column_names.addElement("Наименование");
        dishes_column_names.addElement("Количество продаж");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(String.valueOf(resultSet.getInt(2)));
            dishes_data.add(row);
        }
        jTable.setModel(new DefaultTableModel(dishes_data, dishes_column_names));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
static Connection connec;

    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PopularDishesForm(new javax.swing.JFrame(), "", true,connec).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(PopularDishesForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
