package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.vimclip.Observar;

import java.util.ArrayList;

public class Scroller implements Observar {

    SharedInfo sharedInfo;
    SharedInfo.WholePackage wholePackage_current = null;
    int current_index = 0;

    public Scroller(SharedInfo sharedInfo){

        this.sharedInfo = sharedInfo;
    }


    @Override
    public void bloc_was_created() {

        System.out.println("here in scrollbar bloc was cre");
    }

    @Override
    public void tab_changed(Character reg) {

        System.out.println("here in scrollbar tab changed");
    }

    @Override
    public void move_scrollbar(String direction) {

        System.out.println("Moving scrollabr ");
        moving(direction);
    }

    private void moving(String direction)
    {

        boolean getFirst = true;
        System.out.println("Direction is "+direction);

        if (checkIfthereAreBlocs()) {

            ArrayList<SharedInfo.WholePackage> CWP = sharedInfo.getCurrentWholePackageArray();
            int arraySize = CWP.size();

            if (wholePackage_current != null) {
                // Unselect the current one
                wholePackage_current.getBlocText().setBlueSelection(false);

                int offset = direction.equals("up") ? -1 : 1;
                current_index += offset;

                // Wrap around
                if (current_index >= arraySize) {
                    current_index = 0;
                } else if (current_index < 0) {
                    current_index = arraySize - 1;
                }

            } else {
                // If nothing is currently selected, start from top or bottom
                current_index = getFirst ? 0 : arraySize - 1;
            }

            // Apply selection to the new item
            SharedInfo.WholePackage newSelection = CWP.get(current_index);
            newSelection.getBlocText().setBlueSelection(true);
            wholePackage_current = newSelection;

            Platform.runLater(() -> {
                ScrollPane scrollPane = sharedInfo.getScrollPane_blocs();
                Label targetLabel = newSelection.getBlocText().getLabel();
                VBox content = sharedInfo.getContentPane();

                Bounds contentBounds = content.getLayoutBounds();
                Bounds nodeBounds = targetLabel.localToScene(targetLabel.getBoundsInLocal());
                Bounds scrollBounds = scrollPane.getContent().localToScene(scrollPane.getContent().getBoundsInLocal());

                double y = nodeBounds.getMinY() - scrollBounds.getMinY();
                double scrollY = y / contentBounds.getHeight();

                scrollPane.setVvalue(scrollY);  // Scroll vertically
            });
        }

    }

    @Override
    public void up_or_down_keyPressed(String direction) {
        if (wholePackage_current != null)
        {
            moving(direction);
        }
    }

    private boolean checkIfthereAreBlocs()
    {
       ArrayList<SharedInfo.WholePackage> CWP =  sharedInfo.getCurrentWholePackageArray();

       if (CWP.size() <= 0)
       {
           System.out.println("Regresando, no se puede selecionar nada porque no hay nada");
           return false;
       }

       return true;

    }


}
