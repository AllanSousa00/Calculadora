public class Numero {

    // Soma normal. Essa aqui pelo menos não inventa moda.
    public double soma(double x, double y) {
        return x + y;
    }

    // Subtrai na ordem certa: primeiro número menos segundo número.
    public double subtracao(double x, double y) {
        return x - y;
    }

    // Multiplicação direta, sem segredo.
    public double multiplicacao(double x, double y) {
        return x * y;
    }

    // Dividir por zero não dá. A matemática já mandou o recado faz tempo.
    public double divisao(double x, double y) {
        if (y == 0) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }
        return x / y;
    }
}
