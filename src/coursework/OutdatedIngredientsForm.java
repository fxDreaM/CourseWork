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

public class OutdatedIngredientsForm extends javax.swing.JFrame {

    public OutdatedIngredientsForm(java.awt.Frame parent, String title, boolean modal, Connection conn) throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Vector<Vector> ingredients_data;
        Vector<String> ingredients_column_names;
        PreparedStatement statement = conn.prepareStatement("SELECT ingredient,weight_kg,ingredient_date,ingredient_expiringdate\n"
                + "FROM ingredients\n"
                +"WHERE ingredient_expiringdate<?"
        );
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        statement.setDate(1, date);
        ResultSet resultSet = statement.executeQuery();
        ingredients_data = new Vector<Vector>();
        ingredients_column_names = new Vector<String>();
        ingredients_column_names.addElement("Наименование");
        ingredients_column_names.addElement("Масса(кг)");
        ingredients_column_names.addElement("Дата привоза");
        ingredients_column_names.addElement("Годен до");
        while (resultSet.next()) {
            Vector<String> row = new Vector<String>();
            row.add(resultSet.getString(1));
            row.add(String.valueOf(resultSet.getDouble(2)));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            row.add(dateFormat.format(resultSet.getDate(3)));
            row.add(dateFormat.format(resultSet.getDate(4)));
            ingredients_data.add(row);
        }
        jTable.setModel(new DefaultTableModel(ingredients_data, ingredients_column_names));
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
                    new OutdatedIngredientsForm(new javax.swing.JFrame(), "", true,connec).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(OutdatedIngredientsForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
}
