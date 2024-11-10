public class Relic {
    private String name;
    private int id;
    private Shop.Rarity rarity;
    private String description;
    private int requirement;
    
    public Relic(String name, int id, Shop.Rarity rarity, String description, int requirement){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.description = description;
        this.requirement = requirement;
    }
    
    public String getName(){
        return name;
    }
    
    public int getId(){
        return id;
    }
    
    public Shop.Rarity getRarity(){
        return rarity;
    }
    
    public int getRequirement(){
        return requirement;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String toString(){
        return name + " - " + description;
    }
}
