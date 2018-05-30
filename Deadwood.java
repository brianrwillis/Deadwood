import java.util.*;
import javax.xml.parsers.*;

public class Deadwood {
    public Deadwood() {
    }

    public static void main(String[] args) throws ParserConfigurationException {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Welcome to Deadwood. Number of players?");

        //Get number of players
        int playerCnt = -1;
        while (playerCnt == -1) {
            playerCnt = getNumInput(2, 8);
        }

        Players players = new Players(playerCnt);           //Make players
        players.shuffle();                                  //Randomize player order
        Players playersOrdered = players;                   //Store starting order of players for calculating winner later

        //Make model classes
        Deck deck = new Deck();
        Board board = new Board();

        //Make controller classes
        Moderator moderator = new Moderator(players, deck, board);
        Calculator calculator = new Calculator(players);

        //Make interfaces
        PlayerSpotPrinter playerSpotPrinter = new PlayerSpotPrinter();
        Display display = new Display(players, board);

        //Get player names
        String[] playerNames = new String[playerCnt];
        for (int i = 0; i < playerCnt; i++) {
            System.out.println("Player " + (i + 1) + "'s name?");
            playerNames[i] = keyboard.nextLine();
            while (playerNames[i] == null) {
            }                 //Blocking code: wait for user input
        }
        players.addPlayers(playerNames);

        int[] rules = calculator.calcRules(playerCnt);      //Calculate starting parameters
        int daysLeft = rules[2];                            //Set remaining days

        //Set player starting attributes
        ArrayList<Player> playersList = players.getPlayers();
        for (int i = 0; i < playerCnt; i++) {
            moderator.giveFunds(playersList.get(i), 0, rules[1]);
            moderator.increaseRank(playersList.get(i), rules[0] - 1);
        }

        //Game loop
        boolean gameOngoing = true;
        boolean currentDay = true;
        while (gameOngoing) {
            //Distribute cards
            for (int i = 0; i < 10; i++) {
                Room currRoom = board.getRoom(i);
                SceneCard currCard = deck.getTopofOrder();
                currRoom.addCard(currCard);
            }
            //current.displayCards();
            //Day loop
            while (currentDay) {
            
            //delete:
                System.out.println("\r\nPlayer stats:");        //Print out player info each turn
                display.dispPlayers();
                //controller.updatePlayerDisplay();
            
            //delete:
                //Prompt for user action
                System.out.println("\r\n" + players.getCurrent().getName() + "'s turn: Please select action.");
                System.out.println("1: Move\r\n2: Work");

                //Test action validity
                int actionSel = -1;
                while (actionSel == -1) {
                    //actionSel=controller.getMoveChoice();
                    actionSel = getNumInput(1, 2);
                    int currLoc = players.getCurrent().getLocation();
                    Room currentSet = board.getRoom(players.getCurrent().getLocation());
                    SceneCard currCard = currentSet.getCard();
                    if ((actionSel == 2) && ((currLoc == 10) || currLoc == 11)) {
                        //controller.displayTrailerOfficeError();
                        System.out.println("Cannot work while in the Trailers or Office.");
                        actionSel = -1;
                    } else if ((actionSel == 2) && (!currCard.isActive() || !players.getCurrent().onCard())) {
                        //controller.displayNotEnrollError();
                        System.out.println("Cannot work while not enrolled.");
                        actionSel = -1;
                    } else if ((actionSel == 1) && players.getCurrent().onCard()) {
                        //controller.displayCantMoveError();
                        System.out.println("Cannot move while enrolled.");
                        actionSel = -1;
                    }
                }

                switch (actionSel) {
                    case (1):        //Move
                    
                    //delete:
                        //Prompt user move choice
                        System.out.println("Choose move location:");
                        for (int i = 0; i < 12; i++) {
                            System.out.println((i + 1) + ": " + board.getRoom(i).getName());
                        }

                        //Test move validity
                        boolean moveValid = false;
                        int moveSel = -1;
                        while (!moveValid) {
                            moveSel = -1;
                            while (moveSel == -1) {
                                //moveSel=controller.getLocation();
                                moveSel = getNumInput(1, 12);
                            }
                            moveValid = board.getRoom(players.getCurrent().getLocation()).isAdjacent(board.getRoom(moveSel - 1).getName());
                            if (!moveValid) {
                                 //controller.displayLocationError();
                                System.out.println("Choose an adjacent location.");
                            }
                        }
                        players.getCurrent().changeLocation(moveSel - 1);     //Update user's location
                        int roomNum=moveSel-1;
                        Room currToRoom=this.board.getRoom(roomNum);
                        SceneCard currToCard=currToRoom.getCard();
                        if(!currToCard.isFlipped()){
                           currToCard.flip();
                           //controller.flipCard(roomNum);
                           }


                        if (players.getCurrent().getLocation() == 10) {           //User moved to Trailers
                            //Do nothing
                        } else if (players.getCurrent().getLocation() == 11) {    //User moved to Casting Office
                            boolean upgradeSucc = false;
                            while (!upgradeSucc) {
                                //Prompt upgrade amount
                            //delete:
                                System.out.println("Upgrade how many ranks?");

                                //Get upgrade choice
                                int upgradeNumSel = -1;
                                while (upgradeNumSel == -1) {
                                    upgradeNumSel = getNumInput(1, 5);
                                    //upgradeNumSel=controller.getUpgradeChoice();
                                    if ((players.getCurrent().getRank() + upgradeNumSel) > 6) {
                                        //controller.displayRankChoiceError();
                                        System.out.println("You can only upgrade to a max rank of 6.");
                                        upgradeNumSel = -1;
                                    }
                                }
                                int newRank = players.getCurrent().getRank() + upgradeNumSel;
                     
                     //delete:
                                //Prompt upgrade spend type
                                System.out.println("Upgrade with money or fame?");
                                System.out.println("1: Money\r\n2: Fame\r\n3: Don't upgrade");

                                //Test spend choice validity
                                int upgradeSel = -1;
                                while (upgradeSel == -1) {
                                    //upgradeSel=controller.getUpgradeType();
                                    upgradeSel = getNumInput(1, 3);
                                }

                                //Determine costs per rank
                                if (upgradeSel == 1) {    //Money
                                    int moneyCost = 0;
                                    switch (newRank) {
                                        case (2):
                                            moneyCost = 4;
                                            break;
                                        case (3):
                                            moneyCost = 10;
                                            break;
                                        case (4):
                                            moneyCost = 18;
                                            break;
                                        case (5):
                                            moneyCost = 28;
                                            break;
                                        case (6):
                                            moneyCost = 40;
                                            break;
                                        default:
                                            break;
                                    }
                                    if (players.getCurrent().getCredit() < moneyCost) {
                                       //controller.displayMoneyError(1);
                                        System.out.println("Not enough money to perform upgrade.");
                                        upgradeSucc = false;
                                    } else {
                                        //Upgrade
                                        moderator.giveFunds(players.getCurrent(), -1 * moneyCost, 0);
                                        moderator.increaseRank(players.getCurrent(), upgradeSel);
                                        upgradeSucc = true;
                                    }
                                } else if (upgradeSel == 2) {    //Fame
                                    int fameCost = 0;
                                    switch (newRank) {
                                        case (2):
                                            fameCost = 5;
                                            break;
                                        case (3):
                                            fameCost = 10;
                                            break;
                                        case (4):
                                            fameCost = 15;
                                            break;
                                        case (5):
                                            fameCost = 20;
                                            break;
                                        case (6):
                                            fameCost = 25;
                                            break;
                                        default:
                                            break;
                                    }
                                    if (players.getCurrent().getFame() < fameCost) {
                                        //controller.displayMoneyError(2);
                                        System.out.println("Not enough fame to perform upgrade.");
                                        upgradeSucc = false;
                                    } else {
                                        moderator.giveFunds(players.getCurrent(), 0, -1 * fameCost);
                                        moderator.increaseRank(players.getCurrent(), upgradeSel);
                                        upgradeSucc = true;
                                    }
                                } else {
                                    upgradeSucc = true;     //Don't upgrade
                                }
                            }
                        } else {
                            //Prompt user action
                            System.out.println("\r\nRoles:");
                            Room currentSet = board.getRoom(players.getCurrent().getLocation());
                            SceneCard currentCard = currentSet.getCard();
                            ArrayList<PlayerSpot> spotsOn = currentCard.getRanksOnCard();
                            ArrayList<PlayerSpot> spotsOff = currentSet.getRanksOffCard();
                            //controller.displayOptions(spotsOn, spotsOff);

                     //delete: 
                            System.out.println("On card:");
                            playerSpotPrinter.print(spotsOn, players.getCurrent());
                            System.out.println("Off card:");
                            playerSpotPrinter.print(spotsOff, players.getCurrent());

                            boolean workSucc = false;
                            while (!workSucc) {
                            
                            //delete:
                                //Prompt card choice
                                System.out.println("Take a role on card or off card?");
                                System.out.println("1: On card\r\n2: Off card\r\n3: Take no role");

                                //Get user's card choice
                                int cardChoiceSel = -1;
                                while (cardChoiceSel == -1) {
                                    //cardChoiceSel=controller.getOnOffCard();
                                    cardChoiceSel = getNumInput(1, 3);
                                }

                                if (cardChoiceSel == 3) {           //Don't take a role
                                    break;
                                }

                                //Prompt role choice
                                int spotsSize;
                                if (cardChoiceSel == 1) {
                                    spotsSize = spotsOn.size();
                                } else {
                                    spotsSize = spotsOff.size();
                                }
                                System.out.println("Choose role to take (1-" + spotsSize + ")");

                                //Get user's role choice
                                int roleChoiceSel = -1;
                                while (roleChoiceSel == -1) {
                                    roleChoiceSel = getNumInput(1, spotsSize);
                                    //roleChoiceSel=controller.getRoleChoice();
                                }

                                //Add user to roll
                                if (cardChoiceSel == 1) {
                                    PlayerSpot toAddTo = spotsOn.get(roleChoiceSel - 1);
                                    workSucc = toAddTo.addPlayer(players.getCurrent());
                                    players.getCurrent().putFromCard();
                                } else {
                                    PlayerSpot toAddTo = spotsOff.get(roleChoiceSel - 1);
                                    workSucc = toAddTo.addPlayer(players.getCurrent());
                                    players.getCurrent().putFromCard();
                                }

                                if (!workSucc) {   
                                    controller.displayRoleError();
                                    
                                //delete
                                    System.out.println("Role unavailable.");
                                }
                            }
                        }
                        break;

                    case (2):        //Work
                        //Act or rehearse prompt
                        
                        
                     //delete:
                        System.out.println("Act or rehearse?");
                        System.out.println("1: Act\r\n2: Rehearse");

                        int workSel = -1;
                        while (workSel == -1) {
                            workSel = getNumInput(1, 2);
                            //workSel=controller.getActRehearse();
                            if ((workSel == 2) && (players.getCurrent().getRehearse() + players.getCurrent().getRank() >= 6)) {  //No need for more rehearse markers
                              //delete:
                                System.out.println("You have enough rehearse markers to guarantee success. Time to act!");
                                
                                controller.displayForceAct();
                                workSel = 1;    //Force player to act
                            }
                        }

                        if (workSel == 1) {  //Act
                            Work CurrWork = new Work(); //Perform work
                            Room currentSSet = board.getRoom(players.getCurrent().getLocation());
                            System.out.println("Takes Remaining: " + currentSSet.getRemainingTakes());
                            boolean complete = CurrWork.workRole(players, board);
                            if (complete) {
                                Room currentSet = board.getRoom(players.getCurrent().getLocation());
                                SceneCard currCard = currentSet.getCard();
                                currCard.resetCard();
                                moderator.advanceScene(currentSet);     //Advance scene
                                ArrayList<SceneCard> active = deck.getActiveCard();
                                for (int i = 0; i < active.size(); i++) {
                                    SceneCard iCard = active.get(i);
                                    if (!iCard.isActive()) {        //Remove card from set
                                        active.remove(i);
                                    }
                                }
                                players.getCurrent().remFromCard();
                                currentSet.reset();
                                currCard.resetCard();
                                active.trimToSize();
                                //controller.sceneWrap(currentSet);
                             
                             //delete:   
                                System.out.println("Scene Wrapped! Cards remaining on the board: " + active.size());
                                
                                if (active.size() == 1) {   //Complete day
                                    currentDay = false;
                                    daysLeft--;
                                    //controller.endDay();
                                 
                                 //delete:   
                                    System.out.println("Day Completed! Days Remaining: " + daysLeft);
                                    //controller.updateDayCount();
                                }
                            }
                        } else { //Rehearse
                            players.getCurrent().addRehearse();     //Give rehearse marker
                        }
                        break;

                    default:
                        break;
                }

                players.nextCurrent();      //Change active player

                if (daysLeft == 0) {        //End game
                    gameOngoing = false;
                }
            }
            //Reset day
            currentDay = true;
            moderator.advanceDay();
        }
   //delete:
        System.out.println("\r\nAll days completed!");
        display.dispPlayers();
        
        //controller.updatePlayerDisplay();

        Player winner = calculator.calcWinner(playersOrdered);          //Display winner
        System.out.println("\r\nWinner: " + winner.getName() + "!");
        //controller.displayWinner(winner.getName());

        System.out.println("\r\nThank you for playing!\r\n");
    }


    //Helper function
    //getNumInput: Prompts for user input and returns number input or -1 if error
    private static int getNumInput(int lowerB, int upperB) {
        Scanner keyboard = new Scanner(System.in);

        String userNumInput = keyboard.nextLine();
        while (userNumInput == null) {
        }      //Blocking code: wait for user input

        try {
            int userNum = Integer.parseInt(userNumInput);
            if (userNum >= lowerB && userNum <= upperB) {
                return userNum;
            } else {
                System.out.println("Out of bounds. Please try again.");
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Non-number. Please try again.");
            return -1;
        }
    }
}