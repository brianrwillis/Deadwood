class Player {
    String name;
    private int rank;
    private int credit;
    private int fame;
    private int rehearse;
    private boolean onCard;
    private int location;

    public Player(String name) {
        this.name = name;
        this.rank = 1;
        this.fame = 0;
        this.rehearse = 0;
        this.credit = 0;
        this.onCard = false;
        this.location = 10;
    }

    //Returns score of player
    public int getScore() {
        int score = 5 * this.rank + this.credit + this.fame;
        return score;
    }

    public int getRank() {//return rank
        return this.rank;
    }

    public int getFame() {//return player fame
        return this.fame;
    }

    public int getCredit() {//return player fame
        return this.credit;
    }

    public void setRank(int rank) {//change rank
        this.rank = this.rank + rank;
    }

    public void setCredit(int credit) {//change credit
        this.credit = credit + this.credit;
    }

    public boolean onCard() {//return if player is on a card or not
        return this.onCard;
    }

    public void setFame(int fame) {//change fame
        this.fame = fame + this.fame;
    }

    public void changeLocation(int newLocation) {//changes player location
        this.location = newLocation;
    }

    //Returns location of player
    public int getLocation() {
        return this.location;
    }

    //Adds rehearse marker to player
    public void addRehearse() {
        this.rehearse++;
    }

    //Sets rehearse markers of player to 0
    public void resetRehearse() {
        this.rehearse = 0;
    }

    //Returns rehearse markers of player
    public int getRehearse() {
        return this.rehearse;
    }

    //Returns rehearse markers of player
    public String getName() {
        return this.name;
    }

    //Removes player from card
    public void remFromCard() {
        this.onCard = false;
    }

    //Puts player on card
    public void putFromCard() {
        this.onCard = true;
    }
}