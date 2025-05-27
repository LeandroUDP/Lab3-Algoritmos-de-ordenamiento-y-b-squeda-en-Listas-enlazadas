import java.util.*;

public class Dataset {
    private ArrayList<Game> data;
    private String sortedByAttribute;

    public Dataset(ArrayList<Game> data) {
        this.data = data;
        this.sortedByAttribute = " ";
    }

    public ArrayList<Game> getGamesByPrice(int price) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("price".equals(sortedByAttribute)) {
            int iz = 0, de = data.size() - 1;
            while (iz <= de) {
                int med = (iz + de) / 2;
                int medprice = data.get(med).getPrice();
                if (medprice <= price) {
                    int i = med;
                    while (i >= 0 && data.get(i).getPrice() == price) {
                        resultado.add(0, data.get(i--));
                    }
                    i = med + 1;
                    while (i < data.size() && data.get(i).getPrice() == price) {
                        resultado.add(data.get(i++));
                    }
                    break;
                } else if (medprice < price) {
                    iz = med + 1;
                } else {
                    de = med - 1;
                }
            }
        } else {
            for (Game game : data) {
                if (game.getPrice() == price) {
                    resultado.add(game);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Game> getGamesByPriceRange(int inferior, int superior) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("price".equals(sortedByAttribute)) {
            for (Game game : data) {
                if (game.getPrice() >= inferior && game.getPrice() <= superior) {
                    resultado.add(game);
                } else if (game.getPrice() > superior) {
                    break;
                }
            }
        } else {
            for (Game game : data) {
                if (game.getPrice() >= inferior && game.getPrice() <= superior) {
                    resultado.add(game);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Game> getGamesByCategory(String category) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("category".equals(sortedByAttribute)) {
            for (Game game : data) {
                if (game.getCategory().equals(category)) {
                    resultado.add(game);
                } else if (game.getCategory().compareTo(category) > 0) {
                    break;
                }
            }
        } else {
            for (Game game : data) {
                if (game.getCategory().equals(category)) {
                    resultado.add(game);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Game> getGamesByQuality(int quality) {
        ArrayList<Game> resultado = new ArrayList<>();
        if ("quality".equals(sortedByAttribute)) {
            for (Game game : data) {
                if (game.getQuality() == quality) {
                    resultado.add(game);
                } else if (game.getQuality() > quality) {
                    break;
                }
            }
        }
        return resultado;
    }

    private Comparator<Game> getComparator(String attribute) {
        if (attribute.equals("category")) {
            return Comparator.comparing(Game::getCategory);
        } else if (attribute.equals("quality")) {
            return Comparator.comparingInt(Game::getQuality);
        } else {
            return Comparator.comparingInt(Game::getPrice);
        }
    }

    private void bubbleSort(Comparator<Game> c) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 0; j < data.size() - i - 1; j++) {
                if (c.compare(data.get(j), data.get(j + 1)) > 0) {
                    Collections.swap(data, j, j + 1);
                }
            }
        }
    }

    private void insertionSort(Comparator<Game> c) {
        for (int i = 1; i < data.size(); i++) {
            Game actual = data.get(i);
            int j = i - 1;
            while (j >= 0 && c.compare(data.get(j), actual) > 0) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, actual);
        }
    }

    private void selectionSort(Comparator<Game> c) {
        for (int i = 0; i < data.size() - 1; i++) {
            int aux = i;
            for (int j = i + 1; j < data.size(); j++) {
                if (c.compare(data.get(j), data.get(aux)) < 0) {
                    aux = j;
                }
            }
            Collections.swap(data, i, aux);
        }
    }

    private ArrayList<Game> merge(ArrayList<Game> izq, ArrayList<Game> der, Comparator<Game> c) {
        ArrayList<Game> resultado = new ArrayList<>();
        int i = 0, j = 0;
        while (i < izq.size() && j < der.size()) {
            if (c.compare(izq.get(i), der.get(j)) <= 0) {
                resultado.add(izq.get(i++));
            } else {
                resultado.add(der.get(j++));
            }
        }
        while (i < izq.size()) {
            resultado.add(izq.get(i++));
        }
        while (j < der.size()) {
            resultado.add(der.get(j++));
        }
        return resultado;
    }

    private ArrayList<Game> mergeSort(ArrayList<Game> lista, Comparator<Game> c) {
        if (lista.size() <= 1) {
            return lista;
        }
        int medio = lista.size() / 2;
        ArrayList<Game> izq = mergeSort(new ArrayList<>(lista.subList(0, medio)), c);
        ArrayList<Game> der = mergeSort(new ArrayList<>(lista.subList(medio, lista.size())), c);
        return merge(izq, der, c);
    }

    private int partition(int bajo, int alto, Comparator<Game> c) {
        Game aux = data.get(alto);
        int j = bajo - 1;
        for (int i = bajo; i < alto; i++) {
            if (c.compare(data.get(i), aux) <= 0) {
                j++;
                Collections.swap(data, j, i);
            }
        }
        Collections.swap(data, j + 1, alto);
        return j + 1;
    }

    private void quickSort(int bajo, int alto, Comparator<Game> c) {
        if (bajo < alto) {
            quickSort(bajo, partition(bajo, alto, c) - 1, c);
            quickSort(partition(bajo, alto, c) + 1, alto, c);
        }
    }

    public ArrayList<Game> sortByAlgorithm(String algorithm, String attribute) {
        Comparator<Game> comparator = getComparator(attribute);
        if (algorithm.equals("bubbleSort")) {
            bubbleSort(comparator);
        } else if (algorithm.equals("selectionSort")) {
            selectionSort(comparator);
        } else if (algorithm.equals("insertionSort")) {
            insertionSort(comparator);
        } else if (algorithm.equals("mergeSort")) {
            data = mergeSort(data, comparator);
        } else if (algorithm.equals("quickSort")) {
            quickSort(0, data.size() - 1, comparator);
        } else {
            Collections.sort(data, comparator);
        }
        sortedByAttribute = attribute;
        return data;
    }

    public void countingSortByQuality() {
        int maxQuality = 100;
        ArrayList<ArrayList<Game>> contadores = new ArrayList<>(maxQuality + 1);

        // Inicializar listas vacías
        for (int i = 0; i <= maxQuality; i++) {
            contadores.add(new ArrayList<>());
        }

        // Clasificar juegos por calidad
        for (Game juego : data) {
            contadores.get(juego.getQuality()).add(juego);
        }

        // Reconstruir la lista ordenada
        data.clear();
        for (int i = 0; i <= maxQuality; i++) {
            data.addAll(contadores.get(i));
        }

        sortedByAttribute = "quality";
    }
}