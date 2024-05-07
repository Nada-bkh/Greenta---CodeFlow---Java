package greenta.models;

import services.ServiceJob;

public class Main {
    public static void main(String[] args) {
        System.out.println(new ServiceJob().getAll());
    }
}
