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
        if(isPicked){    //自己有被picked才做line-up
            if(pickedPins.size() == 0){    //周遭沒有被picked的pin
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
    
    //check周遭8個角落的pins是否有被picked
    public void updateAroundPickedPins(){
        int cols = pinManager.getCols();
        
        //reset all piecked pins
        pickedPins.clear();
        
        //自己有被picked才做check
        if(isPicked){
            //left-up 左上角
            int id_lu = id - cols - 1;
            if(id_lu>=0 && (id_lu/cols==(id_lu+1)/cols)){    //在同row
                if(pinList.get(id_lu).isPicked){
                    pickedPins.add(pinList.get(id_lu));
                }
            }

            //up 上
            int id_u = id - cols;
            if(id_u >= 0){
                if(pinList.get(id_u).isPicked){
                    pickedPins.add(pinList.get(id_u));
                }
            }

            //right-up 右上角
            int id_ru = id - cols + 1;
            if(id_ru>=0 && (id_ru/cols==(id_ru-1)/cols)){    //在同row
                if(pinList.get(id_ru).isPicked){
                    pickedPins.add(pinList.get(id_ru));
                }
            }

            //left 左
            int id_l = id - 1;
            if(id_l>=0 && (id_l/cols==(id_l+1)/cols)){    //在同row
                if(pinList.get(id_l).isPicked){
                    pickedPins.add(pinList.get(id_l));
                }
            }

            //right 右
            int id_r = id + 1;
            if(id_r>=0 && (id_r/cols==(id_r-1)/cols)){    //在同row
                if(pinList.get(id_r).isPicked){
                    pickedPins.add(pinList.get(id_r));
                }
            }

            //left-bottom 左下角
            int id_lb = id + cols - 1;
            if(id_lb>=0 && id_lb<pinList.size() && (id_lb/cols==(id_lb+1)/cols)){    //在同row
                if(pinList.get(id_lb).isPicked){
                    pickedPins.add(pinList.get(id_lb));
                }
            }
 
            //bottom 下
            int id_b = id + cols;
            if(id_b>=0 && id_b<pinList.size()){
                if(pinList.get(id_b).isPicked){
                    pickedPins.add(pinList.get(id_b));
                }
            }

            //right-bottom 右下
            int id_rb = id + cols + 1;
            if(id_rb>=0 && id_rb<pinList.size() && (id_rb/cols==(id_rb-1)/cols)){    //在同row
                if(pinList.get(id_rb).isPicked){
                    pickedPins.add(pinList.get(id_rb));
                }
            }
        }
    }
}
