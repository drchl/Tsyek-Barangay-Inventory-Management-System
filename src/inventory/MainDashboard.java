/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package inventory;

import javax.swing.table.JTableHeader;
import java.awt.Color;

/**
 *
 * @author user
 */
public class MainDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainDashboard.class.getName());

    /**
     * Creates new form MainDashboard
     */
    public MainDashboard() {
        initComponents();
        
        JTableHeader header = inventoryTable.getTableHeader();
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
            );

            label.setBackground(Color.decode("#073559")); // Dark Blue
            label.setForeground(java.awt.Color.WHITE);          // White text
            label.setOpaque(true);
            label.setHorizontalAlignment(javax.swing.JLabel.CENTER); // Optional: center align

            return label;
        }
     });
        
     jScrollPane1.getVerticalScrollBar().setUI(new ModernScrollBarUI());
     jScrollPane1.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
    }   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        panel1 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        inventoryBg = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventoryTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1440, 1024));
        setMinimumSize(new java.awt.Dimension(1440, 1024));
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));

        background.setBackground(new java.awt.Color(9, 54, 96));
        background.setMaximumSize(new java.awt.Dimension(1440, 1024));
        background.setMinimumSize(new java.awt.Dimension(1440, 1024));

        panel1.setBackground(new java.awt.Color(50, 110, 147));
        panel1.setPreferredSize(new java.awt.Dimension(900, 800));
        panel1.setRequestFocusEnabled(false);
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        title.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setText("INVENTORY SYSTEM");
        panel1.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, -1, -1));

        inventoryBg.setBackground(new java.awt.Color(72, 128, 161));

        inventoryTable.setAutoCreateRowSorter(true);
        inventoryTable.setBackground(new java.awt.Color(217, 217, 217));
        inventoryTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        inventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "", null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Item_ID", "Name", "Category", "Quantity", "Unit", "Location", "Condition", "Date_Acquired"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        inventoryTable.setShowGrid(true);
        jScrollPane1.setViewportView(inventoryTable);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventoryBgLayout = new javax.swing.GroupLayout(inventoryBg);
        inventoryBg.setLayout(inventoryBgLayout);
        inventoryBgLayout.setHorizontalGroup(
            inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryBgLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 824, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(inventoryBgLayout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        inventoryBgLayout.setVerticalGroup(
            inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryBgLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(inventoryBgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        panel1.add(inventoryBg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 125, 958, 410));

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                .addContainerGap(466, Short.MAX_VALUE)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

   
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JPanel inventoryBg;
    private javax.swing.JTable inventoryTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panel1;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
