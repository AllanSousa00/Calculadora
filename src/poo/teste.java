package poo;

import java.util.HashMap;
import java.util.Scanner;


public class teste {
    public static void main(String[] args) {
        
        Numero n = new Numero();

        boolean continuar = true;

        Scanner scan = new Scanner(System.in);

        while (continuar) {
            double x = 0 ;
            double y = 0;
            int operacao = 0;

            System.out.println("Digite a operação que deseja :"
            +                     " \n 1 para soma "
            +                     " \n 2 para subtração "
            +                     " \n 3 para multiplicação "
            +                     " \n 4 para divisão "
            +                     " \n 0 para finalizar a aplicação"
            );
            operacao = scan.nextInt();

            if (operacao == 0){
                continuar = false;
                break;

            }
            if (operacao < 0 || operacao > 4){
                System.out.println("Operação inválida, tente novamente.");
                continue;
            }

            System.out.println("Digite o primeiro número: ");
            x = scan.nextDouble();

            System.out.println("Digite o segundo número: ");
            y = scan.nextDouble();
            
        switch (operacao) {
            case 1:
                imprimecalculadora(operacao, n.soma(x, y), x, y);
                
                break;
            
            case 2:
                imprimecalculadora(operacao, n.subtracao(x, y), x, y);
                break;

            case 3:
                imprimecalculadora(operacao, n.multiplicacao(x, y), x, y);
                break;
            
            case 4:
                try {
                    imprimecalculadora(operacao, n.divisao(x, y), x, y);
                } catch (ArithmeticException e) {
                    System.out.println("Erro: " + e.getMessage() + " Tente outro valor.");
                }
                break;
                default:
                System.out.println("Operação inválida, tente novamente.");
                break;

            }
        }
    scan.close();


}
    static void imprimecalculadora( int operacao, double resultado, double x, double y){

        HashMap< Integer, String >  mapoperacao =new HashMap<>();

        mapoperacao.put(1, "soma");

        mapoperacao.put(2, "subtração");

        mapoperacao.put(3, "multiplicação");

        mapoperacao.put(4, "divisão");

        System.out.println("O resultado da " + mapoperacao.get(operacao) + " entre " + x + " e " + y + " é: " + resultado);





        

    }


}
