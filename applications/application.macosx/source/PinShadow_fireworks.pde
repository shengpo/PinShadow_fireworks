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
int cols = 200;                //pin的column數
int rows = 60;                 //pin的row數

//for fireworks
ArrayList<Fireworks> fireworksList = null;

//for happy new year
HappyNewYear happyNewYear = null;

//switch showing reference image
boolean isShowReferenceImage = false;


void setup(){
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

void draw(){
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


void mouseMoved(){
    if(frameCount % 6 == 0){
        fireworksList.add(new Fireworks(mouseX, mouseY));
    }
}

void keyPressed(){
    if(key == ' '){
        isShowReferenceImage = !isShowReferenceImage;
    }
}
