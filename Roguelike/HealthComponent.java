public class HealthComponent{
    
    private int cur;
    private int max;
    
    public HealthComponent(int max){
        this.max = max;
        this.cur = max;
    }
    
    public void damage(int amount){
        cur -= lib.clamp(amount, 0, cur);
    }
    
    public void heal(int amount){
        cur += amount;
        if (cur>max){
            cur = max;
        }
    }
    
    // Full heal
    public void heal(){
        cur = max;
    }
    
    public void setCurHealth(int amount){
        cur = amount;
    }
    
    public void setMaxHealth(int max){
        this.max = max;
    }
    
    public int getCurHealth(){
        return cur;
    }
    
    public int getMaxHealth(){
        return max;
    }
    
    public String toString(){
        return cur + "/" + max;
    }
}
