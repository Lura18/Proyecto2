package Algoritmo;

import java.io.*;
import java.util.*;

/**
 * ProblemaP2.java
 * Solución propuesta: dos grafos (fibra/coax), BFS tras cada arista, comparación de componentes por pares O(N^2).
 *
 * Autores: 
 * - Laura Sofia Sarmiento (202113056)
 * - Laura Fonseca ()
 * 
 * Curso: ISIS 1105 - Diseño y Análisis de Algoritmos
 * Semestre: 2025-20
 */
public class ProblemaP2_BFS {

    static class lecturaEntradas {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0;
        private int len = 0;

        lecturaEntradas(InputStream is) {
            in = is;
        }

        private int leer() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                	return -1;
                }
            }
            return buffer[ptr++];
        }

        public String next() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;

            while (true) {
                c = leer();
                if (c == -1) {
                	return null;
                }
                if (!Character.isWhitespace(c)) {
                	break;
                }
            }

            while (c != -1 && !Character.isWhitespace(c)) {
                sb.append((char) c);
                c = leer();
            }
            return sb.toString();
        }

        int nextInt() throws IOException {
            String s = next();
            if (s == null) {
            	return Integer.MIN_VALUE;
            } else {
            	return Integer.parseInt(s);
            }
        }
    }

    public static void main(String[] args) throws Exception {
    	lecturaEntradas entrada = new lecturaEntradas(System.in);
        String toStr = entrada.next();
        if (toStr == null) {
        	return;
        }
        int tot = Integer.parseInt(toStr);

        StringBuilder globalOut = new StringBuilder();

        for (int x = 0; x < tot; x++) { // T es el total de casos de prueba
            int N = entrada.nextInt();
            int M = entrada.nextInt();

            // Dos grafos no dirigidos: 1=fibra, 2=coax
            List<Integer>[] fibra = new ArrayList[N + 1];
            List<Integer>[] coax  = new ArrayList[N + 1];
            
            for (int i = 1; i <= N; i++) {
                fibra[i] = new ArrayList<>();
                coax[i]  = new ArrayList<>();
            }

            // Para construir la línea de salida del caso
            StringBuilder line = new StringBuilder();

            for (int step = 0; step < M; step++) {
                int i = entrada.nextInt();
                int j = entrada.nextInt();
                int k = entrada.nextInt();

                if (k == 1) {
                    // fibra
                    fibra[i].add(j);
                    fibra[j].add(i);
                } else {
                    // coax
                    coax[i].add(j);
                    coax[j].add(i);
                }

                int[] compFibra = construirComponentes(fibra, N);
                int[] compCoax  = construirComponentes(coax,  N);

                boolean redundante = verRedundancia(compFibra, compCoax, N);

                if (step > 0) {
                	line.append(' ');
                }
                
                if (redundante) {
                	line.append('1');
                } else {
                	line.append('0');
                }
            }

            // Imprimir línea del caso
            globalOut.append(line).append('\n');
        }

        System.out.print(globalOut.toString());
    }

    // BFS: comp[u] = id de componente de u
    static int[] construirComponentes(List<Integer>[] G, int N) {
        int[] comp = new int[N + 1];
        Arrays.fill(comp, -1); //inicialización
        int id = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>();

        for (int u = 1; u <= N; u++) {
            if (comp[u] != -1) {
            	continue;
            }
            comp[u] = id;
            q.clear();
            q.add(u);
            while (!q.isEmpty()) {
                int x = q.poll();
                for (int y : G[x]) {
                    if (comp[y] == -1) {
                        comp[y] = id;
                        q.add(y);
                    }
                }
            }
            id++;
        }
        return comp;
    }

    // La red es redundante si (u y v están juntos en fibra) && (u y v están juntos en coax), para todo par.
    // creo que se puede optmizar
    static boolean verRedundancia(int[] compF, int[] compC, int N) {
        for (int u = 1; u <= N; u++) {
            for (int v = u + 1; v <= N; v++) {
                if ((compF[u] == compF[v]) != (compC[u] == compC[v])) {
                	return false;
                }
            }
        }
        return true;
    }
}
