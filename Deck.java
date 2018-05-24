import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

class Deck {
    private ArrayDeque<SceneCard> order;
    private ArrayList<SceneCard> list;
    private ArrayList<SceneCard> activeCard;

    public Deck() throws ParserConfigurationException {
        this.order = new ArrayDeque<SceneCard>();
        this.list = new ArrayList<SceneCard>();
        this.activeCard = new ArrayList<SceneCard>();
        initialize();
        shuffle();
    }

    //Returns the active cards
    public ArrayList<SceneCard> getActiveCard() {
        return this.activeCard;
    }

    private void shuffle() {//randomly shuffle order of cards, resets each card in deck
        for (int j = 0; j < activeCard.size(); j++) {
            SceneCard curr = activeCard.get(j);
            curr.resetCard();
        }
        ArrayList<SceneCard> temp = new ArrayList<SceneCard>(this.list.size());
        while (order.isEmpty() != true) {
            temp.add(order.poll());
        }
        while (temp.isEmpty() != true) {
            int i = RandomNum(0, temp.size());
            order.addFirst(temp.get(i));
            temp.remove(i);
        }

    }

    private void addCard(SceneCard card) {//adds card to deck
        this.order.addFirst(card);
        this.list.add(card);
    }

    public void reset() {//iterates through deck and resets each card
        while (!this.activeCard.isEmpty()) {
            SceneCard curr = this.activeCard.get(0);
            curr.deActive();
            this.activeCard.remove(0);
        }
        shuffle();
    }

    public SceneCard getTopofOrder() {//return card at top of queue, place at bottom
        SceneCard curr = order.getFirst();
        curr.activate();
        order.addLast(curr);
        activeCard.add(curr);
        return order.poll();
    }

    private void initialize() throws ParserConfigurationException {
        Document curr = getDocFromFile("cards.xml");
        getDeckData(curr);
    }

    //RandomNum:
    //in: int a int b
    //out: random int in [a,b]
    //notes: helper function to generate random numbers.
    private int RandomNum(int a, int b) {
        Random rand = new Random();
        int n = rand.nextInt(b) + a;
        return n;
    }//end RandomNum

    // returns a Document object after loading the book.xml file.
    private Document getDocFromFile(String filename)
            throws ParserConfigurationException {
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            try {
                doc = db.parse(filename);
            } catch (Exception ex) {
                System.out.println("XML parse failure");
                ex.printStackTrace();
            }
            return doc;
        } // exception handling
    }

    private void getDeckData(Document d) {
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        for (int i = 0; i < cards.getLength(); i++) {
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            String cardBudget = card.getAttributes().getNamedItem("budget").getNodeValue();
            String cardPic=card.getAttributes().getNamedItem("img").getNodeValue();
            int cardCost = Integer.parseInt(cardBudget);
            //System.out.println("Name: "+cardName+" Budget: "+cardBudget+ " Pic"+cardPic);
            SceneCard current = new SceneCard(cardCost, cardName);
            current.AddPicName(cardPic);
            addCard(current);
            NodeList children = card.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if ("scene".equals(sub.getNodeName())) {
                    String sceneNum = sub.getAttributes().getNamedItem("number").getNodeValue();
                    int sceneN = Integer.parseInt(sceneNum);
                    //System.out.println("Number: "+sceneN);
                    current.addNum(sceneN);
                    String description = sub.getTextContent();
                    //System.out.println("Description: "+description);
                    current.addDesc(description);
                }
                if ("part".equals(sub.getNodeName())) {
                    PlayerSpot currSpot = new PlayerSpot();
                    current.addPSpot(currSpot);
                    String title = sub.getAttributes().getNamedItem("name").getNodeValue();
                    String rank = sub.getAttributes().getNamedItem("level").getNodeValue();
                    //System.out.println("Name: "+title +" Level: "+rank);
                    int IRank = Integer.parseInt(rank);
                    currSpot.AddToPlayerSpot(IRank, title);
                    NodeList subSub = sub.getChildNodes();
                    for (int k = 0; k < subSub.getLength(); k++) {
                        Node currSubSub = subSub.item(k);
                        if ("line".equals(currSubSub.getNodeName())) {
                            String line = sub.getTextContent();
                            currSpot.AddToPlayerSpot(line);
                            //System.out.println("Line: "+line);
                        }
                    }
                }
            }
        }
    }
}