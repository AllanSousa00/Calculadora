import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculadoraInterface extends JFrame implements ActionListener {

    // Paleta da calculadora. Se mexer aqui, muda a cara dela inteira.
    private final Color preto = new Color(20, 20, 22);
    private final Color cinza = new Color(55, 55, 58);
    private final Color cinzaClaro = new Color(180, 180, 183);
    private final Color laranja = new Color(255, 149, 0);

    // Partes da tela que preciso acessar depois que a janela já foi montada.
    private JLabel visor;
    private JDialog telaPremium;
    private JLabel avisoPremium;

    // A conta fica separada assim: primeiro número, operador e segundo número.
    private double primeiroNumero;
    private double segundoNumero;
    private double resultado;
    private String operador = "";

    // Quando isso fica true, o próximo número digitado limpa o visor.
    private boolean limparVisor = false;

    // 0 não libera nada. 1 libera o básico. 2 e 3 liberam a brincadeira toda.
    private int plano = 0;

    // Classe pequena só para deixar as operações longe da tela.
    private Numero numero = new Numero();

    public CalculadoraInterface() {
        montarJanela();
    }

    private void montarJanela() {
        // Aqui nasce a janela. Sem firula: tamanho fixo para parecer calculadora de celular.
        setTitle("Calculadora");
        setSize(380, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(preto);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        painelPrincipal.add(criarVisor(), BorderLayout.NORTH);
        painelPrincipal.add(criarBotoes(), BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private JPanel criarVisor() {
        // O visor é só um JLabel grandão alinhado para a direita.
        JPanel painelVisor = new JPanel(new BorderLayout());
        painelVisor.setBackground(preto);

        visor = new JLabel("0", SwingConstants.RIGHT);
        visor.setForeground(Color.WHITE);
        visor.setFont(new Font("Arial", Font.PLAIN, 55));
        visor.setPreferredSize(new Dimension(0, 115));

        painelVisor.add(visor, BorderLayout.CENTER);
        return painelVisor;
    }

    private JPanel criarBotoes() {
        // A ordem desse vetor é a ordem que aparece na tela. Se bagunçar aqui, ferrou o teclado.
        JPanel painelBotoes = new JPanel(new GridLayout(5, 4, 8, 8));
        painelBotoes.setBackground(preto);

        String[] textos = {
                "AC", "+/-", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "DEL", "="
        };

        for (String texto : textos) {
            JButton botao = new JButton(texto);
            botao.setFont(new Font("Arial", Font.BOLD, 21));
            botao.setForeground(Color.WHITE);
            botao.setBackground(cinza);
            botao.setFocusable(false);
            botao.setBorderPainted(false);
            botao.addActionListener(this);

            if (texto.equals("AC") || texto.equals("+/-") || texto.equals("%")) {
                botao.setBackground(cinzaClaro);
                botao.setForeground(Color.BLACK);
            }

            if (ehOperador(texto) || texto.equals("=")) {
                botao.setBackground(laranja);
            }

            painelBotoes.add(botao);
        }

        return painelBotoes;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        // Todo botão cai aqui. Aí eu vejo o texto dele e mando para o método certo.
        JButton botaoClicado = (JButton) evento.getSource();
        String texto = botaoClicado.getText();

        if (texto.equals("AC")) {
            limparTudo();
        } else if (texto.equals("+/-")) {
            trocarSinal();
        } else if (texto.equals("%")) {
            calcularPorcentagem();
        } else if (texto.equals("DEL")) {
            apagarUltimoNumero();
        } else if (texto.equals("=")) {
            finalizarConta();
        } else if (ehOperador(texto)) {
            escolherOperador(texto);
        } else {
            digitarNumero(texto);
        }
    }

    private void digitarNumero(String texto) {
        // Dois pontos no mesmo número quebram o parseDouble. Melhor cortar o problema na entrada.
        if (texto.equals(".") && visor.getText().contains(".") && !limparVisor) {
            return;
        }

        if (visor.getText().equals("0") || limparVisor) {
            if (texto.equals(".")) {
                visor.setText("0.");
            } else {
                visor.setText(texto);
            }
            limparVisor = false;
        } else if (visor.getText().length() < 12) {
            visor.setText(visor.getText() + texto);
        }
    }

    private void escolherOperador(String texto) {
        // Guarda o número que já está no visor e espera o próximo.
        primeiroNumero = lerVisor();
        operador = texto;
        limparVisor = true;
    }

    private void finalizarConta() {
        // Apertar igual sem ter uma conta montada não precisa fazer escândalo.
        if (operador.equals("") || limparVisor) {
            return;
        }

        segundoNumero = lerVisor();

        try {
            // A conta de verdade fica na classe Numero. Aqui só escolho qual operação chamar.
            switch (operador) {
                case "+":
                    resultado = numero.soma(primeiroNumero, segundoNumero);
                    break;
                case "-":
                    resultado = numero.subtracao(primeiroNumero, segundoNumero);
                    break;
                case "×":
                    resultado = numero.multiplicacao(primeiroNumero, segundoNumero);
                    break;
                case "÷":
                    resultado = numero.divisao(primeiroNumero, segundoNumero);
                    break;
                default:
                    return;
            }

            if (planoLiberaAConta()) {
                mostrarResultado();
            } else {
                abrirTelaPremium();
            }
        } catch (ArithmeticException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage());
        }
    }

    private void abrirTelaPremium() {
        // Essa tela segura o resultado até o usuário escolher um plano da demonstração.
        String conta = removeZeroDecimal(primeiroNumero) + " " + operador + " "
                + removeZeroDecimal(segundoNumero) + " = ???";

        telaPremium = new JDialog(this, true);
        telaPremium.setUndecorated(true);
        telaPremium.setSize(780, 470);
        telaPremium.setLocationRelativeTo(this);

        Color fundoPremium = new Color(22, 22, 26);
        Color fundoCard = new Color(32, 33, 38);

        JPanel painel = new JPanel(new BorderLayout(0, 20));
        painel.setBackground(fundoPremium);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 75, 80), 1),
                BorderFactory.createEmptyBorder(24, 26, 18, 26)));

        JPanel topo = new JPanel(new BorderLayout(20, 0));
        topo.setBackground(fundoPremium);

        JPanel titulos = new JPanel(new GridLayout(3, 1));
        titulos.setBackground(fundoPremium);

        JLabel selo = new JLabel("CALCULADORA PREMIUM");
        selo.setForeground(laranja);
        selo.setFont(new Font("Arial", Font.BOLD, 11));

        JLabel titulo = new JLabel("Seu resultado está pronto.");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitulo = new JLabel("Escolha um plano para desbloquear esta conta.");
        subtitulo.setForeground(new Color(160, 160, 165));
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        titulos.add(selo);
        titulos.add(titulo);
        titulos.add(subtitulo);

        JLabel contaBloqueada = new JLabel(conta, SwingConstants.CENTER);
        contaBloqueada.setForeground(Color.WHITE);
        contaBloqueada.setBackground(cinza);
        contaBloqueada.setOpaque(true);
        contaBloqueada.setFont(new Font("Monospaced", Font.BOLD, 18));
        contaBloqueada.setPreferredSize(new Dimension(180, 70));
        contaBloqueada.setBorder(BorderFactory.createLineBorder(new Color(85, 85, 90)));

        topo.add(titulos, BorderLayout.CENTER);
        topo.add(contaBloqueada, BorderLayout.EAST);

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setBackground(fundoPremium);
        cards.add(criarCardPlano("O BARATINHO", "Conta Básica", "R$ 4,90 / mês",
                "Adição e subtração", 1, fundoCard, false));
        cards.add(criarCardPlano("MAIS ESCOLHIDO", "Quatro Operações", "R$ 8,90 / mês",
                "+  -  ×  ÷", 2, fundoCard, true));
        cards.add(criarCardPlano("MAIS RECURSOS", "Científica", "R$ 14,90 / mês",
                "Básicas + modo científico", 3, fundoCard, false));

        avisoPremium = new JLabel("Assinatura apenas para a demonstração. Ninguém será cobrado.");
        avisoPremium.setForeground(new Color(145, 145, 150));
        avisoPremium.setFont(new Font("Arial", Font.PLAIN, 11));

        JButton agoraNao = new JButton("Agora não");
        agoraNao.setForeground(new Color(180, 180, 185));
        agoraNao.setBackground(fundoPremium);
        agoraNao.setFocusable(false);
        agoraNao.setBorderPainted(false);
        agoraNao.addActionListener(e -> telaPremium.dispose());

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(fundoPremium);
        rodape.add(avisoPremium, BorderLayout.WEST);
        rodape.add(agoraNao, BorderLayout.EAST);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(cards, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        telaPremium.add(painel);
        telaPremium.setVisible(true);
    }

    private JPanel criarCardPlano(String selo, String nome, String preco, String recursos,
            int nivel, Color fundoCard, boolean destaque) {
        // Cada plano é um card. O destaque fica mais chamativo para parecer "mais escolhido".
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(destaque ? new Color(47, 40, 29) : fundoCard);

        Color corDaBorda = destaque ? laranja : new Color(68, 69, 75);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(corDaBorda, destaque ? 2 : 1),
                BorderFactory.createEmptyBorder(16, 16, 14, 16)));

        JPanel textos = new JPanel(new GridLayout(4, 1, 0, 4));
        textos.setBackground(card.getBackground());

        JLabel textoSelo = new JLabel(selo);
        textoSelo.setForeground(laranja);
        textoSelo.setFont(new Font("Arial", Font.BOLD, 9));

        JLabel textoNome = new JLabel(nome);
        textoNome.setForeground(Color.WHITE);
        textoNome.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel textoPreco = new JLabel(preco);
        textoPreco.setForeground(Color.WHITE);
        textoPreco.setFont(new Font("Arial", Font.BOLD, 19));

        JLabel textoRecursos = new JLabel(recursos);
        textoRecursos.setForeground(new Color(165, 165, 170));
        textoRecursos.setFont(new Font("Arial", Font.PLAIN, 12));

        textos.add(textoSelo);
        textos.add(textoNome);
        textos.add(textoPreco);
        textos.add(textoRecursos);

        JButton assinar = new JButton("ASSINAR");
        assinar.setForeground(Color.WHITE);
        assinar.setBackground(destaque ? laranja : cinza);
        assinar.setFont(new Font("Arial", Font.BOLD, 12));
        assinar.setFocusable(false);
        assinar.setBorderPainted(false);
        assinar.setPreferredSize(new Dimension(0, 40));
        assinar.addActionListener(e -> assinarPlano(nivel));

        card.add(textos, BorderLayout.CENTER);
        card.add(assinar, BorderLayout.SOUTH);
        return card;
    }

    private void assinarPlano(int nivel) {
        // O plano barato não faz milagre: multiplicação e divisão ficam para o plano maior.
        if (nivel == 1 && (operador.equals("×") || operador.equals("÷"))) {
            avisoPremium.setForeground(new Color(255, 105, 105));
            avisoPremium.setText("Esse plano não libera esta operação. Escolha outro.");
            return;
        }

        plano = nivel;
        telaPremium.dispose();
        mostrarResultado();
    }

    private boolean planoLiberaAConta() {
        // Regra simples dos planos. Se complicar muito isso aqui, vira boleto de verdade.
        if (plano >= 2) {
            return true;
        }

        if (plano == 1 && (operador.equals("+") || operador.equals("-"))) {
            return true;
        }

        return false;
    }

    private void mostrarResultado() {
        // Mostra o resultado e deixa a calculadora pronta para a próxima conta.
        visor.setText(removeZeroDecimal(resultado));
        operador = "";
        limparVisor = true;
    }

    private void trocarSinal() {
        double valor = lerVisor();
        visor.setText(removeZeroDecimal(valor * -1));
    }

    private void calcularPorcentagem() {
        double valor = lerVisor();
        visor.setText(removeZeroDecimal(valor / 100));
    }

    private void apagarUltimoNumero() {
        // DEL vai comendo número por número. Se sobrar nada, volta para zero.
        String texto = visor.getText();

        if (texto.length() <= 1 || (texto.startsWith("-") && texto.length() == 2)) {
            visor.setText("0");
        } else {
            visor.setText(texto.substring(0, texto.length() - 1));
        }
    }

    private void limparTudo() {
        // AC é o botão do arrependimento: zera tudo e começa de novo.
        primeiroNumero = 0;
        segundoNumero = 0;
        resultado = 0;
        operador = "";
        limparVisor = false;
        visor.setText("0");
    }

    private boolean ehOperador(String texto) {
        return texto.equals("+") || texto.equals("-")
                || texto.equals("×") || texto.equals("÷");
    }

    private double lerVisor() {
        return Double.parseDouble(visor.getText());
    }

    private String removeZeroDecimal(double valor) {
        // 5.0 fica feio no visor. Se for inteiro, mostro só 5.
        if (valor == (long) valor) {
            return String.valueOf((long) valor);
        }

        return String.valueOf(valor);
    }

    public static void main(String[] args) {
        CalculadoraInterface calculadora = new CalculadoraInterface();
        calculadora.setVisible(true);
    }
}
