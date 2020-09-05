
package renderables.r2D.composite;

import loader.LoadedObjectHandler;
import javax.vecmath.Vector2f;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;

/**
 * A object that only accepts double precision floating point numbers
 * 
 * @author Peter Smith 43180543
 */
public class IntegerTextArea extends TextArea {

  // The minimum allowed number
  protected int minimum;
  // The maximum allowed number
  protected int maximum;
  // The current displayed number
  protected int current;

  // The way in which double numbers should be formated
  private final NumberFormat formatter = new DecimalFormat("#0");


  /**
   * Constructor, sets current to minimum.
   * @param position
   * @param size
   * @param fontName
   * @param fontStyle
   * @param minValue
     * @param maxValue
     */
  public IntegerTextArea(Vector2f position, Vector2f size, String fontName, int fontStyle, int minValue,
                         int maxValue) {
    super(position, size, fontName, fontStyle);

    this.minimum = minValue;
    this.maximum = maxValue;
    this.current = this.minimum;

    this.validCharacters = generateValidIntegerCharacters();

    super.setText("" + this.current);
  }

  /**
   * @return all characters that could be in a double precision floating point number
   */
  public static final HashSet<Character> generateValidIntegerCharacters() {
    HashSet<Character> validCharacters = new HashSet<Character>();
    String validCharacterString = "0123456789-";
    for (int i = 0; i < validCharacterString.length(); i++) {
      Character newChar = validCharacterString.charAt(i);
      validCharacters.add(newChar);
    }
    return validCharacters;
  }

  /**
   * @return minimum value
   */
  public double getMinimum() {
    return this.minimum;
  }

  /**
   * @return maximum value
   */
  public double getMaximum() {
    return this.maximum;
  }

  /**
   * @return the current value.
   */
  public int getCurrentValue() {
    return this.current;
  }

  /**
   * Called when the object is selected.
   */
  @Override
  public void onSelect() {
    // Nothing new needs to happen here.
  }

  /**
   * Called when the object is deselected. Ensure that the current text is valid.
   */
  @Override
  public void onDeselect() {
    String currentText = super.getString();
    this.current = parseCurrentText(currentText);
    super.setText("" + this.current);
  }

  /**
   * Ensures that the current text is a valid double, if not, then it will revert to the previous
   * number.
   * 
   * @param text
   * @return the most recent valid number.
   */
  protected int parseCurrentText(String text) {
    int parsedInteger = 0;
    boolean validNumber = false;
    try {
      // Try to parse the text as an double.
      parsedInteger = Integer.parseInt(text);
      validNumber = true;
    } catch (NumberFormatException e) {
      // Not a valid number, use previous number.
    }
    if (!validNumber) {
      // Good number
      parsedInteger = this.current;
    } else if (parsedInteger < this.minimum) {
      // Smaller than the minimum, set as minimum
      parsedInteger = this.minimum;
    } else if (parsedInteger > this.maximum) {
      // Greater than the maximum, set as maximum
      parsedInteger = this.maximum;
    } else {
      // Don't change the number.
    }
    return parsedInteger;
  }

  @Override
  public void completeLoad(LoadedObjectHandler<?> handler) {
    super.completeLoad(handler);
    setText("" + this.current);
  }

  /**
   * Sets the text to the new text, but only if the text represents a valid integer.
   */
  @Override
  public void setText(String newText) {
    if(hasLoaded){
      int newCurrent = parseCurrentText(newText);
      if ((combinedText == null) || (newCurrent != this.current)) {
        this.current = newCurrent;
        super.setText(formatter.format(this.current));
      }
    }
  }


  /**
   * Sets the text to the new current value.
   * 
   * @param newCurrent
   */
  public void setValue(int newCurrent) {
    if (newCurrent != this.current) {
      this.current = newCurrent;
      super.setText(this.formatter.format(this.current));
    }
  }

}
