package poo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CalculadoraInterface extends JFrame {

    private JTextField primeiroNumero;
    private JTextField segundoNumero;
    private JComboBox<String> operacao;
    private JLabel resultado;
    private Numero numero;

    public CalculadoraInterface() {
        numero = new Numero();
        montarJanela();
    }

    private void montarJanela() {
        setTitle("Calculadora");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel titulo = new JLabel("Calculadora", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        primeiroNumero = new JTextField();
        segundoNumero = new JTextField();
        operacao = new JComboBox<>(new String[] {
                "Soma",
                "Subtração",
                "Multiplicação",
                "Divisão"
        });

        formulario.add(new JLabel("Primeiro número:"));
        formulario.add(primeiroNumero);
        formulario.add(new JLabel("Segundo número:"));
        formulario.add(segundoNumero);
        formulario.add(new JLabel("Operação:"));
        formulario.add(operacao);

        JButton calcular = new JButton("Calcular");
        JButton limpar = new JButton("Limpar");

        calcular.addActionListener(this::calcular);
        limpar.addActionListener(e -> limparCampos());

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 10));
        botoes.add(calcular);
        botoes.add(limpar);

        resultado = new JLabel("Resultado: ", SwingConstants.CENTER);
        resultado.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel rodape = new JPanel(new BorderLayout(10, 10));
        rodape.add(botoes, BorderLayout.NORTH);
        rodape.add(resultado, BorderLayout.SOUTH);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(formulario, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        add(painel);
    }

    private void calcular(ActionEvent evento) {
        try {
            double x = lerNumero(primeiroNumero);
            double y = lerNumero(segundoNumero);
            double valor = 0;

            switch (operacao.getSelectedIndex()) {
                case 0:
                    valor = numero.soma(x, y);
                    break;
                case 1:
                    valor = numero.subtracao(x, y);
                    break;
                case 2:
                    valor = numero.multiplicacao(x, y);
                    break;
                case 3:
                    valor = numero.divisao(x, y);
                    break;
                default:
                    break;
            }

            resultado.setText("Resultado: " + formatar(valor));
        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Digite apenas números válidos.");
        } catch (ArithmeticException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage());
        }
    }

    private double lerNumero(JTextField campo) {
        return Double.parseDouble(campo.getText().trim().replace(",", "."));
    }

    private String formatar(double valor) {
        if (valor == Math.rint(valor)) {
            return String.valueOf((long) valor);
        }

        return String.valueOf(valor).replace(".", ",");
    }

    private void limparCampos() {
        primeiroNumero.setText("");
        segundoNumero.setText("");
        operacao.setSelectedIndex(0);
        resultado.setText("Resultado: ");
        primeiroNumero.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraInterface().setVisible(true));
    }
}
