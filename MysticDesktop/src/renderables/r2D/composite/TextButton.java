package renderables.r2D.composite;

import java.awt.Font;

import javax.vecmath.Vector2f;

import blindmystics.input.CurrentInput;
import blindmystics.util.FileReader;
import loader.LoadedObjectHandler;
import renderables.r2D.simple.Button;
import renderables.r2D.text.String2D;
import renderables.r2D.text.String2D.StringAlignment;

public class TextButton extends Button {

	protected String2D renderedText;
	protected String text;
	protected String fontName;
	protected int fontStyle;
	protected Vector2f textSize;

	public static String getDefaultTexture() {
		return FileReader.asSharedFile("test_images/Buttonify.png");
	}
	
	public TextButton(String text, String buttonTexturePath, Vector2f screenPosition, Vector2f size, float rotation) {
		this(text, "Courier", Font.PLAIN, new Vector2f(15, 15), buttonTexturePath, screenPosition, size, rotation);
	}
	
	public TextButton(String text, String fontName, int fontStlye, Vector2f textSize, String buttonTexturePath, Vector2f screenPosition, Vector2f size, float rotation) {
		super(buttonTexturePath, screenPosition, size, rotation);
		this.text = text;
		this.fontName = fontName;
		this.fontStyle = fontStlye;
		this.textSize = textSize;
		renderedText = new String2D(text, StringAlignment.MID_MIDDLE, new Vector2f(0, 0), textSize, fontName, fontStyle);
	}
	
	
	@Override
	public void loadDependencies(LoadedObjectHandler<?> handler) {
		super.loadDependencies(handler);
		renderedText = handler.newDependancy(renderedText);
		addChild(renderedText);
	}
	
	@Override
	public void render() {
		super.render();
		renderedText.render();
	}
	
	public void setText(String newText){
		//System.out.println("newText = " + newText);
		renderedText.setText(newText);
	}
	
	public String getText(){
		return renderedText.getText();
	}
	
	public float getTextWidth() {
		return renderedText.getWidth();
	}

	@Override
	public void update(CurrentInput input, float delta) {
		super.update(input, delta);
		renderedText.update(input, delta);
	}
}
