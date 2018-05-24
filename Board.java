import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

class Board {
    private ArrayList<Room> realBoard;
//    index:, name:
//    0, Train Station
//    1, Secret Hideout
//    2, Church
//    3, Hotel
//    4, Main Street
//    5, Jail
//    6, General Store
//    7, Ranch
//    8, Bank
//    9, Saloon
//    10, Trailers
//    11, Casting Office

    public Board() throws ParserConfigurationException {
        this.realBoard = new ArrayList<Room>();
        initialize();
    }

    public void reset() {//reset all cards to original state
        for (int i = 0; i < this.realBoard.size(); i++) {
            Room currRoom = this.realBoard.get(i);
            currRoom.reset();
        }
    }

    public Room getRoom(int location) {//return room at location specified on ArrayList
        return this.realBoard.get(location);
    }

    public void initialize() throws ParserConfigurationException {
        Document curr = getDocFromFile("board.xml");
        getBoardData(curr);
    }

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

    private void getBoardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList sets = root.getElementsByTagName("set");
        for (int i = 0; i < sets.getLength(); i++) {
            Node set = sets.item(i);
            String setName = set.getAttributes().getNamedItem("name").getNodeValue();
            //System.out.println("Name:"+setName);
            Set currSet = new Set();
            this.realBoard.add(currSet);
            currSet.ChangeRoom(setName);
            NodeList children = set.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);
                if ("neighbors".equals(sub.getNodeName())) {
                    ArrayList<String> currNeighbors = new ArrayList<String>();
                    NodeList neighbors = sub.getChildNodes();
                    for (int k = 0; k < neighbors.getLength(); k++) {
                        Node subNeighbor = neighbors.item(k);
                        if ("neighbor".equals(subNeighbor.getNodeName())) {
                            String neighborName = subNeighbor.getAttributes().getNamedItem("name").getNodeValue();
                            currNeighbors.add(neighborName);
                            //System.out.println("Neighbor:"+neighborName);
                        }
                    }
                    currSet.addAdj(currNeighbors);
                } else if ("takes".equals(sub.getNodeName())) {
                    NodeList takes = sub.getChildNodes();
                    int toReturn = 0;
                    for (int q = 0; q < takes.getLength(); q++) {
                        Node firstTake = takes.item(q);
                        if ("take".equals(firstTake.getNodeName())) {
                            toReturn++;
                        }
                    }
                    currSet.setTakes(toReturn);
                } else if ("parts".equals(sub.getNodeName())) {
                    NodeList parts = sub.getChildNodes();
                    ArrayList<PlayerSpot> currSpotList = new ArrayList<PlayerSpot>(1);
                    for (int x = 0; x < parts.getLength(); x++) {
                        Node currSpot = parts.item(x);
                        //   PlayerSpot currPS=new PlayerSpot();
                        //   currSpotList.add(currPS);
                        if ("part".equals(currSpot.getNodeName())) {
                            PlayerSpot currPS = new PlayerSpot();
                            currSpotList.add(currPS);
                            String partName = currSpot.getAttributes().getNamedItem("name").getNodeValue();
                            String Level = currSpot.getAttributes().getNamedItem("level").getNodeValue();
                            //System.out.println("PartName:"+partName+" requiredLevel:"+ Level);
                            int level = Integer.parseInt(Level);
                            currPS.AddToPlayerSpot(level, partName);
                            NodeList currSpotSub = currSpot.getChildNodes();
                            for (int y = 0; y < currSpotSub.getLength(); y++) {
                                Node CurrSpotSubNode = currSpotSub.item(y);
                                if ("line".equals(CurrSpotSubNode.getNodeName())) {
                                    String line = CurrSpotSubNode.getTextContent();
                                    currPS.AddToPlayerSpot(line);
                                    //System.out.println("Line:" +line);
                                }
                            }
                        }
                    }
                    currSet.addPlayerSpot(currSpotList);

                }
            }

        }
        NodeList Trailers = root.getElementsByTagName("trailer");
        Room trailer = new Room();
        trailer.ChangeRoom("trailer");
        this.realBoard.add(trailer);
        for (int i = 0; i < Trailers.getLength(); i++) {
            Node Trailer = Trailers.item(i);
            NodeList Tchildren = Trailer.getChildNodes();
            for (int j = 0; j < Tchildren.getLength(); j++) {
                Node Tsub = Tchildren.item(j);
                if ("neighbors".equals(Tsub.getNodeName())) {
                    ArrayList<String> TcurrNeighbors = new ArrayList<String>();
                    //System.out.println("Trailer:");
                    NodeList Tneighbors = Tsub.getChildNodes();
                    for (int k = 0; k < Tneighbors.getLength(); k++) {
                        Node TsubNeighbor = Tneighbors.item(k);
                        if ("neighbor".equals(TsubNeighbor.getNodeName())) {
                            String TneighborName = TsubNeighbor.getAttributes().getNamedItem("name").getNodeValue();
                            TcurrNeighbors.add(TneighborName);
                            //System.out.println("Neighbor:"+TneighborName);
                        }
                    }
                    trailer.addAdj(TcurrNeighbors);
                }
            }
        }
        NodeList Casting = root.getElementsByTagName("office");
        Room office = new Room();
        office.ChangeRoom("office");
        this.realBoard.add(office);
        for (int i = 0; i < Casting.getLength(); i++) {
            Node Office = Casting.item(i);
            NodeList Ochildren = Office.getChildNodes();
            for (int j = 0; j < Ochildren.getLength(); j++) {
                Node Osub = Ochildren.item(j);
                if ("neighbors".equals(Osub.getNodeName())) {
                    ArrayList<String> OcurrNeighbors = new ArrayList<String>();
                    //System.out.println("Office:");
                    NodeList Oneighbors = Osub.getChildNodes();
                    for (int k = 0; k < Oneighbors.getLength(); k++) {
                        Node OsubNeighbor = Oneighbors.item(k);
                        if ("neighbor".equals(OsubNeighbor.getNodeName())) {
                            String OneighborName = OsubNeighbor.getAttributes().getNamedItem("name").getNodeValue();
                            OcurrNeighbors.add(OneighborName);
                            //System.out.println("Neighbor:"+OneighborName);
                        }
                    }
                    office.addAdj(OcurrNeighbors);
                }
            }
        }
    }
}