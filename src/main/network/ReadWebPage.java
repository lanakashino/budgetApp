package network;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ReadWebPage {
    BufferedReader br;
    JSONParser parser;

    public ReadWebPage() {
        br = null;
        parser = new JSONParser();
    }

    //EFFECTS: tries generate quotes, then closes Buffer reader
    public void printQuote() throws IOException {
        try {
            generateQuote();
        } catch (Exception e) {
            System.out.println("Quote retrieval error: Try start up again");
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void printQuote(JPanel panel) throws IOException {
        try {
            generateQuote(panel);
        } catch (Exception e) {
            System.out.println("Quote retrieval error: Try start up again");
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    // general code taken and adapted from https://carlofontanos.com/java-parsing-json-data-from-a-url/
    //EFFECTS: prints quote from API
    private void generateQuote() throws IOException, ParseException {
        int rand = (int) (Math.random() * 152 + 1);
        String theURL = "http://www.nokeynoshade.party/api/queens/" + String.valueOf(rand); //this can point to any URL
        URL url = new URL(theURL);
        br = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            JSONObject object = (JSONObject) parser.parse(inputLine);
            String name = (String) object.get("name");
            String quote = (String) object.get("quote");
            System.out.println("\"" + quote + "\"");
            for (int i = 0; i < quote.length() - 5; i++) {
                System.out.print(" ");
            }
            System.out.println("-" + name);
            System.out.println("\n");
        }
    }

    // general code taken and adapted from https://carlofontanos.com/java-parsing-json-data-from-a-url/
    public void generateQuote(JPanel panel) throws IOException, ParseException {
        int rand = (int) (Math.random() * 152 + 1);
        String theURL = "http://www.nokeynoshade.party/api/queens/" + String.valueOf(rand); //this can point to any URL
        URL url = new URL(theURL);
        br = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            parseAndPrint(panel, inputLine);
        }
    }

    private void parseAndPrint(JPanel panel, String inputLine) throws ParseException, IOException {
        JSONObject object = (JSONObject) parser.parse(inputLine);
        String name = (String) object.get("name");
        String quote = (String) object.get("quote");
        Label quoteLabel = new Label("\"" + quote + "\"");
        quoteLabel.setBounds(330 - quote.length() * 3, 80, 600, 30);
        Label queenLabel = new Label("-" + name);
        queenLabel.setBounds(330 + quote.length() * 2, 100, 130, 30);

        String imageUrl = (String) object.get("image_url");
        BufferedImage img = ImageIO.read(new URL(imageUrl));
        BufferedImage imgResized = resize(img, 200,300);
        JLabel queenImage = new JLabel(new ImageIcon(imgResized));
        queenImage.setBounds(250,150, 200, 300);

        panel.add(quoteLabel);
        panel.add(queenLabel);
        panel.add(queenImage);
    }

    //from Stack Overflow: https://stackoverflow.com/questions/9417356/bufferedimage-resize
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}