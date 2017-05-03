/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu.Separator;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author Yann Ced
 */
public class Bejeweled extends JFrame implements Observer {

    private JPanel pnlCases = new JPanel();
    private JPanel pnlInfos = new JPanel();
    private int temps, score ;
    private JLabel lbltemps = new JLabel("Temps écoulé: " + temps);
    private JLabel lblScore = new JLabel("Score : " + score);
    
    private JMenuBar mnuBar = new JMenuBar();
    private JMenu mnuFich = new JMenu("Fichier");
    private JMenuItem mnuScor = new JMenuItem("Afficher les 5 meilleurs scores...");
    private JMenuItem mnuRegl = new JMenuItem("Afficher les règles...");
    private JMenuItem mnuRestart = new JMenuItem("Recommencer");
    private JMenuItem mnuQuit = new JMenuItem("Quitter le jeu");

    
    private JButton tableau[][] = new JButton[8][8];
    private Modele modele = new Modele();
    private int tempsChrono = 0;
    
    private javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent ae) {
            temps = modele.getTemps();
            int reponse = 0;
            lbltemps.setText("Temps écoulé: " + temps+" s.");
            if(temps == tempsChrono){
                boolean questionner=true;
                
                while (questionner) {
                        questionner = false;
                        reponse = JOptionPane.showConfirmDialog(null, "Voulez-vous recommencer?");
                        
                        if (reponse == 0) {
                            modele.stockerScores();
                            modele.creerTable();
                        }
                        else if (reponse == 1) {
                            modele.stockerScores();
                            System.exit(0);
                        }
                        else {
                            questionner = true;
                        }
                    }
            }
            
        }
    });

    
    /**
     * Constructeur de la fenetre à afficher
     * @param observable 
     */
    public Bejeweled(Observable observable) {
        timer.start();

        this.modele = (Modele) observable;
        observable.addObserver(this);
        
        
        this.setLocation(600,250);

        mnuRestart.addActionListener(new ActionListener() {
            /**
             * Évenement sur le menu
             * Recrée une table et réinitialise le score et le temps
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                modele.creerTable();
                temps = 0;

            }
        });
        
        mnuRegl.addActionListener(new ActionListener() {

            /**
             * Évenement sur le menu
             * Affiche les scores dans un JOptionPane
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Le pointage s'éffectue comme suit:\n"
                        + "3 cases adjacentes: 5 points\n"
                        + "4 cases adjacentes: 15 points\n"
                        + "5 cases adjacentes: 30 points.\n "
                        + "'L' de 5 cases: 10 points. \n"
                        + "         NB: Si des cases sont déjà allignées,\n"
                        + "         Cliquer sur une case adjacente su la meme ligne");
            }
        });
        
        mnuScor.addActionListener(new ActionListener() {
            /**
             * Évenement sur le menu
             * Affiche les scores
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
            modele.afficherScores();
            
            }
        });
        
        mnuQuit.addActionListener(new ActionListener() {
            
            /**
             * Évenement sur le menu
             * Donne l'optoin de quitter
             * @param ae 
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int reponse = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir quitter?");
                if (reponse == 0) {
                    modele.stockerScores();
                    System.exit(0);
                }
            }
        });
        Separator sep = new Separator();
        Separator sep2 = new Separator();

        mnuFich.add(mnuRestart);
        mnuFich.add(sep);
        mnuFich.add(mnuScor);
        mnuFich.add(mnuRegl);
        mnuFich.add(sep2);
        mnuFich.add(mnuQuit);
        mnuBar.add(mnuFich);
        this.setJMenuBar(mnuBar);
        pnlCases.setLayout(new GridLayout(8, 8));

        pnlCases.setBackground(Color.DARK_GRAY);
        setSize(500, 500);

        creerTable(modele);

        pnlInfos.add(lblScore);
        pnlInfos.add(lbltemps);

        add(pnlCases, BorderLayout.CENTER);
        add(pnlInfos, BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    /**
     * Fais la mise à jour de la vue
     * @param o
     * @param arg 
     */
    @Override
    public void update(Observable o, Object arg) {
        Modele modele = (Modele) o;
        majTable(modele);
        
    }

    /**
     * Crée l'affichage de la table de jeu
     * @param modele 
     */
    public void creerTable(Modele modele) {
        
        pnlCases.removeAll();
        fluchTable();
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tableau[i][j] = new JButton();
                tableau[i][j].setBackground(modele.getCase(i, j).getCouleur());
                tableau[i][j].addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        Border border = new LineBorder(Color.LIGHT_GRAY, 2);
                        JButton btn = (JButton) ae.getSource();
                        btn.setBorder(border);
                        btn.setBorderPainted(false);
                        choixCase();
                        
                    }
                });

                pnlCases.add(tableau[i][j]);
            }
        }
        add(pnlCases);

    }
    
    /**
     * Met à jour la table de jeu
     * @param modele 
     */
    public void majTable(Modele modele) {
        score = modele.getPointage();
        lblScore.setText("Score : " + score);
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try{
                tableau[i][j].setBackground((modele.getCase(i, j)).getCouleur()); 
                tableau[i][j].setBorderPainted(true);
                tableau[i][j].setBorder(null);
                }catch (NullPointerException e){
                    System.out.println("Vide" + i+"," + j);
                    
                }
                
            }
        }
       

    }
    
    /**
     * Appelle la methode qu'il faut dans le modele et effectue le changement
     */
    public void choixCase() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                try{
                if (tableau[i][j].isBorderPainted() == false) {
                    modele.changerCases(i, j);
                    tableau[i][j].setBorderPainted(true);
                }
                }catch(NullPointerException e){
                    System.out.println("cases vides dans la vue : "+i+","+j);
                }
            }
        }
    }
    
    /**
     * Vide la table de jeu
     */
    public void fluchTable(){
        this.tableau = new JButton[8][8];
            }
}
