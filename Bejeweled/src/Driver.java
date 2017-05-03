/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author yann
 */
public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String unLook = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        try {
            UIManager.setLookAndFeel(unLook);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        
        JOptionPane.showMessageDialog(null, "Bienvenue au Jeu Qui Ressemble (plus ou moins) à B'jeweled");

        Modele modele = new Modele();
        
        Bejeweled jeu = new Bejeweled(modele);
        

    }

}
