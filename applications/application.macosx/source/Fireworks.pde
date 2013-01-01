public class Fireworks {
    private ArrayList<Circle> circleList = null;

    private float x = 0;
    private float y = 0;
    private float ox = 0;
    private float oy = 0;
    private float tx = 0;
    private float ty = 0;
    private int step = 30;
    
    private boolean isBoomed = false;
    
    
    public Fireworks(float x, float y){
        this.ox = this.x = x;
        this.oy = this.y = y;
        this.tx = x;
        this.ty = y*0.3;
        
        circleList = new ArrayList<Circle>();
        step = (int)random(2, 10);
    }
    
    public void update(){
        x = x + (tx-x)/step;
        y = y + (ty-y)/step;
        
        if(dist(x, y, tx, ty)<1 && !isBoomed){
            int count = (int)random(10, 60);
            float r = random(5, 25);
            float tr = random(60, 120);
            float initDegree = random(360);
            for(int i=0; i<count; i++){
                float cx = tx +r*cos(radians((360/count)*i)+initDegree);
                float cy = ty + r*sin(radians((360/count)*i)+initDegree);
                float ctx = tx + tr*cos(radians((360/count)*i)+initDegree);
                float cty = ty + tr*sin(radians((360/count)*i)+initDegree);
                circleList.add(new Circle(cx, cy, ctx, cty));
            }
            isBoomed = true;
        }
    }
    
    public void show(){
        if(circleList.size()>0 && isBoomed){
            for(Circle c : circleList){
                c.update();
                c.show();
            }
        }else{
            stroke(0);
            strokeWeight(random(1, 5));
            line(ox, oy, x, y);
        }
    }
    
    public boolean isFinished(){
        boolean result = true;
        
        if(isBoomed){
            for(Circle c : circleList){
                result = result && c.isDead();
            }
        }else{
            result = false;
        }
        
        return result;
    }
}
