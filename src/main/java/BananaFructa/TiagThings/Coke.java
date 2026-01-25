package BananaFructa.TiagThings;

import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.util.fuel.Fuel;
import net.dries007.tfc.util.fuel.FuelManager;

public class Coke {

    public static void registerFuel() {
        FuelManager.addFuel(new Fuel(IIngredient.of("dustCoke"),1800,1350f,false,true));
    }

}
