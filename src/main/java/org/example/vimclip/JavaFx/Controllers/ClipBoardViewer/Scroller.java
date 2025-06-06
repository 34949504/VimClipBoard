package org.example.vimclip.JavaFx.Controllers.ClipBoardViewer;

import org.example.vimclip.Observar;

public class Scroller implements Observar {

    SharedInfo sharedInfo;
    SharedInfo.WholePackage wholePackage_current;

    public Scroller(SharedInfo sharedInfo){

        this.sharedInfo = sharedInfo;
    }


    @Override
    public void bloc_was_created() {

    }

    @Override
    public void tab_changed(Character reg) {
    }

    @Override
    public void move_scrollbar(String direction) {

        System.out.println("Moving scrollabr ");
    }

    private void moving(String direction)
    {


    }


}
