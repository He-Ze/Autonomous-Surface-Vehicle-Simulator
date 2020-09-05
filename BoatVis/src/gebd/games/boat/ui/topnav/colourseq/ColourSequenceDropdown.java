package gebd.games.boat.ui.topnav.colourseq;

import blindmystics.input.CurrentInput;
import gebd.games.boat.BoatVis;
import gebd.games.boat.entity.ColourSequenceNode;
import loader.LoadedObjectHandler;
import renderables.r2D.composite.TopLevelInterface;
import renderables.r2D.composite.userinterface.scroll.DropdownMenu;
import renderables.r2D.composite.userinterface.scroll.DropdownMenuItem;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 * Created by CaptainPete on 2016-11-13.
 */
public class ColourSequenceDropdown extends DropdownMenu {

    private BoatVis boatVis;
    private ColourSequenceBox colourSequenceBox;
    private int currentEditingIndex = -1;

    public ColourSequenceDropdown(String menuName, Vector2f relativePosition, Vector2f size, float rotation, BoatVis boatVis) {
        super(menuName, relativePosition, size, rotation);

        this.boatVis = boatVis;

        addDropdownOption(new DropdownMenuItem("Add new colour.") {
            @Override
            public void onSelect() {
                currentEditingIndex = -1;
                colourSequenceBox.setVisible(true);
            }

            @Override
            public boolean enabled() {
                return true;
            }
        });

    }

    @Override
    public void update(CurrentInput input, float delta) {
        super.update(input, delta);
    }

    public void resetAllTimings() {
        while (getMenuSize() >= 2) {
            removeDropdownOption(1);
        }
    }
    public void addColourSequence(ColourSequenceNode colourSequenceNode) {
        currentEditingIndex = -1;
        addNewColourTiming(
                colourSequenceNode.getColour(),
                colourSequenceNode.getDuration(),
                colourSequenceNode.getColourBlendAmount()
        );
    }

    private void addNewColourTiming(Vector3f colour, float duration, float colourBlendAmount) {

        String text = colour.toString() + " at " + (colourBlendAmount * 100) + "% for " + duration + "ms";

        ArrayList<ColourSequenceNode> currentColourNodes = boatVis.getColourSequenceHandler().getColourSequenceNodes();

        if (currentEditingIndex < 0) {

            final int listPosition = currentColourNodes.size();
            currentColourNodes.add(new ColourSequenceNode(colour, colourBlendAmount, duration));

            //Add a new dropdown item.
            addDropdownOption(new DropdownMenuItem(text) {
                @Override
                public void onSelect() {
                    currentEditingIndex = listPosition;
                    colourSequenceBox.show(
                            boatVis.getColourSequenceHandler().getColourSequenceNodes().get(listPosition)
                    );
                }

                @Override
                public boolean enabled() {
                    return true;
                }
            });
        } else {

            //Update the existing item.
            VisibleDropdownOption visibleDropdownOption = getMenuItem(currentEditingIndex + 1);
            visibleDropdownOption.setText(text);

            ColourSequenceNode colourSequenceNode = boatVis.getColourSequenceHandler().getColourSequenceNodes().get(currentEditingIndex);
            colourSequenceNode.setColour(colour);
            colourSequenceNode.setColourBlendAmount(colourBlendAmount);
            colourSequenceNode.setDuration(duration);

        }

    }

    @Override
    public void loadDependencies(LoadedObjectHandler<?> handler) {
        colourSequenceBox = handler.newDependancy(new ColourSequenceBox(){

            @Override
            protected void onDisplayClosed(Vector3f colour, float duration, float colourBlendAmount) {
                addNewColourTiming(colour, duration, colourBlendAmount);
            }

        });
        colourSequenceBox.setVisible(false);
        TopLevelInterface.addComponentToTopLayer(colourSequenceBox);

        super.loadDependencies(handler);
    }
}
