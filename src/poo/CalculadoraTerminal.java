package poo;

import java.util.Scanner;

public class CalculadoraTerminal {

    public static void main(String[] args) {
        Numero numero = new Numero();
        Scanner scan = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("Digite a operação desejada:");
            System.out.println("1 - Soma");
            System.out.println("2 - Subtração");
            System.out.println("3 - Multiplicação");
            System.out.println("4 - Divisão");
            System.out.println("0 - Sair");

            int operacao = scan.nextInt();

            if (operacao == 0) {
                continuar = false;
                continue;
            }

            if (operacao < 1 || operacao > 4) {
                System.out.println("Operação inválida.");
                continue;
            }

            System.out.println("Digite o primeiro número:");
            double x = scan.nextDouble();

            System.out.println("Digite o segundo número:");
            double y = scan.nextDouble();

            try {
                double resultado = calcular(numero, operacao, x, y);
                System.out.println("Resultado: " + resultado);
            } catch (ArithmeticException erro) {
                System.out.println("Erro: " + erro.getMessage());
            }
        }

        scan.close();
    }

    private static double calcular(Numero numero, int operacao, double x, double y) {
        switch (operacao) {
            case 1:
                return numero.soma(x, y);
            case 2:
                return numero.subtracao(x, y);
            case 3:
                return numero.multiplicacao(x, y);
            case 4:
                return numero.divisao(x, y);
            default:
                return 0;
        }
    }
}
