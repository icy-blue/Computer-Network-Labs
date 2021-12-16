package Exp101;

public class Main {
    public static void main(String[] args) {
        String data = "icy2000@mail.sdu.edu.cn";
        String afterApplicationLayer = new ApplicationLayer().addHeader(data);
        String afterPresentationLayer = new PresentationLayer().addHeader(afterApplicationLayer);
        String afterSessionLayer = new SessionLayer().addHeader(afterPresentationLayer);
        String afterTransportationLayer = new TransportationLayer().addHeader(afterSessionLayer);
        String afterNetworkLayer = new NetworkLayer().addHeader(afterTransportationLayer);
        String afterDataLinkLayer = new DataLinkLayer().addHeader(afterNetworkLayer);
        String afterPhysicalLayer = new PhysicalLayer().addHeader(afterDataLinkLayer);
        System.out.println(data);
        System.out.println(afterApplicationLayer);
        System.out.println(afterPresentationLayer);
        System.out.println(afterSessionLayer);
        System.out.println(afterTransportationLayer);
        System.out.println(afterNetworkLayer);
        System.out.println(afterDataLinkLayer);
        System.out.println(afterPhysicalLayer);
    }
}