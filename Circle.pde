public class Circle {
    private float x = 0;
    private float y = 0;
    private float tx = 0;
    private float ty = 0;
    private float d = 10;
    private int step = 30;
    
    
    public Circle(float x, float y, float tx, float ty){
        this.x = x;
        this.y = y;
        this.tx = tx;
        this.ty = ty;
        
        d = random(20);
        step = (int)random(10, 60);
    }
    
    public void update(){
        x = x + (tx-x)/step;
        y = y + (ty-y)/step;
        d = d + (1-d)/step;
    }
    
    public void show(){
        noStroke();
        fill(0);
        ellipse(x, y, d, d);
    }
    
    public boolean isDead(){
        if(d > 8){
            return false;
        }else{
            return true;
        }
    }
}
