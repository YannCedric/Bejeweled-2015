
import com.sun.glass.ui.Cursor;
import java.awt.Color;

public class Case {

    private int ligne = 0, colonne = 0;
    private Color couleur;

    public Case() {
    }

    public Case(int ligne, int colonne, Color couleur) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.couleur = couleur;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public Color getCouleur() {
        return couleur;
    }
    
    public void setPosition(int x, int y) {
        this.ligne = x;
        this.colonne = y;
    }
}
