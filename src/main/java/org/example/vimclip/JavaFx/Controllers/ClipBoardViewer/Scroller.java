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
    StageFocuser stageFocuser;

    public Scroller(SharedInfo sharedInfo){

        this.sharedInfo = sharedInfo;
        stageFocuser =  new StageFocuser(sharedInfo);
    }


    @Override
    public void bloc_was_created() {

        System.out.println("here in scrollbar bloc was cre");
    }

    @Override
    public void tab_changed(Character reg) {

        wholePackage_current = null;

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

                if (!wholePackage_current.getBlocText().isSelected())
                    wholePackage_current.getBlocText().setBlueSelection(false);
                else {

                    wholePackage_current.getBlocText().set_selected(true);
                }


                int offset = direction.equals("move_up") ? -1 : 1;
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
            if (!newSelection.getBlocText().isSelected())
                newSelection.getBlocText().setBlueSelection(true);
            else {
                newSelection.getBlocText().setBrighterSelection();
            }

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
                stageFocuser.giveFocus();
            });
        }

    }

    @Override
    public void up_or_down_keyPressed(String direction) {
       if (!sharedInfo.getStage().isIconified()) {
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

    @Override
    public void block_was_clicked() {

        System.out.println("clicked detected");
        ArrayList<SharedInfo.WholePackage> CWP = sharedInfo.getCurrentWholePackageArray();

        for (int i = 0; i < CWP.size(); i++) {
            SharedInfo.WholePackage Wp = CWP.get(i);
            BlocText blocText = Wp.getBlocText();
            if (blocText.isClicked_righNow())
            {
               if (wholePackage_current != null)
               {

                   if (!wholePackage_current.getBlocText().isSelected())
                       wholePackage_current.getBlocText().setBlueSelection(false);
                   else {

                       wholePackage_current.getBlocText().set_selected(true);
                   }

                   SharedInfo.WholePackage newSelection = CWP.get(i);
                   current_index = i;
                   if (!newSelection.getBlocText().isSelected())
                       newSelection.getBlocText().setBlueSelection(true);
                   else {
                       newSelection.getBlocText().setBrighterSelection();
                   }
                   blocText.setClicked_righNow(false);

                   wholePackage_current = newSelection;

                   return;

               }
            }
        }
    }

    @Override
    public void spaceBar_keyPressed() {
        if (wholePackage_current != null)
        {
            BlocText blocText = wholePackage_current.getBlocText();

            if (blocText.isSelected())
            {
               blocText.set_selected(false);
            }
            else {

                blocText.setBlueSelection(false);
                blocText.set_selected(true);
            }
        }
    }
}
