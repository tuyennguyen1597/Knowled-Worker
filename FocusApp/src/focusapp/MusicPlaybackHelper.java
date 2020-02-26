/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package focusapp;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author blair
 */
public class MusicPlaybackHelper {
    public static MediaPlayer globalMediaPlayer;
    public static boolean status;
    public static int counter;
    
    
    
    public static boolean playMusic(String filename) {
        boolean isSuccessful = false;
        if(status==false && counter==0){     
                try {
            Media media = new Media(new File(filename).toURI().toString());
            MusicPlaybackHelper.globalMediaPlayer = new MediaPlayer(media);
            MusicPlaybackHelper.globalMediaPlayer.play();
            isSuccessful = true;
            status=true;
            counter++;
        } catch (Exception e) {
            isSuccessful = false;
        } 
        } else {
            status=false;
            counter=0;
            MusicPlaybackHelper.globalMediaPlayer.stop();
        }
        
        return isSuccessful;
    }
    

}