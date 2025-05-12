package org.example.vimclip;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import java.awt.event.KeyEvent;
import java.util.Arrays;


public class KeyPressed implements NativeKeyListener {

    private static String CTRL = "ctrl";
    private static  String QUOTE = "quote";
    private static String SHIFT = "shift";

    public ArrayList<String> keyStack = new ArrayList<>();
    public String copied_string = null;
    private Robot robot = null;
    RegistryManager registryManager;
    Character selectedRegister = null;
    private boolean listenKeys = true;
    private Character action = null;

    KeyPressed(RegistryManager registryManager){

        this.registryManager = registryManager;

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {

        if (!listenKeys)
            return;


        String key = NativeKeyEvent.getKeyText(e.getKeyCode()).toLowerCase();
        keyStack.add(key);
        int sklen = keyStack.size();
        boolean allgood = false;

        System.out.println(key);


        System.out.println("sklen " + sklen);


        if (sklen == 1) {

            if (key.compareTo(CTRL) == 0)
                allgood = true;
        }
        if (sklen == 2) {

            if (key.compareTo(CTRL) == 0) {
                gettingCopiedString();
                allgood = true;
            } else if (key.length() == 1 && Character.isLetter(key.charAt(0))) {  // copy contents to ctrl v

                char c = key.charAt(0);
                if (c == 'v' || c == 'c') return;

                String contents = registryManager.getValue(key.charAt(0));
                System.out.println("Contents are " + contents);
                ClipboardUtils.setClipboardContents(contents);

            }

        }
        if (sklen == 3) {

            if (key.length() == 1 && Character.isLetter(key.charAt(0))) {
                selectedRegister = key.charAt(0);
                System.out.println("Register es " + selectedRegister);
                allgood = true;
                registryManager.setValue(selectedRegister, copied_string);
                registryManager.loopy();
                keyStack.clear();

            }

        }
        if (!allgood) {
            System.out.println("Limpiando stack");
            keyStack.clear();
            return;
        }
        System.out.println("Everything chiiling");


    }




    private void gettingCopiedString()
    {
        listenKeys = false;

        robot.keyPress(KeyEvent.VK_CONTROL);
               robot.keyPress(KeyEvent.VK_C);
               robot.keyRelease(KeyEvent.VK_C);
               robot.keyRelease(KeyEvent.VK_CONTROL);
               robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
               robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);



        new Thread(() -> {
                   try {
                       Thread.sleep(100);  // wait for clipboard to update
                       String contents = ClipboardReader.getClipboardContents();
                       System.out.println("Clipboard contents: " + contents);
                       copied_string = contents;

                       listenKeys = true;
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }).start();
    }


    private void pastingString(Character selectedRegister)
    {
        String contents = registryManager.getValue(selectedRegister);
        if (contents == null)
        {
            System.out.println("No hay nada");
            return;
        }

        ClipboardUtils.setClipboardContents(contents);

        listenKeys = false;

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        new Thread(() ->{

            try {

                Thread.sleep(100);
                listenKeys = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }//todo Para copiar a registro ctrl ctrl registro, para obterner valor a ctrl v  es ctrl registro




    public void nativeKeyReleased(NativeKeyEvent e) {
//        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
    }
    public void exiting(NativeKeyEvent e){


        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException nativeHookException) {
                nativeHookException.printStackTrace();
            }
        }

}




}




class Comandos
{
    ArrayList<ArrayList<Object>> comandos_array = new ArrayList<>();
    RegistryManager registryManager;
    String copied_string;

    boolean DEBUG = Constants.DEBUG;

    ArrayList<String> keystack = null;

    int min_length = 0;
    int max_length = 0;


   Comandos(RegistryManager registryManager){
       this.registryManager = registryManager;

       registrar_array(new ArrayList<>(Arrays.asList("ctrl","ctrl","@",new registrar(this))));
       findLengths();
   }

   private void registrar_array(ArrayList<Object> registro)
   {
       comandos_array.add(registro);
   }

   private void findLengths()
   {
       int small = 20;
       int big = -1;  //2,3,3
       for (ArrayList<Object> arrayList: comandos_array)
       {
           int cur_len = arrayList.size();
           if (cur_len > big)
               big = cur_len;

           if (small>cur_len)
               small = cur_len;

       }

       imprimiento_mensaje(String.format("Biggest command length found is %d ", big));
       imprimiento_mensaje(String.format("Smalles command length found is %d ",small));
       this.min_length = small;
       this.max_length = big;
   }


    public boolean do_command(ArrayList<String> keystack)
    {
        this.keystack = keystack;

        ArrayList<Object> command_palette = find_command(keystack);

        if (command_palette != null)
        {
            ((doing_command) command_palette.getLast()).do_command();
            return  true;
        }
        keystack.clear();
        return false;
    }
    public ArrayList<Object> find_command(ArrayList<String>keystack)
    {
        int keystack_len = keystack.size(); // Example: if you press a , then would be 1
        boolean found_match = false;

        for (int i  = 0; i < keystack.size();++i)
        {
            ArrayList<Object> cur_command = comandos_array.get(i);
            int cur_command_size = cur_command.size() -1; // The last item is the function to be called, not to be compared

            imprimiendo_comando("cur command",cur_command,1);
            imprimiendo_comando("keystack command",keystack);
            if ( cur_command_size  != keystack_len){

               imprimiento_mensaje(String.format("El length del comando y del keystack no coinciden %d %d ",keystack_len,cur_command_size));
                continue; // -1 because the last item is the func to be executed
            }


            for (int x = 0; x < cur_command_size;++x) //Looping through individual commands
            {
                String cur_command_string = (String) cur_command.get(i);
                String cur_keystack_string = keystack.get(i);
                int cur_keystack_string_len = cur_keystack_string.length();

                imprimiento_mensaje(String.format("Inside inner for loop: Comparing cur command and keystack\n%s and %s",cur_command_string,cur_keystack_string));


                if (cur_command_string.compareTo(cur_keystack_string) == 0)
                {
                    imprimiento_mensaje("El current string del commando y del stack key coinciden");
                }
                else if (cur_command_string.compareTo("@") == 0) // @ denotes a letter from a - z and the string must be of length
                                                            // 1 to be true, because if somebody presses shift, then string would be long
                {
                    imprimiento_mensaje("El comando tiene como string @");

                    if (cur_keystack_string_len == 1 && Character.isLetter(cur_keystack_string.charAt(0)))
                    {
                        imprimiento_mensaje("El string del keystack es una letra");
                    }
                    else
                    {
                        imprimiendo_mensaje(String.format("El string del keystack no es una letra, su valor es %s",cur_keystack_string));
                        found_match = false;
                    }

                }
                else
                {

                    imprimiento_mensaje(("Match not found "));
                    found_match = false;
                    break;
                }

            }

            if (found_match)
            {
                imprimiento_mensaje("Se encontró un match");
                return cur_command;
            }

        }
        return  null;

    }

    private void imprimiendo_comando (String name,ArrayList<Object> comando,int offset)
    {
        System.out.printf("Comando %s:",name);
        for (int i  = 0; i < comando.size() - offset;++i)
        {
            System.out.print(comando.get(i));
            System.out.print(",");
        }
        System.out.println();

    }

    private void imprimiento_mensaje(String mensaje)
    {
        if (DEBUG){
            System.out.println(mensaje);
        }
    }

    private void imprimiendo_comando (String name,ArrayList<String> comando)
    {
        System.out.printf("Comando %s:",name);
        for (int i  = 0; i < comando.size() ;++i)
        {
            System.out.print(comando.get(i));
            System.out.print(",");
        }
        System.out.println();

    }

    public void setCopied_string(String copied_string) {
        this.copied_string = copied_string;
    }

    public String getCopied_string() {
        return copied_string;
    }

    public RegistryManager getRegistryManager() {
        return registryManager;
    }

    public ArrayList<String> getKeystack() {
        return keystack;
    }
}

interface doing_command{

    void do_command();

}


class registrar implements doing_command
{
    Comandos c;

    public registrar(Comandos c)
    {
        this.c = c;
    }
    @Override
    public void do_command() {
        String copied_string = c.getCopied_string();
        char selectedRegister = c.getKeystack().get(2).charAt(0);
        System.out.println("Register es "+selectedRegister);
        c.getRegistryManager().setValue(selectedRegister,copied_string);
        c.getRegistryManager().loopy();
        c.getKeystack().clear();
    }
}

