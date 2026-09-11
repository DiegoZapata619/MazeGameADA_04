package game.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/*
    Ventana de diálogo que muestra la tabla de puntajes guardados,
    leyénddolos del archivo scores.txt y ordenándolos por nivel alcanzado.
 */

public class ScoreGui extends JDialog implements ActionListener {

    public ScoreGui() {
        super();
    }

    //Lee scores.txt, agrupa los puntajes por nivel alcanzado y los despliega
    //en pantalla junto con un botón para cerrar el diálogo.
    public void ScoreGui()
    {
        Container cp = getContentPane();
        JButton ok = new JButton("OK");
        ok.setActionCommand("OK");
        ok.addActionListener(this);
        int lineNum = 0;
        cp.add(ok, BorderLayout.SOUTH);
        try {
            String line = "";
            String[] myScoreArray = new String[100];
            for (int i = 0; i < myScoreArray.length; i++)
                myScoreArray[i] = " ";
            String line1 = "";
            BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("scores.txt")));//read in the scores data
            int recordsCount = 0;
            while ((line = br1.readLine()) != null) {
                line = br1.readLine();
                if (line != "") {
                    recordsCount += 1;
                    //El asterisco marca el inicio del nivel alcanzado, usado para ordenar
                    int tempPOS = line.indexOf("*");
                    String pos = line.substring(tempPOS + 1);
                    int index = Integer.parseInt(pos);
                    if (myScoreArray[index] == " ")
                        myScoreArray[index] = line; //Se agrega el puntaje en su posición
                    else {
                        for (int i = 0; i < myScoreArray.length; i++) {
                            if (index + i < myScoreArray.length) //Evita salirse del arreglo
                            {
                                if (myScoreArray[index + i].equals(" ")) {
                                    myScoreArray[index + 1] = line; //Se agrega en la siguiente posición libre
                                }
                            }
                        }
                    }
                    JPanel scorePanel = new JPanel();
                    scorePanel.setLayout(new GridLayout(recordsCount, recordsCount));
                    for (int i = 0; i < myScoreArray.length; i++) {
                        if (myScoreArray[i] != " ") {
                            mainLabel = new JLabel(myScoreArray[i], JLabel.LEFT);
                            scorePanel.add(mainLabel);
                        }
                    }
                    cp.add(scorePanel);
                }
            }
        }
        catch (IOException ex) {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Hubo un problema al leer scores.txt. No se pudieron cargar los puntajes");
        }
        pack();
        setVisible(true);
    }

    //Cierra el diálogo al presionar el botón "OK".
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    private JLabel mainLabel;
}