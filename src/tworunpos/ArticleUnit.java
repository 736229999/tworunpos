package tworunpos;

public class ArticleUnit {
	final static int TYPE_PIECE = 0;
	final static int TYPE_WEIGHT = 1;

	final static String[] TYPE_NAMES_GERMAN = {"Stück", "Kilogramm"};
	final static String[] TYPE_SHORTNAMES_GERMAN = {"Stck", "kg"};
	
	private int unit = 0;
	
	


	public  ArticleUnit(){
	}
	
	public  ArticleUnit(int unit){
		this.unit = unit;
	}
	
	public  ArticleUnit(String unitName){
		this.setUnitTypeByName(unitName);
	}
	
	
	public boolean setUnitTypeByName(String name){
		DebugScreen.getInstance().print("setUnitTypeByName: "+name);
		for(int i = 0; i < TYPE_NAMES_GERMAN.length; i++){
			DebugScreen.getInstance().print("Iterate: "+name);
			if(TYPE_NAMES_GERMAN[i].equals( name)){
				DebugScreen.getInstance().print("Found: "+TYPE_NAMES_GERMAN[i]);
				unit = i;
				return true;
			}
		}
		return false;
	}

	public String getNameGerman(){
		return TYPE_NAMES_GERMAN[unit];
	}
	
	public String getShortNameGerman(){
		return TYPE_SHORTNAMES_GERMAN[unit];
	}
	
	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	public boolean isPiece(){
		if(unit == TYPE_PIECE){
			return true;
		}
		return false;
	}
	public boolean isWeight(){
		if(unit == TYPE_WEIGHT){
			return true;
		}
		return false;
	}
	public String toString(){
		return getShortNameGerman();
	}
	
	
}
