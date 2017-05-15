package item;

import entity.Combatant;

/**
 * TODO: Fix magic numbers.
 * @author matti
 */
public class Vial extends Item {
	public static final Item[] CATALOG_VIAL = new Item[]{
		new Vial("Cardiotic Fluid", "Dark red flakes dance in the pinkish tincture.", 0, 0) {
			
			//TESTING adding player parameter.
			@Override
			public void use(Combatant user) {
				user.decreaseHealthBy(14);
			}
		},
		
		new Vial("Solution of Finesse", "The orange liquid eddies dizzyingly.", 0, 48) {
			@Override
			public void use() {
				
			}
		},
		
		new Vial("Test Ichor", "What is this?", 48, 0) {
			@Override
			public void use() {
				
			}
		}
	};
	
	private Vial(String name, String desc, int xMargin, int yMargin) {
		super("img/item/vials.png", 0, 0);
		this.name = name;
		this.descVis = desc;
		this.xMargin = xMargin;
		this.yMargin = yMargin;
	}

	@Override
	public void use() {}

	@Override
	public void use(Combatant user) {}
}