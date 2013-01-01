import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PinShadow_fireworks extends PApplet {

/*****************************************
 Author: Shen, Sheng-Po (http://shengpo.github.com)
 Date:   2013.01.01
 License: CC BY-SA 3.0

This is a fireworks version of Pin Shadow project for celebrating NEW 2013 YEAR!!

*****************************************/

//for garbage collection
GarbageCollector gc;

//for pin manager
PinManager pinManager = null;
int cols = 200;                //pin\u7684column\u6578
int rows = 60;                 //pin\u7684row\u6578

//for fireworks
ArrayList<Fireworks> fireworksList = null;

//for happy new year
HappyNewYear happyNewYear = null;

//switch showing reference image
boolean isShowReferenceImage = false;


public void setup(){
    size(1000, 300);
    background(255);
    
    //for pin manager
    pinManager = new PinManager(cols, rows);

    //for fireworks
    fireworksList = new ArrayList<Fireworks>();
    
    //for happy new year
    happyNewYear = new HappyNewYear();

    //for garbage collection
    gc = new GarbageCollector(1000);
}

public void draw(){
    background(255);
 
    //clear and update fireworks
    for(int i=fireworksList.size()-1; i>=0; i--){
        if(fireworksList.get(i).isFinished()){
            fireworksList.remove(i);
        }
    }
 
    for(int i=0; i<fireworksList.size(); i++){
        fireworksList.get(i).update();
        fireworksList.get(i).show();
    }
    
    //for happy new year
    happyNewYear.update();
    happyNewYear.show();
    
    //get reference image
    PImage img = get();
    if(!isShowReferenceImage){
        background(255);
    }

    //show pin shadow animation    
    pinManager.update(img);
    pinManager.show();

    //do garbage collection
    gc.runGC();
}


public void mouseMoved(){
    if(frameCount % 6 == 0){
        fireworksList.add(new Fireworks(mouseX, mouseY));
    }
}

public void keyPressed(){
    if(key == ' '){
        isShowReferenceImage = !isShowReferenceImage;
    }
}
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
        if(d > 1){
            return false;
        }else{
            return true;
        }
    }
}
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
        this.ty = y*0.3f;
        
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
public class GarbageCollector {
        private int counter = 0;
        private int counterFinal = 1000;        //do the garbage collection when the counterFinal value is reached
        
        
        public GarbageCollector(int counterFinal){
                this.counterFinal = counterFinal;
        }

        public void runGC(){
                counter = counter + 1;        //gc counter
                if(counter > counterFinal){
                        counter = 0;
                        System.gc();
                        System.out.println("--> run the java garbage collection");
                }
        }
}

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
public class Pin {
    private PinManager pinManager = null;
    private ArrayList<Pin> pinList = null;          //whole pinList, for referenced
    private ArrayList<Pin> pickedPins = null;    //for picked pins arounding this pin

    public int id = -1;
    
    public float x = 0;
    public float y = 0;

    public boolean isPicked = false;
    
    //to use polar system    
    
    public Pin(int id, float x, float y, boolean isPicked, PinManager pinManager){
        this.id = id;
        this.x = x;
        this.y = y;
        this.isPicked = isPicked;
        this.pinManager = pinManager;

        pinList = pinManager.getPinList();
        pickedPins = new ArrayList<Pin>();
    }
    
    public void update(){
    }
    
    public void show(){
        //for line-up
        if(isPicked){    //\u81ea\u5df1\u6709\u88abpicked\u624d\u505aline-up
            if(pickedPins.size() == 0){    //\u5468\u906d\u6c92\u6709\u88abpicked\u7684pin
                noStroke();
                fill(0);
                ellipse(x, y, 4, 4);
            }else{
                for(Pin p : pickedPins){
                    stroke(0);
                    strokeWeight(1);
                    line(x, y, p.x, p.y);
                }
            }
        }else{
            //original point
            stroke(0);
            strokeWeight(1);
            point(x, y);
        }
    }
    
    //check\u5468\u906d8\u500b\u89d2\u843d\u7684pins\u662f\u5426\u6709\u88abpicked
    public void updateAroundPickedPins(){
        int cols = pinManager.getCols();
        
        //reset all piecked pins
        pickedPins.clear();
        
        //\u81ea\u5df1\u6709\u88abpicked\u624d\u505acheck
        if(isPicked){
            //left-up \u5de6\u4e0a\u89d2
            int id_lu = id - cols - 1;
            if(id_lu>=0 && (id_lu/cols==(id_lu+1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_lu).isPicked){
                    pickedPins.add(pinList.get(id_lu));
                }
            }

            //up \u4e0a
            int id_u = id - cols;
            if(id_u >= 0){
                if(pinList.get(id_u).isPicked){
                    pickedPins.add(pinList.get(id_u));
                }
            }

            //right-up \u53f3\u4e0a\u89d2
            int id_ru = id - cols + 1;
            if(id_ru>=0 && (id_ru/cols==(id_ru-1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_ru).isPicked){
                    pickedPins.add(pinList.get(id_ru));
                }
            }

            //left \u5de6
            int id_l = id - 1;
            if(id_l>=0 && (id_l/cols==(id_l+1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_l).isPicked){
                    pickedPins.add(pinList.get(id_l));
                }
            }

            //right \u53f3
            int id_r = id + 1;
            if(id_r>=0 && (id_r/cols==(id_r-1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_r).isPicked){
                    pickedPins.add(pinList.get(id_r));
                }
            }

            //left-bottom \u5de6\u4e0b\u89d2
            int id_lb = id + cols - 1;
            if(id_lb>=0 && id_lb<pinList.size() && (id_lb/cols==(id_lb+1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_lb).isPicked){
                    pickedPins.add(pinList.get(id_lb));
                }
            }
 
            //bottom \u4e0b
            int id_b = id + cols;
            if(id_b>=0 && id_b<pinList.size()){
                if(pinList.get(id_b).isPicked){
                    pickedPins.add(pinList.get(id_b));
                }
            }

            //right-bottom \u53f3\u4e0b
            int id_rb = id + cols + 1;
            if(id_rb>=0 && id_rb<pinList.size() && (id_rb/cols==(id_rb-1)/cols)){    //\u5728\u540crow
                if(pinList.get(id_rb).isPicked){
                    pickedPins.add(pinList.get(id_rb));
                }
            }
        }
    }
}
public class PinManager {
    private ArrayList<Pin> pinList = null;
    private int cols = 0;
    private int rows = 0;
    private float xStep = 0;    //\u6bcf\u500bpin\u4e4b\u9593\u5728x\u65b9\u5411\u7684\u9593\u8ddd
    private float yStep = 0;    //\u6bcf\u500bpin\u4e4b\u9593\u5728y\u65b9\u5411\u7684\u9593\u8ddd
    
    
    public PinManager(int cols, int rows){
        this.cols = cols;
        this.rows = rows;
        pinList = new ArrayList<Pin>();
        
        xStep = width/(cols+1f);
        yStep = height/(rows+1f);
        
        //set pin's location and pins' default state is unpicked
        int id = 0;
        for(int j=0; j<rows; j++){
            for(int i=0; i<cols; i++){
                float x = xStep*(i+1);
                float y = yStep*(j+1);
                boolean isPicked = false;

                pinList.add(new Pin(id, x, y, isPicked, this));
                id = id + 1;
            }
        }
    }
    
    //update reference image and set picked info for each pin
    public void update(PImage referImage){
        //set picked pins on whole overview
        for(Pin p: pinList){
            int i = PApplet.parseInt((p.id%cols + 1)*xStep);
            int j = PApplet.parseInt((p.id/cols + 1)*yStep);
            
            if(red(referImage.get(i, j)) < 100){
                p.isPicked = true;
            }else{
                p.isPicked = false;
            }
        }
                
        //to check arounding pins if picked for every pin and to mark them for doing line-up
        for(Pin p : pinList){
            p.updateAroundPickedPins();
        }
    }
    
    public void show(){
        for(Pin p : pinList){
            p.show();
        }
    }
    
    
    public ArrayList<Pin> getPinList(){
        return pinList;
    }
    
    public int getCols(){
        return cols;
    }
    
    public int getRows(){
        return rows;
    }
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "PinShadow_fireworks" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
