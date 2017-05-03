/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JOptionPane;

/**
 *
 * @author Yann
 */
public class Modele extends Observable {

    private Case tableDeJeu[][] = new Case[8][8];
    private Color couleurs[] = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.WHITE, Color.ORANGE, Color.MAGENTA, Color.BLUE, Color.ORANGE};
    private int pointage = 0 , temps=60;
    private LinkedBlockingQueue<Integer> listInt = new LinkedBlockingQueue();
    
    BufferedReader lecteur;
    PrintWriter ecriture;

    
    

    
    public Modele() {
        creerTable();

    }

    /**
     * Crée la table de jeu et la remplir avec des cases
     */
    public void creerTable() {
        Random rdm = new Random();
        int numeroRdm;
        pointage = 0;
        temps = 60;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
                Case cas = new Case(i, j, couleurs[numeroRdm]);
                tableDeJeu[i][j] = cas;
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
                if (tableDeJeu[i][j].getCouleur() == tableDeJeu[i + 1][j].getCouleur()
                        && tableDeJeu[i][j].getCouleur() == tableDeJeu[i + 2][j].getCouleur()) {
                    for (Color coul : couleurs) {
                        if (coul != tableDeJeu[i][j].getCouleur()) {
                            tableDeJeu[i + 1][j].setCouleur(coul);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
                if (tableDeJeu[i][j].getCouleur() == tableDeJeu[i][j + 1].getCouleur()
                        && tableDeJeu[i][j].getCouleur() == tableDeJeu[i][j + 2].getCouleur()) {
                    for (Color coul : couleurs) {
                        if (coul != tableDeJeu[i][j].getCouleur()) {
                            tableDeJeu[i][j + 1].setCouleur(coul);
                        }
                    }
                }
            }
        }
        setChanged();
        notifyObservers();
        //S'assure de pas avoir de match dès le debut
        /*for (int i = 0; i < 7; i++) {
         for (int j = 1; j < 6; j++) {

         for (int k = 1; k < 6; k++) {
         for (int l = 0; l < 7; l++) {
         numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
         if (tableDeJeu[k][l].getCouleur() == tableDeJeu[k + 1][l].getCouleur()
         && tableDeJeu[k][l].getCouleur() == tableDeJeu[k - 1][l].getCouleur()) {
         if (k != l) {
         tableDeJeu[k][l].setCouleur(tableDeJeu[l][k].getCouleur());
         }
         if (k == l) {
         tableDeJeu[k][l].setCouleur(couleurs[numeroRdm]);
         }

         }
         }
         }

         numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
         if (tableDeJeu[i][j].getCouleur() == tableDeJeu[i][j + 1].getCouleur()
         && tableDeJeu[i][j].getCouleur() == tableDeJeu[i][j - 1].getCouleur()) {
         if (i != j) {
         tableDeJeu[i][j].setCouleur(tableDeJeu[j][i].getCouleur());
         }
         if (i == j) {
         tableDeJeu[i][j].setCouleur(couleurs[numeroRdm]);
         }

         }
         }
         } */
    }

    /**
     * Permermet de retourner une case à une position specifique 
     * @param i
     * @param j
     * @return Une case specifique
     */
    public Case getCase(int i, int j) {
        return tableDeJeu[i][j];
    }

    /**
     * Effectue le changement de cases seulement si les conditions sont respectées
     * @param i Coordonnée x du 1er boutton choisi
     * @param j Coordonnée y du 1er boutton choisi
     * @param k Coordonnée x du 2e boutton choisi
     * @param l Coordonnée y du 2e boutton choisi 
     */
    private void effectuerEchange(int i, int j, int k, int l) {

        Case copie[][] = new Case[8][8];

        // Création d'une copie
        for (int m = 0; m < 8; m++) {
            for (int n = 0; n < 8; n++) {
                copie[m][n] = tableDeJeu[m][n];
            }
        }

        Case casetem = new Case();
        casetem = copie[i][j];
        copie[i][j] = copie[k][l];
        copie[k][l] = casetem;

        if( (traiterLigne(i, copie, copie[i][j].getCouleur()) > 2) 
            || (traiterLigne(k, copie, copie[k][l].getCouleur()) > 2)    ){
            //Copie la copie sur l'original
            for (int m = 0; m < 8; m++) {
                for (int n = 0; n < 8; n++) {
                    tableDeJeu[m][n] = copie[m][n];
                }
            }
        }
        else if( (traiterCol(j, copie, copie[i][j].getCouleur()) > 2) 
            || (traiterCol(l, copie, copie[k][l].getCouleur()) > 2)    ){
            //Copie la copie sur l'original
            for (int m = 0; m < 8; m++) {
                for (int n = 0; n < 8; n++) {
                    tableDeJeu[m][n] = copie[m][n];
                }
            }
        } 

    }

    /**
     * Traite une ligne specifique en supprimant les cases qui se suivent et meme couleur
     * @param k Ligne specifique
     * @param copie Copie de la table de jeu
     * @param col Couleur specifique à comparer
     * @return le nombre de cases successives de meme couleurr
     */
    private int traiterLigne(int k, Case copie[][], Color col) {

        int cmpt = 0;
        ArrayList<Integer> cmptr = new ArrayList<Integer>();
        int colonneInit = 0;

        //compte le nb de couleurs successives (cmpt) a partir d'une coloneInit
        for (int j = 0; j < 8; j++) {
            if (copie[k][j].getCouleur() == col) {
                cmpt++;
                if (j==7) {
                   if (cmpt > 2) {
                    colonneInit = j - cmpt;
                    cmptr.add(cmpt);
                    cmptr.add(cmpt);
                } 
                }
            } else {
                if (cmpt > 2) {
                    colonneInit = j - cmpt;
                    cmptr.add(cmpt);
                }

                cmpt = 0;
            }
        }

        if (cmptr.isEmpty()) {
            return 0;
        }

        if (cmptr.get(0) > 2) {

            cmpt = cmptr.get(0);
            //supprime les bonnes valeures dans le tableau
            if (cmpt > 2) {

                for (int j = 0; j < cmpt; j++) {
                    copie[k][colonneInit] = null;
                    colonneInit++;
                }
            }

            //Redéfinit le score
            switch (cmpt) {
                case 3:
                    this.pointage += 5;
                    break;
                case 4:
                    this.pointage += 15;
                    this.temps += 2;
                    break;
                case 5:
                    this.pointage += 30;
                    this.temps += 5;
                    break;

            }

            return cmpt;
        }
        return 0;

    }
    
    /**
     * Traite une colonne specifique en supprimant les cases qui se suivent et meme couleur
     * @param k Colonne specifique
     * @param copie Copie de la table de jeu
     * @param col Couleur specifique à comparer
     * @return le nombre de cases successives de meme couleurr
     */
    private int traiterCol(int k, Case copie[][], Color col) {

        int cmpt = 0;
        ArrayList<Integer> cmptr = new ArrayList<Integer>();
        int ligneInit = 0;

        //compte le nb de couleurs successives (cmpt) a partir d'une coloneInit
        for (int j = 0; j < 8; j++) {
            if (copie[j][k].getCouleur() == col) {
                cmpt++;
                if (j==7) {
                   if (cmpt > 2) {
                    ligneInit = j - cmpt;
                    cmptr.add(cmpt);
                } 
                }
            } else {
                if (cmpt > 2) {
                    ligneInit = j - cmpt;
                    cmptr.add(cmpt);
                }

                cmpt = 0;
            }
        }

        if (cmptr.isEmpty()) {
            return 0;
        }

        if (cmptr.get(0) > 2) {

            cmpt = cmptr.get(0);
            //supprime les bonnes valeures dans le tableau
            if (cmpt > 2) {

                for (int j = 0; j < cmpt; j++) {
                    copie[ligneInit][k] = null;
                    ligneInit++;
                }
            }

            //Redéfinit le score
            switch (cmpt) {
                case 3:
                    this.pointage += 5;
                    break;
                case 4:
                    this.pointage += 15;
                    break;
                case 5:
                    this.pointage += 30;
                    break;

            }

            return cmpt;
        }
        return 0;

    }
    
    /**
     * Verifie si la dictance de déplacement est bonne
     * @param i Coordonnée x du 1er boutton choisi
     * @param j Coordonnée y du 1er boutton choisi
     * @param k Coordonnée x du 2e boutton choisi
     * @param l Coordonnée y du 2e boutton choisi
     * @return true si la distance entre les 2 points est bonne
     */
    private boolean checkMoveDistance(int i, int j, int k, int l) {

        if ((Math.abs(i - k) == 1 || Math.abs(i - k) == 0)
                && (Math.abs(j - l) == 1 || Math.abs(j - l) == 0)) {
            return true;
        }
        return false;
    }
    
    /**
     * Permet d'aller chercher le score
     * @return le score
     */
    public int getPointage() {
        return pointage;
    }

    /**
     * Prend les coordonées du boutton selectionné et effectue le changement apres avoir choisi 2 bouttons
     * @param num1 coordonnée x du boutton choisi
     * @param num2 coordonnée u du boutton choisi
     */
    public void changerCases(int num1, int num2) {
        listInt.add((Integer) num1);
        listInt.add((Integer) num2);

        if (listInt.size() == 4) {
            int i = (int) listInt.poll();
            int j = (int) listInt.poll();
            int k = (int) listInt.poll();
            int l = (int) listInt.poll();

            if ((checkMoveDistance(i, j, k, l) == true)
                    && ((i != k) || (j != l))) {

                effectuerEchange(i, j, k, l);

                checkEtFixLignesVides(tableDeJeu);

                setChanged();
                notifyObservers();
                listInt.clear();

            } else {
                JOptionPane.showMessageDialog(null, "Déplacement illégal !");
                checkEtFixLignesVides(tableDeJeu);
                listInt.clear();
                setChanged();
                notifyObservers();
                
            }
        }

    }

    /**
     * Verifie si le tableau contient des suites & les supprime
     * @param tableDeJeu table de jeu
     */
    private void checkEtFixLignesVides(Case tableDeJeu[][]) {

        LinkedBlockingQueue<Integer> cmptr = new LinkedBlockingQueue();

        int lignInit = 0;

        while (contientNull(tableDeJeu) == true) {
            
            for (int i = 7; i >= 0; i--) {
                for (int j = 7; j >= 0; j--) {
                    if (tableDeJeu[i][j] == null) {
                        
                        if (i != 0)  {
                        tableDeJeu[i][j] = tableDeJeu[i - 1][j];
                        tableDeJeu[i - 1][j] = null;
                        cmptr.add(j);
                        
                       } else if (i == 0) {
                        Random rdm = new Random();
                        int numeroRdm ;
                        numeroRdm = rdm.nextInt(6 - 0 + 1) + 0;
                        tableDeJeu[0][j] = new Case(0, j, couleurs[numeroRdm]);
                    }
                        
                    }
                }
            }
        }

    }
    
    /**
     * Verifie si le tableau contient des elements nulls
     * @param tableDeJeu
     * @return true si il contient des elements nuls
     */
    private boolean contientNull(Case tableDeJeu[][]) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tableDeJeu[i][j] == null) {
                    return true;
                }
            }

        }
        return false;
    }
    
    /**
     * Lis les scores d'un fichier et affiche dans un JOptionPane
     */
    public void afficherScores() {
        String finale = null;
        try {
            lecteur = new BufferedReader(new FileReader("scores.txt"));
            String ligne = lecteur.readLine();
            finale = "Les 5 meilleurs scores :";
            while (ligne != null) {
                finale = finale + "\n " + ligne;
                ligne = lecteur.readLine();
            }
        } catch (IOException e) {
        }
        JOptionPane.showMessageDialog(null, finale);
    }

    /**
     * Méthode qui stocke les scores dans un fichier
     */
    public void stockerScores() {
    ArrayList<String> listeNom = new ArrayList();
        ArrayList<Integer> listeScores = new ArrayList();
        String nom = null;
        int positionAChanger;
        boolean posExiste = true;
        int plusPetit = 0;
        try {
            lecteur = new BufferedReader(new FileReader("scores.txt"));
            String ligne = lecteur.readLine();
            while (ligne != null) {
                String tabNom[] = ligne.split(":");
                int num = Integer.parseInt(tabNom[1]);
                listeNom.add(tabNom[0]);
                listeScores.add(num);
                ligne = lecteur.readLine();
            }

        } catch (IOException e) {
        } finally {
            try {
                lecteur.close();
            } catch (IOException ex) {
            }
        }
        if (!listeNom.isEmpty() && listeNom.size() == 5) {
            Object obj = Collections.min(listeScores);
            plusPetit = (int) obj;
        } else if (listeNom.isEmpty() || listeNom.size() < 5) {
            plusPetit = 0;
            posExiste = false;
        }

        if (pointage > plusPetit && listeNom.size() == 5) {
            boolean reDemander = true;
            while (reDemander) {
                reDemander = false;
                nom = JOptionPane.showInputDialog(null, "Veuillez entrer votre nom :");
                if (nom == null || nom.isEmpty()) {
                    reDemander = true;
                }
            }
            positionAChanger = listeScores.indexOf(plusPetit);
            listeScores.remove(positionAChanger);
            listeNom.remove(positionAChanger);
            listeScores.add(pointage);
            listeNom.add(nom);

        } else if (listeNom.size() < 5) {
            boolean reDemander = true;
            while (reDemander) {
                reDemander = false;
                nom = JOptionPane.showInputDialog(null, "Veuillez entrer votre nom :");
                if (nom == null || nom.isEmpty()) {
                    reDemander = true;
                }
            }
            listeScores.add(pointage);
            listeNom.add(nom);
        }

        try {
            ecriture = new PrintWriter(new FileOutputStream("scores.txt", false));
            for (int i = 0; i < listeNom.size(); i++) {
                ecriture.println(listeNom.get(i) + ":" + listeScores.get(i));
            }
            ecriture.close();
        } catch (FileNotFoundException ex) {
        }
    }

    /**
     * 
     * @return la valeur du temps;
     */
    public int getTemps() {
        return this.temps--;
    }

}
