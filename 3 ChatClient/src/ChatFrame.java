/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JOptionPane;

/**
 *
 * @author Ishfaq Haider 1418-FBAS/BSSE/F11A
 */
public class ChatFrame extends javax.swing.JFrame {

    /**
     * Creates new form ChatFrame
     */
    public ChatFrame() {
        initComponents();
        //JOptionPane.showMessageDialog(null,"ChatFrame Constructor"); //--
        //jMenuBar1.setVisible(false);
        //buttonGroup1.setSelected(jRadioButton1.getModel(),true);
        //this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRadioButtonSignIn = new javax.swing.JRadioButton();
        jRadioButtonSignUp = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItemAddFriend = new javax.swing.JMenuItem();
        jMenuItemRemoveFriend = new javax.swing.JMenuItem();
        jMenuItemLogout = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemChangePassword = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItemSharing = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 25));

        buttonGroup1.add(jRadioButtonSignIn);
        jRadioButtonSignIn.setText("SignIn");

        buttonGroup1.add(jRadioButtonSignUp);
        jRadioButtonSignUp.setText("SignUp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(jRadioButtonSignIn)
                .addGap(18, 18, 18)
                .addComponent(jRadioButtonSignUp)
                .addContainerGap(183, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 2, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonSignIn)
                    .addComponent(jRadioButtonSignUp)))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jMenu1.setText("Manage");

        jMenuItemAddFriend.setText("Add Friend");
        jMenu1.add(jMenuItemAddFriend);

        jMenuItemRemoveFriend.setText("Remove Friend");
        jMenu1.add(jMenuItemRemoveFriend);

        jMenuItemLogout.setText("Logout");
        jMenu1.add(jMenuItemLogout);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Settings");

        jMenuItemChangePassword.setText("Change Password");
        jMenu2.add(jMenuItemChangePassword);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Extras");

        jMenuItemSharing.setText("File Sharing");
        jMenu3.add(jMenuItemSharing);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatFrame().setVisible(true);
            }
        });
        JOptionPane.showMessageDialog(null,"ChatFrame main"); //--
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JMenu jMenu1;
    public javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    public javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenuItem jMenuItemAddFriend;
    public javax.swing.JMenuItem jMenuItemChangePassword;
    public javax.swing.JMenuItem jMenuItemLogout;
    public javax.swing.JMenuItem jMenuItemRemoveFriend;
    public javax.swing.JMenuItem jMenuItemSharing;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JRadioButton jRadioButtonSignIn;
    public javax.swing.JRadioButton jRadioButtonSignUp;
    // End of variables declaration//GEN-END:variables
}
