public class HappyNewYear {
    private float x = 0;
    private float y = 0;
    private int direction = 1;

    
    public HappyNewYear(){
//        textFont(createFont("Georgia", 32));
        textFont(createFont("Ubuntu-Bold", 70));
        y = height-10;
    }

    public void update(){
        x = x + 5*direction;
        
        if(x<-100 || x >width/2){
            direction = -direction;
        }
    }
    
    public void show(){
        text("HAPPY NEW YEAR 2013", x, y);
    }
}
