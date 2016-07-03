package jeiexporter.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jeiexporter.render.RenderFluid;
import jeiexporter.render.RenderIDrawable;
import jeiexporter.render.RenderItem;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.lang.reflect.Field;

public class Adapters
{
    public static final TypeAdapter<IRecipeCategory> drawable = new TypeAdapter<IRecipeCategory>()
    {
        @Override
        public void write(JsonWriter out, IRecipeCategory value) throws IOException
        {
            out.beginObject();
            IDrawable drawable = value.getBackground();
            out.name("w").value(drawable.getWidth());
            out.name("h").value(drawable.getHeight());
            out.name("tex").value(RenderIDrawable.render(drawable, value.getUid()));
            out.endObject();
        }

        @Override
        public IRecipeCategory read(JsonReader in) throws IOException
        {
            return null;
        }
    };

    public static final TypeAdapter<IGuiIngredient<ItemStack>> itemIngredient = new TypeAdapter<IGuiIngredient<ItemStack>>()
    {
        @Override
        public void write(JsonWriter out, IGuiIngredient<ItemStack> value) throws IOException
        {
            out.beginObject();
            out.name("x").value(getInt(x, value));
            out.name("y").value(getInt(y, value));
            out.name("w").value(getInt(w, value));
            out.name("h").value(getInt(h, value));
            out.name("p").value(getInt(p, value));
            out.name("in").value(value.isInput());
            out.name("amount").value(value.getAllIngredients().size() > 0 ? value.getAllIngredients().get(0).stackSize : 0);
            out.name("stacks").beginArray();
            for (ItemStack itemStack : value.getAllIngredients())
                out.value(RenderItem.render(itemStack));
            out.endArray();
            out.endObject();
        }

        @Override
        public IGuiIngredient<ItemStack> read(JsonReader in) throws IOException
        {
            return null;
        }
    };

    public static final TypeAdapter<IGuiIngredient<FluidStack>> fluidIngredient = new TypeAdapter<IGuiIngredient<FluidStack>>()
    {
        @Override
        public void write(JsonWriter out, IGuiIngredient<FluidStack> value) throws IOException
        {
            out.beginObject();
            out.name("x").value(getInt(x, value));
            out.name("y").value(getInt(y, value));
            out.name("w").value(getInt(w, value));
            out.name("h").value(getInt(h, value));
            out.name("p").value(getInt(p, value));
            out.name("in").value(value.isInput());
            out.name("amount").value(value.getAllIngredients().size() > 0 ? value.getAllIngredients().get(0).amount : 0);
            out.name("fluids").beginArray();
            for (FluidStack fluidStack : value.getAllIngredients())
                out.value(RenderFluid.render(fluidStack));
            out.endArray();
            out.endObject();
        }

        @Override
        public IGuiIngredient<FluidStack> read(JsonReader in) throws IOException
        {
            return null;
        }
    };

    private static int getInt(Field field, Object object)
    {
        try
        {
            return field.getInt(object);
        } catch (IllegalAccessException e)
        {
            return 0;
        }
    }

    private static Field x;
    private static Field y;
    private static Field w;
    private static Field h;
    private static Field p;

    static
    {
        try
        {
            x = GuiIngredient.class.getDeclaredField("xPosition");
            x.setAccessible(true);
            y = GuiIngredient.class.getDeclaredField("yPosition");
            y.setAccessible(true);
            w = GuiIngredient.class.getDeclaredField("width");
            w.setAccessible(true);
            h = GuiIngredient.class.getDeclaredField("height");
            h.setAccessible(true);
            p = GuiIngredient.class.getDeclaredField("padding");
            p.setAccessible(true);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }


}
