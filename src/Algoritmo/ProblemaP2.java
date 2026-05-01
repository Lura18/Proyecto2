package Algoritmo;

import java.io.*;

import java.util.*;

/**
 * ProblemaP2.java
 * Solución con Union-Find
 *
 * Soolución propuesta 2:
 * 
 * Mantener dos UF: uno para fibra (k=1) y otro para coax (k=2). Tras cada conexión, hacer union en el UF adecuado.
 * 
 * Para decidir redundancia, para cada nodo u calcular (findFibra(u), findCoax(u)). La red es redundante si y solo si:
 * Para todo representante rF de fibra hay un único representante rC de coax con el que coinciden exactamente los mismos nodos y simétricamente para rC de coax.
 *
 * Autores: 
 * - Laura Sofia Sarmiento (202113056)
 * - Laura Fonseca (202222197)
 * 
 * Curso: ISIS 1105 - Diseño y Análisis de Algoritmos
 * Semestre: 2025-20
 */
public class ProblemaP2 {

    // Union-Find  
    static class UF {
        int[] parent;
        int[] size;

        UF(int n) {
            parent = new int[n + 1];
            size   = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            int p = parent[x];
            if (p != x) {
            	parent[x] = find(p);
            }
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            
            if (ra == rb) {
            	return;
            }
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    //Misma que en el de BFS
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

            UF ufFibra = new UF(N);
            UF ufCoax  = new UF(N);

            StringBuilder line = new StringBuilder();

            for (int step = 0; step < M; step++) {
                int i = entrada.nextInt();
                int j = entrada.nextInt();
                int k = entrada.nextInt();

                if (k == 1) {
                	ufFibra.union(i, j);
                }
                else {
                	ufCoax.union(i, j);
                }

                boolean redundante = verRedundancia(ufFibra, ufCoax, N);

                if (step > 0) {
                	line.append(' ');
                }
                
                if (redundante) {
                	line.append('1');
                } else {
                	line.append('0');
                }
            }

            globalOut.append(line).append('\n');
        }

        System.out.print(globalOut.toString());
    }

    /**
     * Verifica si las particiones de fibra y coax son idénticas.
     * Para cada nodo u, se toma el par (rF(u), rC(u)). 
     */
    static boolean verRedundancia(UF fibra, UF coax, int N) {
        // rF -> rC y rC -> rF 
        HashMap<Integer, Integer> mapFaC = new HashMap<>();
        HashMap<Integer, Integer> mapCaF = new HashMap<>();

        for (int u = 1; u <= N; u++) {
            int rF = fibra.find(u);
            int rC = coax.find(u);

            Integer prevC = mapFaC.putIfAbsent(rF, rC);
            if (prevC != null && prevC != rC) {
            	return false;
            }

            Integer prevF = mapCaF.putIfAbsent(rC, rF);
            if (prevF != null && prevF != rF) {
            	return false;
            }
        }
        return true;
    }
}