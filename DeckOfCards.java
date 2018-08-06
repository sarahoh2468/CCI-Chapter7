import java.util.Random;

public class DeckOfCards {
    /* Design the data structures for a generic deck of cards. Explain how you would subclass the
    data structures to implement blackjack
    */
    public class Card {
        private String suit;
        public static final String[] kindOfSuit = {"Club", "Heart", "Spade", "Diamond"};
        private int value;

        public Card(int i, String s) {
            this.setSuit(s);
            this.setValue(i);
        }

        public boolean isValidSuit(String s) {
            for (int i = 0; i < kindOfSuit.length; i++) {
                if (kindOfSuit[i].equals(s)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isValidValue(int i) {
            if (i < 1 || i > 13) {
                return false;
            }
            return true;
        }

        public void setSuit(String s) {
            if (isValidSuit(s)) {
                this.suit = s;
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        public void setValue(int i) {
            if (isValidValue(i)) {
                this.value = i;
            }
            else {
                throw new IllegalArgumentException();
            }
        }
    }

    public class Deck {
        public static final int num_cards = 52;

        private Card[] cards;
        private int numCardsLeft;
        private Random rand;

        public Deck(int seed) {
            this.cards = new Card[num_cards];
            int count = 0;
            for (int i = 0; i < Card.kindOfSuit.length; i++) {
                for (int val = 1; val <= 13; val++) {
                    this.cards[count] = new Card(val, Card.kindOfSuit[i]);
                    count++;
                }
            }
            this.numCardsLeft = num_cards;

            if (seed == -1) {
                this.rand = new Random();
            } else {
                this.rand = new Random(seed);
            }
        }
        public void reset() {
            this.numCardsLeft = num_cards;
        }

        public void shuffle() {
            for (int i = 0; i < num_cards; i++) {
                int swapWith = Math.abs(this.rand.nextInt()) % num_cards;
                Card temp = this.cards[i];
                this.cards[i] = this.cards[swapWith];
                this.cards[swapWith] = temp;
            }
        }

        public Card drawCard() {
            if (this.numCardsLeft == 0) {
                this.reset();
                this.shuffle();
            }
            this.numCardsLeft--;
            return this.cards[this.numCardsLeft];
        }
    }
}
