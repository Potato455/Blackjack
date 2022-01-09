package Blackjack;

import Blackjack.Card.Card;
import Blackjack.Card.Rank;
import Blackjack.Card.Suit;

import java.util.*;

public class Blackjack {
    private final int aNDecks;  // nber of decks
    private int aMoney; // balance money of player
    private CardStack aCardPile; // deck to pull cards from

    public Blackjack(int nberDecks, int pMoney){
        aNDecks = nberDecks;
        aMoney = pMoney;
        // initialise aCardPile
        shuffle();
    }

    public void play(){
        // first loop
        // set up phase
        while (true){
            // check if no more money
            if (aMoney == 0){
                System.out.println("Out of money, bye bye.");
                break;
            }
            // dealer and player hand
            List<Card> DealerHand = new ArrayList<Card>();
            List<Card> PlayerHand = new ArrayList<Card>();
            int bet = getBet(); // get bet amount
            aMoney -= bet;  // reduce from balance the bet amount
            // check if aCardPile is empty, if so shuffle
            if (aCardPile.isEmpty()){
                shuffle();
            }
            PlayerHand.add(aCardPile.pop());

            if (aCardPile.isEmpty()){
                shuffle();
            }
            DealerHand.add(aCardPile.pop());

            if (aCardPile.isEmpty()){
                shuffle();
            }
            PlayerHand.add(aCardPile.pop());

            if (aCardPile.isEmpty()){
                shuffle();
            }
            DealerHand.add(aCardPile.pop());

            if (aCardPile.isEmpty()){
                shuffle();
            }

            int PlayerPoint = 0;    // accumulated point of player in current round
            PlayerPoint += getPoint(PlayerHand.get(0), PlayerPoint);

            PlayerPoint += getPoint(PlayerHand.get(1), PlayerPoint);

            System.out.println("Your hand: " + PlayerHand.get(0).toString() + " | " + PlayerHand.get(1).toString());
            System.out.println("Your point: " + PlayerPoint);
            System.out.println("Dealer: " + DealerHand.get(0).toString() + " | " + "UNKNOWN");
            System.out.println("Cards left in deck: " + aCardPile.size());

            // Blackjack !! get 1.5 times your bet
            if (PlayerPoint == 21){
                System.out.println("You WON BLACKJACK!!!");
                System.out.println("Your winning: " + (bet + bet*1.5));
                aMoney += bet + bet*1.5;
                continue;
            }

            int DealerPoint = 0;    // accumulated point of dealer in current round
            DealerPoint += getPoint(DealerHand.get(0), DealerPoint);

            // game playing phase
            while(true) {
                Move move = getMove();
                // if player wants to hit, add new card to player hand
                if (move.equals(Move.HIT)) {
                    Card tempCard = aCardPile.pop();
                    PlayerHand.add(tempCard);
                    if (aCardPile.isEmpty()){
                        shuffle();
                    }

                    PlayerPoint += getPoint(tempCard, PlayerPoint);
                    String result = "Your hand: ";
                    for (int i = 0; i<PlayerHand.size()-1; i++){
                        result += PlayerHand.get(i).toString() + " | ";
                    }
                    result += PlayerHand.get(PlayerHand.size()-1).toString();
                    System.out.println(result);
                    System.out.println("Your point: " + PlayerPoint);
                    System.out.println("Dealer: " + DealerHand.get(0).toString() + " | " + "UNKNOWN");
                    System.out.println("Cards left in deck: " + aCardPile.size());

                    // check for bust
                    if (PlayerPoint>21){
                        System.out.println("BUST!");
                        System.out.println("New balance: " + aMoney);
                        break;
                    }
                }
                // note: take care of aces in list, implementation: getPoint(List l){...}
                // if player wants to stand
                if (move.equals(Move.STAND)) {
                    System.out.println("Dealer has: " + DealerHand.get(0).toString() + " | " + DealerHand.get(1).toString());
                    DealerPoint += getPoint(DealerHand.get(1), DealerPoint);
                    // check blackjack
                    if (DealerPoint == 21){
                        System.out.println("Dealer got BLACKJACK. BUST! :(");
                    }

                    // Dealer starts drawing and compare points
                    while(DealerPoint < 17){
                        Card tempCard = aCardPile.pop();
                        DealerHand.add(tempCard);
                        DealerPoint += getPoint(tempCard, DealerPoint);
                        if (aCardPile.isEmpty()){
                            shuffle();
                        }
                        String result = "Your hand: ";
                        for (int i = 0; i<PlayerHand.size()-1; i++){
                            result += PlayerHand.get(i).toString() + " | ";
                        }
                        result += PlayerHand.get(PlayerHand.size()-1).toString();
                        System.out.println(result);
                        System.out.println("Your point: " + PlayerPoint);

                        String result2 = "Dealer: ";
                        for (int i = 0; i<DealerHand.size()-1; i++){
                            result2 += DealerHand.get(i).toString() + " | ";
                        }
                        result2 += DealerHand.get(PlayerHand.size()-1).toString();
                        System.out.println(result2);
                        System.out.println("Dealer point: " + DealerPoint);
                    }
                    // check dealer bust
                    if (DealerPoint > 21){
                        System.out.println("Dealer BUSTS! YOU WIN " + bet*2);
                        aMoney += bet*2;
                        System.out.println("New balance: " + aMoney);
                    }

                    // compare points
                    else if (PlayerPoint > DealerPoint){
                        System.out.println("YOU WIN " + bet*2);
                        aMoney+= bet*2;
                        System.out.println("New balance: " + aMoney);
                    }
                    // if tie, push
                    else if (PlayerPoint == DealerPoint){
                        System.out.println("PUSH");
                        aMoney+= bet;
                        System.out.println("New balance: " + aMoney);
                    }
                    else {
                        System.out.println("YOU LOSE");
                        System.out.println("New balance: " + aMoney);
                    }
                    System.out.println("Cards left in deck: " + aCardPile.size() + "\n____________________________________________________");

                    break;
                }
            }
        }
    }
    // gets the bet amount in multiple of 10
    private int getBet(){
        while (true){
            System.out.println("Input amount to bet (multiple of 10)");
            Scanner input = new Scanner(System.in);
            int bet = input.nextInt();
            if (bet > aMoney || bet < 0){
                System.out.println("ERROR: You can only bet what you have.");
                continue;
            }
            if (bet%10 == 0){
                System.out.println("Your bet: " + bet);
                return bet;
            }
            else {
                System.out.println("Invalid input, try again");
            }
        }
    }

    // create new shuffled cardstack
    private void shuffle(){
        List<Card> cards = new ArrayList<>();
        for (int i=0; i<aNDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(Card.get(rank, suit));
                }
            }
        }
        Collections.shuffle(cards);
        aCardPile = new CardStack(cards);
    }
    // get point of a card depending on current total point
    private int getPoint(Card c, int totalPoint){
        // if it's an ace
        if (c.getRank().ordinal()==0){
            if (totalPoint<11){
                return 11;
            }
            else {return 1;}
        }
        else if(c.getRank().ordinal()<9){
            return c.getRank().ordinal()+1;
        }
        else return 10;
    }
    // get the move input (hit or stand)
    private Move getMove(){
        while(true){
            System.out.println("h: hit\ns: stand"); //d for double ?
            Scanner input = new Scanner(System.in);
            String move = input.nextLine();
            if (move.equalsIgnoreCase("h")){
                return Move.HIT;
            }
            else if (move.equalsIgnoreCase("s")){
                return Move.STAND;
            }
            else {
                System.out.println("Invalid input, try again.");
            }

        }
    }
    public static void main(String[] args) {
        Blackjack game = new Blackjack(2, 100);
        game.play();
    }

}


