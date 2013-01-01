public class PinManager {
    private ArrayList<Pin> pinList = null;
    private int cols = 0;
    private int rows = 0;
    private float xStep = 0;    //每個pin之間在x方向的間距
    private float yStep = 0;    //每個pin之間在y方向的間距
    
    
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
            int i = int((p.id%cols + 1)*xStep);
            int j = int((p.id/cols + 1)*yStep);
            
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
