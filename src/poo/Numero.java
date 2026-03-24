package poo;

public class Numero {

    private double x;

    private double getX() {
        return x;
    }

    private void setX(double x) {
        this.x = x;
    }

    public double soma(double x, double y) {
        setX(x + y);
        return getX();
    }

    public double subtracao(double x, double y) {
        setX(x - y);
        return getX();
    }

    public double multiplicacao(double x, double y) {
        setX(x * y);
        return getX();
    }

    public double divisao(double x, double y) {
        if (y == 0) {
            throw new ArithmeticException("Divisão por zero não é permitida.");
        }
        setX(x / y);
        return getX();
    }
}
