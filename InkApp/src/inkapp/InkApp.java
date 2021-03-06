package InkApp;
  
import GraphicsLib.G;
import GraphicsLib.G.BBox;
import GraphicsLib.G.V;
import GraphicsLib.G.VS;
import InkApp.Ink.Blend;
import GraphicsLib.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
  

 public class InkApp extends Window {
    public static final int W = UC.initialWindowWidth,  
    		H = UC.initialWindowWidth, 
    		GBL = UC.greenBoxLoc,
    		GBS = UC.greenBoxSize;
    public static G.VS BACKGROUND = new VS(new V(), new V(W,H)); 
    public static VS nb = new VS(new BBox(GBL,GBS));
    public static InkList inkList = null;
    public static Boxes boxes = new Boxes();
    public static Layer layer =  Layer.getNewLayer();
     
    public InkApp() {
      super("Ink App", W, H);
      Trainer.trainShapes("N-N");
    }
    
    protected void paintComponent(Graphics g){
      BACKGROUND.fill(g, Color.WHITE);
      g.setColor(Color.BLACK);
      Layer.ALL.show(g);
    }
    
    
/**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
      PANEL = new InkApp();
      launch();
    }
    
    public void mousePressed(MouseEvent me){
      int mx = me.getX(), my = me.getY();
      Area.DN(mx, my);
      repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent me){
      int mx = me.getX(), my = me.getY();
      Area.UP(mx, my);
      repaint();
    }
    
    @Override
    public void mouseDragged(MouseEvent me){
      int mx = me.getX(), my = me.getY();
      Area.DRAG(mx, my);
      repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent ke) {
	    	char c = ke.getKeyChar();
	    	if(Trainer.isActive) {
	    		Trainer.theTrainer.charTyped(c);
	    		repaint();
	    	}
    }
    
    public static class InkArea extends Area {

		public InkArea() {
			super(BACKGROUND, Color.white, "InkArea : InkArea");
		}

		public void dn(int x, int y) {
			inking = true;
		}
		
		public void up(int x, int y) {
			inkList.addNew();
		}
    }
    
    public static class InkList extends ArrayList<Ink> implements I.Show{
    	
      public void addNew(){
    	  Blend newbl = (new Ink.Blend()).setBlendFromBuffer();
    	  if(size() == 0) {
    		  this.add(new Ink(newbl, new BBox(Ink.BUFFER.bbox)));
    	  }
    	  else {
    		  Blend blend = findClosest(newbl);
    		  if(blend.distToNorm(newbl) > UC.hugeDistance) {
    			  this.add(new Ink(newbl, new BBox(Ink.BUFFER.bbox)));
    		  } else {
    			  blend.blend(newbl);
    			  this.add(new Ink(blend, new BBox(Ink.BUFFER.bbox)));
    		  }
    		  
    	  }
      }
      
      public Blend findClosest(Ink.Norm n) {  
    	  Blend res = get(0).blend;
    	  int d = res.distToNorm(n);
    	  for(int i=1;i<size();i++) {
    		  int k = get(i).blend.distToNorm(n);
    		  if(k<d) {
    			  d = k;
    			  res = get(i).blend;
    		  }
    	  }
    	  return res;
	  }
      
      public void show(Graphics g){for(Ink ink:this){ink.show(g);}}
    }
   
  }